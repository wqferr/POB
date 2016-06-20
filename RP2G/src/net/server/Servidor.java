package net.server;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import net.Mensagem;
import net.Mensagem.Evento;
import core.Jogo;
import core.mapa.Posicao;
import exception.DesyncException;

public class Servidor {
	
	public static final int PORTA_PADRAO = 4479;
	
	private ServerSocket ss;
	private boolean ativo;
	private TratadorCliente[] clientes;
	
	private Jogo jogo;
	
	public Servidor() {
		this.clientes = new TratadorCliente[Jogo.NRO_JOGADORES];
		// TODO inicializar jogo
	}
	
	public void abrir() throws IOException {
		this.abrir(PORTA_PADRAO);
	}
	
	public void abrir(int porta) throws IOException {
		if (this.ss == null)
            this.ss = new ServerSocket(porta);
		else
			throw new IOException("Servidor já aberto");
	}
	
	public void fechar() throws IOException {
		this.ss.close();
	}

	public void start() throws DesyncException, IOException {
		this.ativo = true;
		int i = 0;
		while (i < this.clientes.length) {
			try {
				Socket s = this.ss.accept();
                this.clientes[i] = new TratadorCliente(this, s);
                System.err.println("Conexão nova com " + s.getInetAddress() + ";");
                i++;
			} catch(IOException e) {}
		}
		System.err.println("Clientes conectados.");
		
		for (TratadorCliente tc : this.clientes)
			new Thread(tc).start();
		
		for (i = 1; i < this.clientes.length; i++)
            this.clientes[i].notificar(Evento.INICIO_CONEXAO);
		
		boolean conf = false;
		try {
            conf = this.confirmarTodos();
		} catch (DesyncException e) {
			this.notificarDessincronia();
		} catch (IOException e) {
			this.notificarQueda();
		}
		
		if (!conf)
			this.notificarDessincronia();
	
		System.err.println("Enviando informações do jogo.");
		try {
            this.enviar(this.jogo);
		} catch (IOException e) {
			this.notificarQueda();
		}
		System.err.println("Enviando itens.");
		// TODO mandar arquivo de itens.
		System.err.println("Enviando personagens.");
		// TODO mandar informações sobre personagens
		
		
		boolean acabou = false;
		int vez = 0;
		Mensagem msg = null;
		
		this.clientes[0].notificar(Evento.INICIO_TURNO);
		
		while (!acabou) {
			try {
                msg = this.clientes[vez].receber();
			} catch (IOException e) {
				this.notificarQueda();
			}
			System.err.println(vez + ": " + msg);
            
			Scanner s = new Scanner(msg.getMsg());
			int j;
			
			switch (msg.getEvento()) {
                case MOVIMENTO:
                	i = s.nextInt();
                	j = s.nextInt();
                	
                	if (this.jogo.mover(new Posicao(i, j))) {
                        this.sinalizarTodosExceto(msg, vez);
                        conf = false;
                        try {
                            conf = this.confirmarTodosExceto(vez);
                        } catch (DesyncException e) {
                        	this.notificarDessincronia();
                        } catch (IOException e) {
                        	this.notificarQueda();
                        }
                        if (conf)
                            this.clientes[vez].notificar(Evento.CONFIRMACAO);
                        else
                            this.notificarDessincronia();
                    } else {
                        this.notificarDessincronia();
                    }
                        
                    break;
                    
                case ATAQUE:
                	i = s.nextInt();
                	j = s.nextInt();
                	
                	if (this.jogo.atacar(new Posicao(i, j))) {
                        this.sinalizarTodosExceto(msg, vez);
                        conf = false;
                        try {
                            conf = this.confirmarTodosExceto(vez);
                        } catch (DesyncException e) {
                        	this.notificarDessincronia();
                        } catch (IOException e) {
                        	this.notificarQueda();
                        }
                        if (conf)
                            this.clientes[vez].notificar(Evento.CONFIRMACAO);
                        else
                            this.notificarDessincronia();
                        
                        // TODO verificar fim do jogo
                    } else {
                        this.notificarDessincronia();
                    }
                	
                    break;
                    
                case FIM_TURNO:
                	this.jogo.proximoPersonagem();
                	this.notificarTodosExceto(Evento.FIM_TURNO, vez);
                	vez = (vez+1) % this.clientes.length;
                	this.clientes[vez].notificar(Evento.INICIO_TURNO);
                    break;
                    
                default:
                	// Pacote fora do protocolo
                	this.notificarDessincronia();
                    break;
			}
			s.close();
		}
		
		this.ativo = false;
	}
	
	private void enviar(Serializable obj) throws IOException {
		for (TratadorCliente tc : this.clientes)
			tc.enviar(obj);
	}
	
	private void sinalizarTodosExceto(Mensagem m, int c) throws IOException {
		for (int i = 0; i < c; i++)
			this.clientes[i].enviar(m);
		for (int i = c+1; i < this.clientes.length; i++)
			this.clientes[i].enviar(m);
	}
	
	private boolean confirmarTodos() throws DesyncException, IOException {
		boolean confirmado = true;
		for (int i = 0; i < this.clientes.length; i++) {
			Mensagem m = this.clientes[i].receber();
			if (m.getEvento() != Evento.CONFIRMACAO) {
				confirmado = false;
                if (m.getEvento() == Evento.DESSINCRONIA)
                    throw new DesyncException();
			}
		}
			
		return confirmado;
	}
	
	private boolean confirmarTodosExceto(int c) throws DesyncException, IOException {
		boolean confirmado = true;
		for (int i = 0; i < c; i++) {
			Mensagem m = this.clientes[i].receber();
			if (m.getEvento() != Evento.CONFIRMACAO) {
				confirmado = false;
                if (m.getEvento() == Evento.DESSINCRONIA)
                    throw new DesyncException();
			}
		}
		for (int i = c+1; i < this.clientes.length; i++) {
			Mensagem m = this.clientes[i].receber();
			if (m.getEvento() != Evento.CONFIRMACAO) {
				confirmado = false;
                if (m.getEvento() == Evento.DESSINCRONIA)
                    throw new DesyncException();
			}
		}
		return confirmado;
	}
	
	private void notificarTodosExceto(Evento e, int c) throws IOException {
		for (int i = 0; i < c; i++)
			this.clientes[i].notificar(e);
		for (int i = c+1; i < this.clientes.length; i++)
			this.clientes[i].notificar(e);
	}
	
	private void notificarTodos(Evento e, boolean ignorarExcecao) throws IOException {
		for (TratadorCliente tc : this.clientes) {
			try {
				tc.notificar(e);
			} catch (IOException exc) {
				if (!ignorarExcecao)
					throw exc;
			}
		}
	}
	
	private void notificarDessincronia() throws DesyncException, IOException {
		this.notificarTodos(Evento.DESSINCRONIA, true);
		
		throw new DesyncException("Dessincronia detectada.");
	}
	
	private void notificarQueda() throws IOException {
		this.notificarTodos(Evento.QUEDA_CONEXAO, true);
		
		throw new IOException("Queda de um ou mais clientes.");
	}
	
	public boolean isAtivo() {
		return this.ativo;
	}
	
}

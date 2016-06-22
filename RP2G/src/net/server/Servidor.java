package net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import net.Mensagem;
import net.Mensagem.Evento;
import core.Jogo;
import core.database.DatabaseHandler;
import core.mapa.Posicao;
import exception.DesyncException;

public class Servidor {
	
	public static final int PORTA_PADRAO = 4479;
	
	private ServerSocket ss;
	private boolean ativo;
	private TratadorCliente[] clientes;
	private int porta;
	
	private Jogo jogo;
	
	public Servidor(Jogo jogo) {
		this(jogo, PORTA_PADRAO);
	}
	
	public Servidor(Jogo jogo, int porta) {
		this.clientes = new TratadorCliente[Jogo.NRO_JOGADORES];
		this.jogo = jogo;
		this.porta = porta;
	}
	
	public void start() throws DesyncException, IOException {
        this.ss = new ServerSocket(this.porta);
		this.ativo = true;
		int i = 0;
		System.err.println("Abrindo servidor.");
		while (i < this.clientes.length) {
			try {
				Socket s = this.ss.accept();
                System.err.println("Conexão nova com " + s.getInetAddress() + ";");
                this.clientes[i] = new TratadorCliente(s);
                i++;
			} catch(IOException e) {
				System.err.println("ERRO");
			}
		}
		System.err.println("Clientes conectados.");
		
		for (TratadorCliente tc : this.clientes)
            tc.notificar(Evento.INICIO_CONEXAO);
		
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
	
		System.err.println("Enviando database.");
		for (TratadorCliente tc : this.clientes)
			DatabaseHandler.writeToStream(tc.getObjectOutputStream());
		
		System.err.println("Enviando informações do jogo.");
		try {
            this.enviar(this.jogo.getMapa());
            this.enviar(this.jogo.getPersonagensTime1());
            this.enviar(this.jogo.getPersonagensTime2());
		} catch (IOException e) {
			this.notificarQueda();
		}
		System.err.println("Informações transmitidas com êxito.");
		System.err.println("Iniciando jogo.");
		
		int vez = 0;
		Mensagem msg = null;
		
		this.clientes[0].notificar(Evento.INICIO_TURNO);
		
		while (!jogo.acabou()) {
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
                        	System.err.println("desync 1");
                        	this.notificarDessincronia();
                        } catch (IOException e) {
                        	this.notificarQueda();
                        }
                        if (conf)
                            this.clientes[vez].notificar(Evento.CONFIRMACAO);
                        else {
                        	System.err.println("desync 2");
                            this.notificarDessincronia();
                        }
                    } else {
                        System.err.println("desync 3");
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
                    } else {
                        this.notificarDessincronia();
                    }
                	
                    break;
                    
                case FIM_TURNO:
                	this.clientes[vez].notificar(Evento.CONFIRMACAO);
                	this.jogo.proximoPersonagem();
                	this.notificarTodos(Evento.FIM_TURNO, false);
                	vez = (vez+1) % this.clientes.length;
                	this.clientes[vez].notificar(Evento.INICIO_TURNO);
                    break;
                    
                default:
                	// Pacote fora do protocolo
                	this.notificarDessincronia();
                    break;
			}
			s.close();
			jogo.exibir(System.out::print);
		}
		
		for (TratadorCliente tc : this.clientes)
			tc.close();
		
		this.ativo = false;
		this.ss.close();
	}
	
	private void enviar(Object obj) throws IOException {
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
	
	public void notificarQueda() throws IOException {
		this.notificarTodos(Evento.QUEDA_CONEXAO, true);
		
		throw new IOException("Queda de um ou mais clientes.");
	}
	
	public boolean isAtivo() {
		return this.ativo;
	}
	
}

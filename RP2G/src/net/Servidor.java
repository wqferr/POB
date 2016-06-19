package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import exception.DesyncException;
import net.msg.Mensagem;
import net.msg.Mensagem.Evento;

public class Servidor {
	
	private ServerSocket ss;
	private TratadorCliente[] clientes;
	
	public Servidor() {
		this.clientes = new TratadorCliente[2];
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

	public void start() throws IOException {
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
	
		System.err.println("Enviando mapa.");
		// TODO mandar informações sobre mapa
		System.err.println("Enviando itens.");
		// TODO mandar arquivo de itens.
		System.err.println("Enviando personagens.");
		// TODO mandar informações sobre personagens
		
		// TODO criar objeto jogo
		boolean acabou = false;
		int vez = 0;
		
		while (!acabou) {
			Mensagem msg = null;
			try {
                msg = this.clientes[vez].receber();
			} catch (IOException e) {
				this.notificarQueda();
			}
			Scanner s = new Scanner(msg.getMsg());
			int j;
			
			switch (msg.getEvento()) {
                case MOVIMENTO:
                	i = s.nextInt();
                	j = s.nextInt();
                	
                	// if (mover(p, i, j)) {
                        this.sinalizarTodosExceto(msg, vez);
                        boolean conf = false;
                        try {
                            conf = this.confirmarTodosExceto(Evento.COMANDO_FEITO, vez);
                        } catch (DesyncException e) {
                        	this.notificarDessincronia();
                        } catch (IOException e) {
                        	this.notificarQueda();
                        }
                        if (conf)
                            this.clientes[vez].notificar(Evento.COMANDO_FEITO);
                        else
                            this.notificarDessincronia();
                    // } else {
                        this.notificarDessincronia();
                    // }
                        
                    break;
                    
                case ATAQUE:
                	i = s.nextInt();
                	j = s.nextInt();
                	// seguir modelo de MOVIMENTO
                	// (testar MOVIMENTO antes de copiar)
                    break;
                    
                case FIM_TURNO:
                    break;
                    
                case COMANDO_FEITO:
                    break;
                    
                case DESSINCRONIA:
                	this.notificarDessincronia();
                    break;
                    
                default:
                    break;
			}
			s.close();
		}
		
		// enquanto não acabou
		//		receber mensagem do jogador atual
		//			verificar queda
		
		//			mover (opcional)
		//				verificar se válido
		//					executar
		//					atualizar outro jogador
		//				verificar desync
		
		//			atacar (opcional)
		//				verificar se válido
		//					executar
		//					atualizar outro jogador
		//				verificar desync
		
		//			fim de turno
		//				atualizar outro jogador
		//				trocar para proximo personagem
	}
	
	public void sinalizar(Mensagem m) throws IOException {
		for (TratadorCliente tc : this.clientes)
			tc.enviar(m);
	}
	
	public void sinalizarTodosExceto(Mensagem m, int c) throws IOException {
		for (int i = 0; i < c; i++)
			this.clientes[i].enviar(m);
		for (int i = c+1; i < this.clientes.length; i++)
			this.clientes[i].enviar(m);
	}
	
	public boolean confirmarTodosExceto(Evento e, int c) throws IOException {
		boolean confirmado = true;
		for (int i = 0; i < c; i++) {
			Mensagem m = this.clientes[i].receber();
			if (m.getEvento() != e)
				confirmado = false;
			if (m.getEvento() == Evento.DESSINCRONIA)
				throw new DesyncException();
		}
		for (int i = c+1; i < this.clientes.length; i++) {
			Mensagem m = this.clientes[i].receber();
			if (m.getEvento() != e)
				confirmado = false;
			if (m.getEvento() == Evento.DESSINCRONIA)
				throw new DesyncException();
		}
		return confirmado;
	}
	
	public void notificarTodosExceto(Evento e, int c) throws IOException {
		for (int i = 0; i < c; i++)
			this.clientes[i].notificar(e);
		for (int i = c+1; i < this.clientes.length; i++)
			this.clientes[i].notificar(e);
	}
	
	public void notificarTodos(Evento e) throws IOException {
		this.notificarTodos(e, false);
	}
	
	public void notificarTodos(Evento e, boolean ignorarExcecao) throws IOException {
		for (TratadorCliente tc : this.clientes) {
			try {
				tc.notificar(e);
			} catch (IOException exc) {
				if (!ignorarExcecao)
					throw exc;
			}
		}
	}
	
	public void notificarDessincronia() throws IOException {
		this.notificarTodos(Evento.DESSINCRONIA, true);
		
		throw new DesyncException();
	}
	
	public void notificarQueda() throws IOException {
		this.notificarTodos(Evento.QUEDA_CONEXAO, true);
		
		throw new IOException("Queda de um ou mais clientes.");
	}
	
}

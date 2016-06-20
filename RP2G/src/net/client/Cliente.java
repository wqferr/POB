package net.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import net.Mensagem;
import net.Mensagem.Evento;
import net.server.Servidor;
import core.Jogo;
import exception.DesyncException;

public class Cliente {
	
	private Socket conexao;
	private boolean vez;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public void conectar(InetAddress ip) throws IOException {
		this.conectar(ip, Servidor.PORTA_PADRAO);
	}

	public void conectar(InetAddress ip, int porta) throws IOException {
		this.conexao = new Socket(ip, porta);
		this.in = new ObjectInputStream(this.conexao.getInputStream());
		this.out = new ObjectOutputStream(this.conexao.getOutputStream());
		Mensagem msg = this.receber();
		if (msg.getEvento() != Evento.INICIO_CONEXAO)
			this.notificarDessincronia();
		this.notificar(Evento.CONFIRMACAO);
		Jogo jogo = null;
		boolean acabou = false;
		
		while (!acabou) {
			msg = this.receber();
			
			switch (msg.getEvento()) {
                case INICIO_TURNO:
                    break;
                    
                case MOVIMENTO:
                    break;
                    
                case ATAQUE:
                    break;
                    
                case FIM_TURNO:
                	jogo.proximoPersonagem();
                    break;
                    
                case CONFIRMACAO:
                    break;
                    
                case DESSINCRONIA:
                    break;
                    
                case QUEDA_CONEXAO:
                    break;
                    
                default:
                    break;
			}
		}
	}
	
	public Mensagem receber() throws IOException {
		try {
			return (Mensagem) this.in.readObject();
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	public boolean confirmar(Evento e) throws DesyncException, IOException {
		Mensagem m = this.receber();
		if (m.getEvento() == Evento.DESSINCRONIA)
			throw new DesyncException();
		return m.getEvento() == e;
	}
	
	public void enviar(Mensagem m) throws IOException {
		this.out.writeObject(m);
	}
	
	public void notificar(Evento e) throws IOException {
		this.enviar(new Mensagem(e));
	}
	
	public void notificarDessincronia() throws DesyncException, IOException {
		this.notificar(Evento.DESSINCRONIA);
		throw new DesyncException();
	}

}

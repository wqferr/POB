package net.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import exception.DesyncException;
import net.Mensagem;
import net.Mensagem.Evento;
import net.server.Servidor;

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
		
		this.vez = "1".equals(msg.getMsg());
	}
	
	public Mensagem receber() throws IOException {
		try {
			return (Mensagem) this.in.readObject();
		} catch (ClassNotFoundException e) {
			return null;
		}
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

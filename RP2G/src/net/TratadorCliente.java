package net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import net.msg.Mensagem;
import net.msg.Mensagem.Evento;

public class TratadorCliente implements Runnable {
	
	private final Servidor servidor;
	private final Socket conexao;
	private final ObjectInputStream in;
	private final ObjectOutputStream out;

	public TratadorCliente(Servidor servidor, Socket conexao) throws IOException {
		this.servidor = servidor;
		this.conexao = conexao;
		this.in = new ObjectInputStream(this.conexao.getInputStream());
		this.out = new ObjectOutputStream(this.conexao.getOutputStream());
	}
	
	public boolean isConectado() {
		return !this.conexao.isOutputShutdown();
	}
	
	public void enviar(Mensagem m) throws IOException {
		this.out.writeObject(m);
	}
	
	public void notificar(Evento e) throws IOException {
		this.enviar(new Mensagem(e));
	}
	
	public Mensagem receber() throws IOException {
		try {
            return (Mensagem) this.in.readObject();
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	@Override
	public void run() {
		
	}
}

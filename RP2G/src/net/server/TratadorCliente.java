package net.server;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import net.Mensagem;
import net.Mensagem.Evento;

public class TratadorCliente implements Closeable {
	
	private Socket conexao;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private int i;

	public TratadorCliente(Socket conexao, int i) throws IOException {
		this.conexao = conexao;
		this.in = new ObjectInputStream(this.conexao.getInputStream());
		this.out = new ObjectOutputStream(this.conexao.getOutputStream());
		this.i = i;
	}
	
	public void enviar(Object obj) throws IOException {
		System.err.println("Enviado a " + i + ": " + obj);
		this.out.writeObject(obj);
	}
	
	public void notificar(Evento e) throws IOException {
		this.enviar(new Mensagem(e));
	}
	
	public Mensagem receber() throws IOException {
		try {
			Mensagem msg = (Mensagem) this.in.readObject();
			System.err.println("Recebido de " + i + ": " + msg);
            return msg;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	@Override
	public void close() throws IOException {
		this.conexao.close();
	}

	public ObjectOutputStream getObjectOutputStream() throws IOException {
		return this.out;
	}
	
}

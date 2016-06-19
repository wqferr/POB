package net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import net.msg.Mensagem;
import net.msg.Mensagem.Evento;

public class TratadorCliente implements Runnable {
	
	private Servidor servidor;
	private Socket conexao;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Queue<Mensagem> buffer;

	public TratadorCliente(Servidor servidor, Socket conexao) throws IOException {
		this.servidor = servidor;
		this.conexao = conexao;
		this.in = new ObjectInputStream(this.conexao.getInputStream());
		this.out = new ObjectOutputStream(this.conexao.getOutputStream());
		this.buffer = new LinkedList<>();
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
	
	public Mensagem receber() {
		if (this.buffer.isEmpty())
			return null;
		return this.buffer.remove();
	}

	@Override
	public void run() {
		while (!this.conexao.isClosed()) {
			try {
				this.buffer.add((Mensagem) this.in.readObject());
			} catch (ClassNotFoundException | IOException e) {
				try {
                    this.servidor.notificarDessincronia();
				} catch (IOException ex) {}
			}
		}
		if (this.servidor.isAtivo()) {
			try {
                this.servidor.notificarQueda();
			} catch (IOException e) {}
		}
	}
}

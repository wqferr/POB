package net.server;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import net.Mensagem;
import net.Mensagem.Evento;

/**
 * Classe para auxiliar IO com clientes do lado do Servidor.
 */
public class TratadorCliente implements Closeable {
	
	private Socket conexao;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private int i;

	/**
	 * Cria um novo tratador para a conexão dada.
	 * @param conexao A conexão a ser tratada
	 * @param i O id do cliente
	 * @throws IOException Se não for possível criar Object(In|Out)putStreams.
	 */
	public TratadorCliente(Socket conexao, int i) throws IOException {
		this.conexao = conexao;
		this.in = new ObjectInputStream(this.conexao.getInputStream());
		this.out = new ObjectOutputStream(this.conexao.getOutputStream());
		this.i = i;
	}
	
	/**
	 * Envia um objeto serializável a este cliente.
	 * @param obj O objeto a ser enviado
	 * @throws IOException Se houver erro durante a transmissão.
	 */
	public void enviar(Object obj) throws IOException {
		System.err.println("Enviado a " + i + ": " + obj);
		this.out.writeObject(obj);
	}
	
	/**
	 * Envia um evento a este cliente.
	 * @param e O evento a ser notificado
	 * @throws IOException Se houver erro durante a transmissão.
	 */
	public void notificar(Evento e) throws IOException {
		this.enviar(new Mensagem(e));
	}
	
	/**
	 * Espera um objeto serializável deste cliente.
	 * @return O objeto lido.
	 * @throws IOException Se houver erro durante a transmissão.
	 */
	public Mensagem receber() throws IOException {
		try {
			Mensagem msg = (Mensagem) this.in.readObject();
			System.err.println("Recebido de " + i + ": " + msg);
            return msg;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	/**
	 * Chama o método close() da conexão.
	 */
	public void close() throws IOException {
		this.conexao.close();
	}

	/**
	 * Retorna a OutputStream utilizada por este tratador.
	 * @return A OutputStream do tratador.
	 */
	public ObjectOutputStream getObjectOutputStream() {
		return this.out;
	}
	
}

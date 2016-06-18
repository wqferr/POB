package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import net.msg.Mensagem;

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

	public void start() {
		int i = 0;
		while (i < this.clientes.length) {
			try {
				Socket s = this.ss.accept();
                this.clientes[i] = new TratadorCliente(this, s);
                System.err.println("Conexão nova com " + s.getInetAddress() + ";");
                i++;
			} catch(IOException e) {}
		}
		System.err.println("Clientes conectados: iniciando sessão.");
		
		for (TratadorCliente tc : this.clientes)
			new Thread(tc).start();
	
		// TODO jogo
	}
	
	public void sinalizar(Mensagem m) throws IOException {
		for (TratadorCliente tc : this.clientes)
			tc.enviar(m);
	}

}

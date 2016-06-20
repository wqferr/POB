package net.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import net.server.Servidor;

public class Cliente {
	
	private Socket conexao;

	public void conectar(InetAddress ip, int porta) throws IOException {
		this.conexao = new Socket(ip, porta);
	}
	
	public void conectar(InetAddress ip) throws IOException {
		this.conectar(ip, Servidor.PORTA_PADRAO);
	}

}

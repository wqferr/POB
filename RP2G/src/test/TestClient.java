package test;

import java.io.IOException;
import java.net.InetAddress;

import net.client.Cliente;
import core.Jogo;

public class TestClient {

	public static void main(String[] args) {
		Cliente c = new Cliente((Jogo j) -> { 
			System.out.println("Acordado");
			return null;
		}, InetAddress.getLoopbackAddress());
		
		try {
			c.conectar();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

}

package net.test;

import java.io.IOException;
import java.net.InetAddress;

import net.client.Cliente;

public class TestClient {

	public static void main(String[] args) {
		Cliente c = new Cliente(() -> System.out.println("Acordado"));
		
		try {
			c.conectar(InetAddress.getLoopbackAddress());
		} catch (IOException e) {
			System.err.println(e);
		}
	}

}

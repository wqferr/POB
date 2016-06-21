package test;

import java.io.IOException;

import net.server.Servidor;

public class TestServer {

	public static void main(String[] args) {
		Servidor s = new Servidor(null);
		try {
            s.start();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

}

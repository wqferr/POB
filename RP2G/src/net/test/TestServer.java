package net.test;

import java.io.IOException;

import net.server.Servidor;

public class TestServer {

	public static void main(String[] args) {
		Servidor s = new Servidor();
		try {
            s.start();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

}

package test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

import net.client.Cliente;
import net.client.Comando;
import net.server.Servidor;
import core.Jogo;

public class TestClient {

	public static void main(String[] args) {
		boolean andou = false;
		boolean atacou = false;
		try (final Scanner in = new Scanner(System.in)) {
            in.useDelimiter("[^\\s]");
            Cliente c = new Cliente((Jogo j) -> {
                if (!in.hasNext())
                	return new Ordem(Comando.ENCERRAR);

                switch (in.next()) {
                case "m":
                	break;
                }
                return null;
            }, InetAddress.getLoopbackAddress(), Servidor.PORTA_PADRAO);
            in.close();
            
            try {
                c.conectar();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
	}

}

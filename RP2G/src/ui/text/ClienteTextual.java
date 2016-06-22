package ui.text;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import net.client.Cliente;
import net.client.Controlador;
import core.Jogo;

public class ClienteTextual {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner tec = new Scanner(System.in);
		
		tec.useDelimiter("[\\s]");
		System.out.print("IP do servidor: ");
		String hostName = tec.next();
		tec.nextLine();
		Controlador controle = (Jogo j) -> {
			tec.nextLine();
			return null;
		};
		Cliente cliente = null;
		try {
			cliente = new Cliente(controle, InetAddress.getByName(hostName));
		} catch (UnknownHostException e) {
			System.err.println("IP inválido.");
			System.exit(1);
		}
		
		try {
            cliente.conectar();
            System.out.println("Conectado ao servidor");
            cliente.start();
            System.out.println("FIM DE JOGO");
		} catch (IOException e) {
			System.out.println("ERRO: " + e);
		}
	}

}

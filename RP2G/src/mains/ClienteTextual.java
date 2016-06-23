package mains;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import net.client.Cliente;
import ui.text.ControladorTextual;

public class ClienteTextual {
	
	public static void main(String[] args) {
		Scanner tec = new Scanner(System.in);
		System.out.print("Digite o IP do servidor: ");
		String host = tec.nextLine().trim();
		Cliente c = null;
		try {
			c = new Cliente(new ControladorTextual(tec), InetAddress.getByName(host));
		} catch (UnknownHostException e) {
			System.out.println("IP inválido");
		}
		
		try {
			c.conectar();
		} catch (IOException e) {
			System.err.println("Erro ao tentar conectar ao servidor: " + e);
			System.exit(1);
		}
		
		c.getJogo().exibir();
		
		try {
			c.start();
		} catch (IOException e) {
			System.err.println("Erro durante a execução: " + e);
			System.exit(2);
		}
	}

}

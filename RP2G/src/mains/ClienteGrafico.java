package mains;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import net.client.Cliente;
import net.server.Servidor;
import ui.graphic.JanelaJogo;

/**
 * Classe com o método Main para a interface gráfica do jogo
 * @author wheatley
 *
 */

public class ClienteGrafico {

	public static void main(String[] args) throws UnknownHostException {
		Scanner scan = new Scanner(System.in);
		System.out.println("IP do Servidor:");
		Cliente client = new Cliente(null, InetAddress.getByName(scan.nextLine()), Servidor.PORTA_PADRAO);
		try { client.conectar(); }
		catch(IOException e) { e.printStackTrace(); }
		
		JanelaJogo win = new JanelaJogo(client.getJogo(), client);
		win.setVisible(true);
		client.setControlador(win);
		client.getJogo().setOuvinte(win);
		
		try { client.start(); }
		catch(IOException e) { e.printStackTrace(); }
		scan.close();
	}
}

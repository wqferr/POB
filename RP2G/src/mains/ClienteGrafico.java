package mains;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import ui.graphic.JanelaJogo;
import core.Jogo;
import core.Ordem;
import core.Ordem.Comando;
import core.mapa.Posicao;
import net.client.Cliente;
import net.client.Controlador;
import net.server.Servidor;

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
		scan.close();
		
		JanelaJogo win = new JanelaJogo(client.getJogo(), client);
		win.setVisible(true);
		client.setControlador(win);
		client.getJogo().setOuvinte(win);
		
		try { client.start(); }
		catch(IOException e) { e.printStackTrace(); }
	}
}

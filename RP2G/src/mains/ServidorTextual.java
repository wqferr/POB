package mains;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

import net.server.Servidor;
import core.Jogo;
import core.database.DatabaseHandler;
import core.mapa.Mapa;

/**
 * Classe com o método Main para o servidor
 */

public class ServidorTextual {
	public static void main(String[] args) {
		new DatabaseHandler();
		
		@SuppressWarnings("resource") // fechar o Scanner fecharia stdin
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Nome do Mapa:");
		Mapa map = Mapa.get(scan.nextLine());
		Random rng = new Random();
		Jogo game = new Jogo(map, rng);
		
		Servidor server = new Servidor(game, rng);
		
		try {
			System.out.println(InetAddress.getLocalHost());
		} catch (UnknownHostException e1) {
			System.out.println("Não foi possível recuperar seu endereço IP");
		}
		
		try { server.start(); }
		catch (IOException e){ System.err.println(e); }
	}
}

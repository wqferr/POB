package mains;

import java.io.IOException;
import java.util.Scanner;

import net.server.Servidor;
import core.Jogo;
import core.database.DatabaseHandler;
import core.mapa.Mapa;

/**
 * Classe com o método Main para o servidor
 *
 */

public class ServidorTextual {
	public static void main(String[] args) {
		DatabaseHandler dbHandle = new DatabaseHandler();
		
		Scanner scan = new Scanner(System.in);
		System.out.println("Nome do Mapa:");
		Mapa map = Mapa.get(scan.nextLine());
		Jogo game = new Jogo(map);
		scan.close();
		
		Servidor server = new Servidor(game);
		try { server.start(); }
		catch (IOException e){ System.err.println(e); }
	}
}

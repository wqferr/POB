package mains;

import java.io.IOException;

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
		Mapa map = Mapa.get("Map1");
		Jogo game = new Jogo(map);
		
		Servidor server = new Servidor(game);
		try {
            server.start();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}

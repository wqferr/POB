package test;

import java.io.IOException;
import java.util.Arrays;

import net.server.Servidor;
import core.Jogo;
import core.database.DatabaseHandler;
import core.mapa.Mapa;
import core.personagem.Personagem;
import core.personagem.Profissao;
import exception.ItemInvalidoException;
import exception.NomeRepetidoException;

/**
 * Classe com a funcao Main para o Servidor
 */

public class TestServer {

	public static void main(String[] args) {
		new DatabaseHandler();
		
		Personagem p1 = null, p2 = null;
		try {
			p1 = new Personagem("guerreiro", Profissao.GUERREIRO, 10, 2, 3, 1, 1);
			p2 = new Personagem("mago", Profissao.MAGO, 10, 2, 1, 3, 1);
		} catch (NomeRepetidoException e) {
			throw new IllegalArgumentException();
		}
		
		try {
			p1.adicionar("Espada Bastarda");
            p1.setArma("Espada Bastarda");
            p1.adicionar("Whey");
            p2.adicionar("Bola de Fogo");
            p2.setArma("Bola de Fogo");
            p2.adicionar("Pot");
		} catch (ItemInvalidoException e) {}
		
		Mapa m = Mapa.get("Map1");
		Jogo j = new Jogo(m, Arrays.asList(p1), Arrays.asList(p2));
		
		Servidor s = new Servidor(j, System.nanoTime());
		try {
            s.start();
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			try {
				s.close();
			} catch (IOException e) {}
		}
	}

}

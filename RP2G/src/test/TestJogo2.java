package test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import net.server.Servidor;
import core.Jogo;
import core.database.DatabaseHandler;
import core.mapa.Mapa;
import core.personagem.Personagem;
import exception.ItemInvalidoException;

/**
 * Classe com a funcao Main sem utilizar cliente mas utilizando DataBase
 */

public class TestJogo2 {

	public static void main(String[] args) {
		new DatabaseHandler();
		Iterator<Entry<String, Personagem>> iter = Personagem.getIterator();
		
		while (iter.hasNext()) {
			Personagem p = iter.next().getValue();
			switch (p.getProfissao()) {
                case GUERREIRO:
                    p.adicionar("Espada Bastarda");
                    p.adicionar("Whey");
                    try {
                        p.setArma("Espada Bastarda");
                    } catch (ItemInvalidoException e1) {}
                    break;
                    
                case MAGO:
                	p.adicionar("Bola de Fogo");
                	p.adicionar("Pot");
                    try {
                        p.setArma("Bola de Fogo");
                    } catch (ItemInvalidoException e) {}
                	break;
                	
                default:
                    break;
			}
		}
		
		Mapa m = Mapa.get("Map1");
		Random r = new Random();
		Jogo j = new Jogo(m, r);
		
		Servidor s = new Servidor(j, r);
		try {
            s.start();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

}

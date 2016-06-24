package mains;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;

import net.server.Servidor;
import core.Jogo;
import core.database.DatabaseHandler;
import core.mapa.Mapa;
import core.personagem.Personagem;
import exception.ItemInvalidoException;

/**
 * Classe com o método Main para o servidor
 */

public class ServidorTextual {
	
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
		
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Nome do Mapa:");
		Mapa map = Mapa.get(scan.nextLine());
		
        Jogo game = new Jogo(map);
        Servidor server = new Servidor(game, System.nanoTime());
        
        try {
            server.start();
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            try {
                server.close();
            } catch (IOException e) {}
        }
	}
}

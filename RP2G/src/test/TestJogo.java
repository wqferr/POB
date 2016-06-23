package test;

import java.io.File;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

import ui.graphic.JanelaJogo;
import core.Jogo;
import core.database.DatabaseHandler;
import core.item.arma.Espada;
import core.item.arma.Livro;
import core.mapa.Mapa;
import core.personagem.Personagem;
import core.personagem.Profissao;

/**
 * Classe com a funcao Main para o jogo sem utilizar ciente nem DataBase
 */

public class TestJogo {

	public static void main(String[] args) throws Exception{
		new DatabaseHandler();
		
		Personagem p1 = null, p2 = null;
		p1 = new Personagem("guerreiro", Profissao.GUERREIRO, 20, 2, 3, 1, 2);
		p2 = new Personagem("mago", Profissao.MAGO, 10, 2, 1, 5, 2);
		p1.adicionar(new Espada("Espada Bastarda", 3, 1));
        p1.setArma("Espada Bastarda");
        p2.adicionar(new Livro("Bola de Fogo", 4, 5));
        p2.setArma("Bola de Fogo");
		
        Mapa m = null;
		m = new Mapa("Map1", ImageIO.read(new File("db/Map1.png")));
		Jogo j = new Jogo(m, Arrays.asList(p1), Arrays.asList(p2), new Random());
        
        JanelaJogo jan = new JanelaJogo(j);
        jan.setVisible(true);
	}

}

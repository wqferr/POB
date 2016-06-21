package test;

import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

import core.JanelaJogo;
import core.Jogo;
import core.database.DatabaseHandler;
import core.mapa.Mapa;
import core.mapa.Posicao;
import core.mapa.Quadrado;
import core.personagem.Personagem;
import core.personagem.Profissao;
import core.personagem.Personagem.Stat;
import exception.ItemInvalidoException;
import exception.NomeRepetidoException;

public class TestJogo {

	public static void main(String[] args) {
		new DatabaseHandler();
		
		Personagem p1 = null, p2 = null;
		try {
			p1 = new Personagem("guerreiro", Profissao.GUERREIRO, 10, 2, 3, 1, 1);
			p2 = new Personagem("mago", Profissao.MAGO, 10, 2, 1, 3, 1);
		} catch (NomeRepetidoException e) {}
		
		try {
			p1.adicionar("Espada Bastarda");
            p1.setArma("Espada Bastarda");
            System.out.println(p1.getArma().getDanoBase());
            p2.adicionar("Bola de Fogo");
            p2.setArma("Bola de Fogo");
            System.out.println(p2.getArma().getDanoBase());
		} catch (ItemInvalidoException e) {}
		
		Mapa m = Mapa.get("mapa1");
		for (Posicao p : m.getSpawnPointsTime1())
			System.err.println(p);
		System.err.println();
		for (Posicao p : m.getSpawnPointsTime2())
			System.err.println(p);
		System.err.println();
		
		Jogo j = new Jogo(m, Arrays.asList(p1), Arrays.asList(p2));
		JanelaJogo win = new JanelaJogo(j);
		win.setVisible(true);
			
        boolean podeMover = true;
        Pattern p = Pattern.compile("[^\\s]");
        
        try (Scanner s = new Scanner(System.in)) {
            while (!j.acabou()) {
            	System.out.println("Vez: " + j.personagemAtual().getNome());
                int l, c;
                desenharTabuleiro(j, p1, p2);
                
                String cmd = s.next(p);
                
                switch (cmd) {
                    case "m":
                    	if (podeMover) {
                            l = s.nextInt();
                            c = s.nextInt();
                            if (j.mover(new Posicao(l, c)))
                                podeMover = false;
                            else
                            	System.out.println("movimento invalido");
                    	}
                        break;
                        
                    case "a":
                        l = s.nextInt();
                        c = s.nextInt();
                        if (!j.atacar(new Posicao(l, c))) {
                        	System.out.println("movimento invalido");
                        	break;
                        }
                    
                    case "f":
                        podeMover = true;
                    	j.proximoPersonagem();
                    	break;
                }
                
                s.nextLine();
            }
            System.out.println("FIM");
		}
	}
	
	private static void desenharTabuleiro(Jogo jogo, Personagem p1, Personagem p2) {
		System.out.print(p1.getHp() + "/" + p1.getStat(Stat.HP_MAX));
		System.out.print('\t');
		System.out.println(p2.getHp() + "/" + p2.getStat(Stat.HP_MAX));
		
		Mapa m = jogo.getMapa();
		for (int i = 0; i < m.getNLinhas(); i++) {
			for (int j = 0; j < m.getNColunas(); j++) {
				Quadrado q = m.getQuadrado(new Posicao(i, j));
				Personagem p = q.getOcupante();
				if (p == null) {
					if (q.isTransponivel())
						System.out.print(' ');
					else
						System.out.print('+');
				} else {
					System.out.print(p.getNome().charAt(0));
				}
				System.out.print(' ');
			}
			System.out.println();
		}
	}

}

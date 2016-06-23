package ui.text;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import net.client.Controlador;
import utils.struct.Par;
import core.Jogo;
import core.Ordem;
import core.Ordem.Comando;
import core.item.Item;
import core.item.arma.Arma;
import core.mapa.Posicao;
import core.personagem.Personagem;
import core.personagem.Personagem.Stat;

/**
 * Classe de Interface Textual para o cliente
 */

public class ControladorTextual implements Controlador {
	
	private Scanner in;
	
	public ControladorTextual(Scanner in) {
		this.in = in;
	}
	
	public ControladorTextual() {
		this(new Scanner(System.in));
	}
	
	public Ordem proximaOrdem(Jogo jogo) {
        jogo.exibir();
        while (!jogo.acabou()) {
            try {
                System.out.println();
                String cmd = in.next();
                
                switch (cmd) {
                    case "m":
                        Posicao p = new Posicao(in.nextInt(), in.nextInt());
                        in.nextLine();
                        if (jogo.podeMover(p))
                            return new Ordem(Comando.MOVER, p);
                        else
                        	System.out.println("Comando inválido");
                        break;
                        
                    case "a":
                        p = new Posicao(in.nextInt(), in.nextInt());
                        in.nextLine();
                        if (jogo.podeAtacar(p))
                            return new Ordem(Comando.ATACAR, p);
                        else
                        	System.out.println("Comando inválido");
                        break;
                        
                    case "i":
                        p = new Posicao(in.nextInt(), in.nextInt());
                        in.nextLine();
                        Personagem personagem = jogo.getMapa().getQuadrado(p).getOcupante();
                        if (personagem == null) {
                            System.out.println("Posição desocupada");
                        } else {
                            System.out.println(personagem.getNome());
                            int hpMax = personagem.getStat(Stat.HP_MAX);
                            int porcentHp = (10*personagem.getHp()) / hpMax;
                            System.out.printf("HP: %d/%d [", personagem.getHp(), hpMax);
                            int i = 0;
                            while (i < porcentHp) {
                                System.out.print('#');
                                i++;
                            }
                            while (i < 10) {
                                System.out.print('-');
                                i++;
                            }

                            System.out.printf("] %.2f%s\n", (100f * personagem.getHp()) / hpMax, "%");
                            
                            System.out.println("\nStats:");
                            System.out.printf("Força:\t%d\n", personagem.getStat(Stat.FOR));
                            System.out.printf("Inteligência:\t%d\n", personagem.getStat(Stat.INT));
                            System.out.printf("Destreza:\t%d\n", personagem.getStat(Stat.DEX));
                            System.out.printf("Velocidade:\t%d\n", personagem.getStat(Stat.VEL));
                            System.out.println();
                            
                            Arma a = personagem.getArma();
                            if (a == null) {
                                System.out.println("Nenhuma arma equipada");
                            } else {
                                System.out.printf("Arma: %s\n", a.getNome());
                                System.out.printf("Dano base: %d\t", a.getDanoBase());
                                System.out.printf("Alcance: %d\n", a.getAlcance());
                            }
                        }
                        break;
                        
                    case "u":
                        String item = in.nextLine().trim();
                        if (jogo.podeUsar(item))
                            return new Ordem(Comando.USAR, item);
                        else
                        	System.out.println("Comando inválido");
                        break;
                        
                    case "l":
                        System.out.println("Inventario:\n");
                        List<Par<Item, Integer>> itens = jogo.personagemAtual().getItens();
                        if (itens.size() == 0)
                        	System.out.println("Vazio");
                        else
                            for (Par<Item, Integer> par : itens)
                                System.out.printf("%s: %d\n", par.getV1().getNome(), par.getV2());
                        System.out.println();
                        in.nextLine();
                        break;
                        
                    case "M":
                        jogo.exibir();
                        in.nextLine();
                        break;
                        
                    case "f":
                        in.nextLine();
                        System.out.println("================");
                        System.out.println("  FIM DE TURNO");
                        return new Ordem(Comando.ENCERRAR);
                    
                    case "h":
                        System.out.println("m i j\tMover para linha i coluna j");
                        System.out.println("a i j\tAtacar linha i coluna j");
                        System.out.println("u n  \tUsar item de nome n");
                        System.out.println("i    \t");
                        System.out.println("l    \tLista itens do personagem atual");
                        System.out.println("M    \tExibir mapa novamente");
                        System.out.println("f    \tEncerrar o turno");
                        System.out.println("h    \tMostrar esta lista");
                        System.out.println("Coordenadas dadas na sequência linha, coluna");
                        System.out.println("Time atual em letras maiúsculas, time adversário em minúsculas");
                        in.nextLine();
                        break;
                        
                    default:
                        in.nextLine();
                        System.out.println("Comando inválido");
                }
            } catch (InputMismatchException e) {
                System.out.println("Comando inválido");
            }
        }
        
        return new Ordem(Comando.ENCERRAR);
	}
	
}

package ui.text;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import net.client.Cliente;
import net.client.Controlador;
import struct.Par;
import core.Jogo;
import core.Ordem;
import core.Ordem.Comando;
import core.item.Item;
import core.item.arma.Arma;
import core.mapa.Posicao;
import core.personagem.Personagem;
import core.personagem.Personagem.Stat;

public class ClienteTextual {

	public static void main(String[] args) {
		// Fechar o scanner fecharia a stdin
		@SuppressWarnings("resource")
		Scanner tec = new Scanner(System.in);
		
		tec.useDelimiter("[\\s]");
		System.out.print("IP do servidor: ");
		String hostName = tec.next();
		tec.nextLine();
		
		Controlador controle = (Jogo jogo) -> {
            System.out.println("INÍCIO DE TURNO");
            System.out.println("===============");
            jogo.exibir();
			while (!jogo.acabou()) {
				try {
					System.out.println("Personagem atual: " + jogo.personagemAtual().getPosicao());
					System.out.println();
                    String cmd = tec.next();
                    
                    System.err.println(cmd);
                    switch (cmd) {
                        case "m":
                            Posicao p = new Posicao(tec.nextInt(), tec.nextInt());
                            tec.nextLine();
                            if (jogo.podeMover(p))
                            	return new Ordem(Comando.MOVER, p);
                            break;
                            
                        case "a":
                            p = new Posicao(tec.nextInt(), tec.nextInt());
                            tec.nextLine();
                            if (jogo.podeAtacar(p))
                            	return new Ordem(Comando.ATACAR, p);
                            break;
                            
                        case "i":
                        	p = new Posicao(tec.nextInt(), tec.nextInt());
                            tec.nextLine();
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
                        	String item = tec.nextLine().trim();
                        	if (jogo.podeUsar(item))
                        		return new Ordem(Comando.USAR, item);
                            break;
                            
                        case "l":
                        	System.out.println("Inventario:\n");
                        	List<Par<Item, Integer>> itens = jogo.personagemAtual().getItens();
                        	for (Par<Item, Integer> par : itens)
                        		System.out.printf("%s: %d\n", par.getV1().getNome(), par.getV2());
                        	System.out.println();
                        	tec.nextLine();
                        	break;
                        	
                        case "M":
                        	jogo.exibir();
                        	tec.nextLine();
                        	break;
                            
                        case "f":
                        	tec.nextLine();
                            System.out.println("================");
                            System.out.println("  FIM DE TEXTO  ");
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
                        	tec.nextLine();
                        	break;
                            
                        default:
                        	tec.nextLine();
                        	System.out.println("Comando inválido");
                    }
                } catch (InputMismatchException e) {
                	System.out.println("Comando inválido");
                }
			}
			
			return new Ordem(Comando.ENCERRAR);
		};
		Cliente cliente = null;
		try {
			cliente = new Cliente(controle, InetAddress.getByName(hostName));
		} catch (UnknownHostException e) {
			System.err.println("IP inválido.");
			System.exit(1);
		}
		
		try {
            cliente.conectar();
            final Jogo jogo = cliente.getJogo();
            jogo.setOuvinte((Void v) -> {
            	jogo.exibir();
            });
            System.out.println("Conectado ao servidor");
            cliente.start();
            System.out.println("FIM DE JOGO");
		} catch (IOException e) {
			System.out.println("ERRO: " + e);
		}
	}

}

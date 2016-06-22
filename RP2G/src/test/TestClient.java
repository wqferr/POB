package test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

import ui.JanelaJogo;
import net.client.Cliente;
import net.client.Controlador;
import net.server.Servidor;
import core.Jogo;
import core.Ordem;
import core.Ordem.Comando;
import core.mapa.Posicao;
import core.personagem.Personagem;
import core.personagem.Personagem.Stat;

public class TestClient {
	
	private static boolean andou = false;
	private static boolean atacou = false;

	public static void main(String[] args) {
        @SuppressWarnings("resource")
        final Scanner in = new Scanner(System.in);
        in.useDelimiter("[\\s]");
        
        Controlador cont = (Jogo jogo) -> {
        	System.out.println();
        	Personagem g = Personagem.get("guerreiro");
        	Personagem m = Personagem.get("mago");
        	
        	System.out.print(String.format("%d/%d\t", g.getHp(), g.getStat(Stat.HP_MAX)));
        	System.out.print(String.format("%d/%d\n", m.getHp(), m.getStat(Stat.HP_MAX)));
        	jogo.exibir(System.out::print);
            if (andou && atacou) {
                andou = false;
                atacou = false;
                return new Ordem(Comando.ENCERRAR);
            }
            
            int i, j;
            
            while (true) {
            	String cmd = in.next();
            	System.err.println(cmd);
                switch (cmd) {
                    case "m":
                        i = in.nextInt();
                        j = in.nextInt();
                        
                        if (!andou && jogo.podeMover(new Posicao(i, j))) {
                            andou = true;
                            return new Ordem(Comando.MOVER, new Posicao(i, j));
                        }
                        break;
                        
                    case "a":
                        i = in.nextInt();
                        j = in.nextInt();
                        
                        if (!atacou && jogo.podeAtacar(new Posicao(i, j))) {
                            atacou = true;
                            return new Ordem(Comando.ATACAR, new Posicao(i, j));
                        }
                        break;
                        
                    case "f":
                        andou = false;
                        atacou = false;
                        return new Ordem(Comando.ENCERRAR);
                }
                in.nextLine();
            }
        };
        Cliente c = new Cliente(cont, InetAddress.getLoopbackAddress(), Servidor.PORTA_PADRAO);
        
        try {
            c.conectar();
        } catch (IOException e) {
            System.err.println(e);
        }
        
        JanelaJogo win = new JanelaJogo(c.getJogo());
	}

}

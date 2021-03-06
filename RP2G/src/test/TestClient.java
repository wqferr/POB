package test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import net.client.Cliente;
import net.client.Controlador;
import net.server.Servidor;
import ui.graphic.JanelaJogo;
import core.Jogo;
import core.Ordem;
import core.Ordem.Comando;
import core.mapa.Posicao;

/**
 * Classe com a funcao Main para o Cliente
 */

public class TestClient {
	
	private static boolean andou = false;
	private static boolean atacou = false;

	public static void main(String[] args) throws UnknownHostException {
        @SuppressWarnings("resource")
        final Scanner in = new Scanner(System.in);
        in.useDelimiter("[\\s]");
        
        Controlador cont = (Jogo jogo) -> {
        	System.out.println();
        	
        	jogo.exibir();
            if (andou && atacou) {
                andou = false;
                atacou = false;
                return new Ordem(Comando.ENCERRAR);
            }
            
            int i, j;
            
            while (!jogo.acabou()) {
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
            
            return new Ordem(Comando.ENCERRAR);
        };
        Cliente c = new Cliente(cont, InetAddress.getLocalHost(), Servidor.PORTA_PADRAO); 
        
        try {
            c.conectar();
            JanelaJogo win = new JanelaJogo(c.getJogo(), c);
            c.setControlador(win);
            c.getJogo().setOuvinte(win);
            win.setVisible(true);
			c.start();
        } catch (IOException e) {
            System.err.println(e);
        }
	}

}

package test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

import net.client.Cliente;
import net.client.Controlador;
import net.client.Ordem;
import net.client.Ordem.Comando;
import net.server.Servidor;
import core.Jogo;
import core.mapa.Posicao;

public class TestClient {
	
	private static boolean andou = false;
	private static boolean atacou = false;

	public static void main(String[] args) {
        @SuppressWarnings("resource")
        final Scanner in = new Scanner(System.in);
        in.useDelimiter("[\\s]");
        
        Controlador cont = (Jogo jogo) -> {
        	System.out.println();
        	jogo.exibir(System.out::print);
            if (andou && atacou) {
                andou = false;
                atacou = false;
                return new Ordem(Comando.ENCERRAR);
            }
            
            int i, j;
            
            while (true) {
                switch (in.next()) {
                    case "m":
                        i = in.nextInt();
                        j = in.nextInt();
                        
                        System.err.println(i + " " + j + " " + andou);
                        if (!andou && jogo.mover(new Posicao(i, j))) {
                        	System.err.println(1);
                            andou = true;
                            return new Ordem(Comando.MOVER, i, j);
                        }
                        break;
                        
                    case "a":
                        i = in.nextInt();
                        j = in.nextInt();
                        
                        if (!atacou && jogo.atacar(new Posicao(i, j))) {
                            atacou = true;
                            return new Ordem(Comando.ATACAR, i, j);
                        }
                        break;
                        
                    case "f":
                        andou = false;
                        atacou = false;
                        return new Ordem(Comando.ENCERRAR);
                }
            }
        };
        Cliente c = new Cliente(cont, InetAddress.getLoopbackAddress(), Servidor.PORTA_PADRAO);
        
        try {
            c.conectar();
        } catch (IOException e) {
            System.err.println(e);
        }
	}

}

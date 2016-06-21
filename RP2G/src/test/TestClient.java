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
        Controlador cont = (Jogo jogo) -> {
            in.useDelimiter("[\\s]");
            if (andou && atacou) {
            	System.err.println(1);
                andou = false;
                atacou = false;
                return new Ordem(Comando.ENCERRAR);
            }
            
            int i, j;
            String cmd;
            System.err.println(2);
            
            while (true) {
                switch (in.next()) {
                    case "m":
                        i = in.nextInt();
                        j = in.nextInt();
                        
                        if (!andou && jogo.mover(new Posicao(i, j))) {
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

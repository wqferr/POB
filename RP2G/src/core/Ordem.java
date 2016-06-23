package core;

import java.util.Scanner;

import net.Mensagem;
import core.mapa.Posicao;

/**
 * Classe que representa uma ordem dada a um personagem.
 */
public class Ordem {
	
	/**
	 * Enum de opções que podem ser feitas em uma jogada.
	 */
	public static enum Comando {
		MOVER,
		ATACAR,
		USAR,
		ENCERRAR;
	}

	private Comando c;
	private Object arg;
	
	/**
	 * Cria uma nova ordem sem argumentos.
	 * @param c O comando da ordem
	 */
	public Ordem(Comando c) {
		this(c, null);
	}
	
	/**
	 * Cria uma nova ordem.
	 * @param c O comando da ordem
	 * @param arg O argmento do comando
	 */
	public Ordem(Comando c, Object arg) {
		this.c = c;
		this.arg = arg;
	}
	
	/**
	 * Interpreta uma mensagem e transforma-a em uma ordem.
	 * @param m A mensagem a ser interpretada.
	 */
	public Ordem(Mensagem m) {
		this.c = m.getEvento().getCorrespondente();
		try (Scanner s = new Scanner(m.getMsg())) {
            switch (this.c) {
                case MOVER:
                case ATACAR:
                    this.arg = new Posicao(s.nextInt(), s.nextInt());
                    break;
                    
                case USAR:
                	this.arg = s.next();
                    
                default:
                    this.arg = null;
            }
		}
	}
	
	/**
	 * Retorna o argumento desta ordem.
	 * @return O argumento.
	 */
	public Object getArg() {
		return this.arg;
	}
	
	/**
	 * Retorna o comando desta ordem.
	 * @return O comando.
	 */
	public Comando getComando() {
		return this.c;
	}
	
	/**
	 * Converte a ordem para string
	 */
	@Override
	public String toString() {
		return this.c + ": " + String.valueOf(this.arg);
	}

}

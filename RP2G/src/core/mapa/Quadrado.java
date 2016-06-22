package core.mapa;
import java.io.Serializable;

import core.personagem.Personagem;

public class Quadrado implements Serializable {

	private static final long serialVersionUID = 1366852158355341746L;
	
	private Posicao posicao;	
	private boolean transponivel;
	private Personagem ocupante;

	/**
	 * Constrói um quadrado 
	 * @param posicao Posição 
	 * @param transponivel Se o quadrado é transponível
	 * @param ocupante Personagem que irá ocupar o quadrado do mapa
	 */
	public Quadrado(Posicao posicao, boolean transponivel, Personagem ocupante) {
		this.posicao = posicao;
		this.transponivel = transponivel;
		this.ocupante = ocupante;
	}
	
	/**
	 * Constrói um quadrado intransponível
	 * @param posicao Posição 
	 */
	public Quadrado(Posicao posicao) {
		this(posicao, false);
	}

	/**
	 * Constrói um quadrado sem nenhum personagem ocupando-o
	 * @param posicao Posição
	 * @param transponivel Se o quadrado é transponível
	 */
	public Quadrado(Posicao posicao, boolean transponivel) {
		this(posicao, transponivel, null);
	}

	/**
	 * Retorna a posição
	 * @return Retona a posição
	 */
	public Posicao getPosicao() {
		return this.posicao;
	} 

	/**
	 * Retorna o personagem que está no quadrado
	 * @return O personagem que está ocupando o quadrado
	 */
	public Personagem getOcupante() {
		return this.ocupante;
	}

	/**
	 * Ocupa um quadrado com um personagem
	 * @param p Personagem para ocupar um quadrado do mapa
	 */
	public void setOcupante(Personagem p) {
		this.ocupante = p;
		if (p != null)
            p.mover(this.posicao);
	}

	/**
	 * Verifica se uma posição do mapa é tranponível
	 * @return Se a posição é transponível
	 */
	public boolean isTransponivel() {
		return this.ocupante == null && this.transponivel;
	}

	/**
	 * Verifica se a posição do mapa está ocupada
	 * @return Se a posição está ocupada
	 */
	public boolean isOcupado() {
		return this.ocupante != null;
	}

}

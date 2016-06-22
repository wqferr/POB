package core.mapa;
import java.io.Serializable;

import core.personagem.Personagem;

public class Quadrado implements Serializable {

	private static final long serialVersionUID = 1366852158355341746L;
	
	private Posicao posicao;	
	private boolean transponivel;
	private Personagem ocupante;

	public Quadrado(Posicao posicao, boolean transponivel, Personagem ocupante) {
		this.posicao = posicao;
		this.transponivel = transponivel;
		this.ocupante = ocupante;
	}
		
	public Quadrado(Posicao posicao) {
		this(posicao, false);
	}
	
	public Quadrado(Posicao posicao, boolean transponivel) {
		this(posicao, transponivel, null);
	}
	
	public Posicao getPosicao() {
		return this.posicao;
	} 
	
	public Personagem getOcupante() {
		return this.ocupante;
	}
	
	public void setOcupante(Personagem p) {
		this.ocupante = p;
		if (p != null)
            p.mover(this.posicao);
	}
	
	public boolean isTransponivel() {
		return this.transponivel;
	}
	
	public boolean isOcupado() {
		return this.ocupante != null;
	}

}

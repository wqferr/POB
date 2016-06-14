package core.item.arma;

import core.item.Item;
import core.personagem.Profissao;
import core.personagem.Personagem;

public abstract class Arma extends Item {
	int danoBase;
	int alcance;
	
	public Arma(String nome, int danoBase, int alcance){
		super(nome);
		this.danoBase = danoBase;
		this.alcance = alcance;
	}
		
	 public abstract void atacar(Personagem pA, Personagem pB);
	 
	 public abstract int calcularDano(Personagem pA, Personagem pB);
}

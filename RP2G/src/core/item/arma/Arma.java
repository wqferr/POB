package core.item.arma;

import core.item.Item;
import core.personagem.Personagem;
import core.personagem.Profissao;

public abstract class Arma extends Item {
	protected int danoBase;
	protected int alcance;
	
	public Arma(String nome, int danoBase, int alcance){
		super(nome);
		this.danoBase = danoBase;
		this.alcance = alcance;
	}	 
	
	public int getDanoBase(){
		return this.danoBase;
	}
	
	public int getAlcance(){
		return this.alcance;
	}
	
	public abstract int calcularDano(Personagem pA, Personagem pB);
	
	public abstract boolean isEquipavel(Profissao p);
}

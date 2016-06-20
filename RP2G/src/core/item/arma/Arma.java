package core.item.arma;

import java.util.Arrays;
import java.util.List;

import core.item.Item;
import core.personagem.Personagem;
import core.personagem.Profissao;
import exception.NomeRepetidoException;

public abstract class Arma extends Item {
	
	private static final long serialVersionUID = -8971519113924851432L;
	
	private int danoBase;
	private int alcance;
	private final List<Profissao> equipantes;
	
	public Arma(String nome, int danoBase, int alcance, Profissao... e) throws NomeRepetidoException {
		super(nome);
		this.danoBase = danoBase;
		this.alcance = alcance;
		this.equipantes = Arrays.asList(e);
	}	 
	
	public int getDanoBase(){
		return this.danoBase;
	}
	
	public int getAlcance(){
		return this.alcance;
	}
	
	public boolean isEquipavel(Profissao p) {
		return this.equipantes.contains(p);
	}
	
	public boolean usar(Personagem p) {
		return false;
	}
	
	public abstract int calcularDano(Personagem pA, Personagem pB);
	
}

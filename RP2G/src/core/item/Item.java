package core.item;

import core.personagem.Profissao;

public abstract class Item {

	private final String nome;
	
	public Item(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public abstract boolean isEquipavel(Profissao p);
}

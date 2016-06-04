package core.item;

import core.personagem.Personagem;

public abstract class ItemUsavel extends Item {
	
	public ItemUsavel(String nome) {
		super(nome);
	}
	
	public abstract void usar(Personagem p);

}

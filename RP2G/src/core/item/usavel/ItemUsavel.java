package core.item.usavel;

import core.item.Item;
import core.personagem.Personagem;
import core.personagem.Profissao;

public abstract class ItemUsavel extends Item {
	
	public ItemUsavel(String nome) {
		super(nome);
	}
	
	public boolean isEquipavel(Profissao p) {
		return false;
	}
	
	public abstract void usar(Personagem p);

}
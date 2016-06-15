package core.item.usavel;

import core.item.Item;
import core.personagem.Personagem;
import core.personagem.Profissao;
import exception.NomeRepetidoException;

public abstract class ItemUsavel extends Item {
	
	public ItemUsavel(String nome) throws NomeRepetidoException {
		super(nome);
	}
	
	public boolean isEquipavel(Profissao p) {
		return false;
	}
	
	public abstract void usar(Personagem p);

}
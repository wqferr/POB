package core.item.usavel;

import core.item.Item;
import core.personagem.Personagem;
import exception.NomeRepetidoException;

public abstract class ItemUsavel extends Item {
	
	public ItemUsavel(String nome) throws NomeRepetidoException {
		super(nome);
	}
	
	public abstract void usar(Personagem p);

}
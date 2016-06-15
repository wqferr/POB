package core.item.usavel;

import core.personagem.Personagem;
import exception.NomeRepetidoException;


public class Pocao extends ItemUsavel {
	
	private int eficacia;

	public Pocao(String nome, int eficacia) throws NomeRepetidoException {
		super(nome);
		this.eficacia = eficacia;
	}
	
	@Override
	public void usar(Personagem p) {
		p.curar(this.eficacia);
	}

}

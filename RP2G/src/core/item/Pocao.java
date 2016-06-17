package core.item;

import core.personagem.Personagem;
import core.personagem.Personagem.Stat;
import exception.NomeRepetidoException;


public class Pocao extends Item {
	
	private int eficacia;

	public Pocao(String nome, int eficacia) throws NomeRepetidoException {
		super(nome);
		this.eficacia = eficacia;
	}
	
	@Override
	public boolean usar(Personagem p) {
		if (p.getHp() == p.getStat(Stat.HP_MAX))
			return false;
        p.curar(this.eficacia);
        return true;
	}

}

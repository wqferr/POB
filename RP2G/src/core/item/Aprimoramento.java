package core.item;

import core.personagem.Personagem;
import core.personagem.Personagem.Stat;
import exception.NomeRepetidoException;

public class Aprimoramento extends Item {
	
	private final int bonusFor;
	private final int bonusDex;
	private final int bonusInt;
	
	public Aprimoramento(String nome, int bf, int bd, int bi) throws NomeRepetidoException {
		super(nome);
		this.bonusFor = bf;
		this.bonusDex = bd;
		this.bonusInt = bi;
	}

	@Override
	public boolean usar(Personagem p) {
		p.setStat(Stat.FOR, p.getStat(Stat.FOR) + this.bonusFor);
		p.setStat(Stat.DEX, p.getStat(Stat.DEX) + this.bonusDex);
		p.setStat(Stat.INT, p.getStat(Stat.INT) + this.bonusInt);
		return true;
	}

}

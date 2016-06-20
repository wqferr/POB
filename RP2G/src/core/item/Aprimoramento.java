package core.item;

import core.personagem.Personagem;
import core.personagem.Personagem.Stat;
import exception.NomeRepetidoException;

public class Aprimoramento extends Item {
	
	private static final long serialVersionUID = -1448175719260056216L;
	
	private final int bonusFor;
	private final int bonusDex;
	private final int bonusInt;
	private final int bonusVel;
	
	public Aprimoramento(String nome, int bf, int bd, int bi, int bv) throws NomeRepetidoException {
		super(nome);
		this.bonusFor = bf;
		this.bonusDex = bd;
		this.bonusInt = bi;
		this.bonusVel = bv;
	}

	@Override
	public boolean usar(Personagem p) {
		p.setStat(Stat.FOR, p.getStat(Stat.FOR) + this.bonusFor);
		p.setStat(Stat.DEX, p.getStat(Stat.DEX) + this.bonusDex);
		p.setStat(Stat.INT, p.getStat(Stat.INT) + this.bonusInt);
		p.setStat(Stat.VEL, p.getStat(Stat.VEL) + this.bonusVel);
		return true;
	}

}

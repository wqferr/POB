package core.item.arma;

import core.personagem.Personagem;
import core.personagem.Personagem.Stat;
import core.personagem.Profissao;
import exception.NomeRepetidoException;

public class Arco extends Arma {

	public Arco (String nome, int danoBase, int alcance) throws NomeRepetidoException {
		super(nome, danoBase, alcance, Profissao.ARQUEIRO);
	}

	@Override
	public int calcularDano(Personagem pA, Personagem pB) {
		double dif = ((pA.getStat(Stat.FOR)/2.0 + pA.getStat(Stat.DEX)) - (pB.getStat(Stat.FOR)/2.0 + pB.getStat(Stat.DEX)))/5.0;
		if (dif>=0) return (int)Math.ceil(this.getDanoBase() * (dif+1));
		else return (int)Math.ceil(this.getDanoBase() /(-(dif-1))); 
	}

	@Override
	public boolean isEquipavel(Profissao p) {
		return (p == Profissao.ARQUEIRO);
	}

}

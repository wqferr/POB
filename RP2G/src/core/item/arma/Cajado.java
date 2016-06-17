package core.item.arma;

import core.personagem.Personagem;
import core.personagem.Personagem.Stat;
import core.personagem.Profissao;
import exception.NomeRepetidoException;

public class Cajado extends Arma {

	public Cajado(String nome, int danoBase, int alcance) throws NomeRepetidoException {
		super(nome, danoBase, alcance);
	}

	@Override
	public int calcularDano(Personagem pA, Personagem pB) {
		double dif = (pA.getStat(Stat.INT) +(pB.getStat(Stat.FOR)/2.0))/5.0;
		return (int)Math.ceil(this.getDanoBase() * (dif+1));
	}

	@Override
	public boolean isEquipavel(Profissao p) {
		return (p == Profissao.SACERDOTE);
	}

}

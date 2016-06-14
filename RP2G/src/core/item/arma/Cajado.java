package core.item.arma;

import core.personagem.Personagem;
import core.personagem.Profissao;
import core.personagem.Personagem.Stat;
import core.utils.Dado;

public class Cajado extends Arma {

	public Cajado(String nome, int danoBase, int alcance){
		super(nome, danoBase, alcance);
	}

	@Override
	public int calcularDano(Personagem pA, Personagem pB) {
		//(((ForcaA - ForcaB) / 5) * danoBase) + danoBase;
		return (((pA.getStat(Stat.INT) + pB.getStat(Stat.FOR))/5)+1) * this.danoBase;
	}

	@Override
	public boolean isEquipavel(Profissao p) {
		return (p == Profissao.SACERDOTE);
	}

}

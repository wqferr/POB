package core.item.arma;

import core.personagem.Personagem;
import core.personagem.Personagem.Stat;
import core.personagem.Profissao;
import exception.NomeRepetidoException;

public class Livro extends Arma {

	public Livro(String nome, int danoBase, int alcance) throws NomeRepetidoException {
		super(nome, danoBase, alcance);
	}

	@Override
	public int calcularDano(Personagem pA, Personagem pB) {
		double dif = (pA.getStat(Stat.INT) - pB.getStat(Stat.INT))/5.0;
		if (dif>=0) return (int)Math.ceil(this.danoBase * (dif+1));
		else return (int)Math.ceil(this.danoBase /(-(dif-1))); 
	}

	@Override
	public boolean isEquipavel(Profissao p) {
		return (p == Profissao.MAGO);
	}

}

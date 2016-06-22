package core.item.arma;

import core.personagem.Personagem;
import core.personagem.Personagem.Stat;
import core.personagem.Profissao;
import exception.NomeRepetidoException;
/**
 * Uma arma de longo alcance utilizada por arqueiros
 */
public class Arco extends Arma {

	private static final long serialVersionUID = -4413534022371181430L;

	/**
	 * Cria um arco
	 * @param nome Nome do arco
	 * @param danoBase Dano base do arco
	 * @param alcance Alcançe máximo da flecha
	 * @throws NomeRepetidoException Se já existe um item com esse mesmo nome
	 */
	public Arco (String nome, int danoBase, int alcance) throws NomeRepetidoException {
		super(nome, danoBase, alcance, Profissao.ARQUEIRO);
	}

	/**
	 * Calcula o dano causado pelo item do personagem A no personagem B
	 * @param pA Personagem atacante
	 * @param pB Personagem atacado
	 * @return O dano causado pelo ataque
	 */
	@Override
	public int calcularDano(Personagem pA, Personagem pB) {
		double dif = ((pA.getStat(Stat.FOR)/2.0 + pA.getStat(Stat.DEX)) - (pB.getStat(Stat.FOR)/2.0 + pB.getStat(Stat.DEX)))/5.0;
		if (dif>=0) return (int)Math.ceil(this.getDanoBase() * (dif+1));
		else return (int)Math.ceil(this.getDanoBase() /(-(dif-1))); 
	}

}

package core.item.arma;

import core.personagem.Personagem;
import core.personagem.Personagem.Stat;
import core.personagem.Profissao;
import exception.NomeRepetidoException;

/**
 * Item usado por sacerdote.
 */
public class Cajado extends Arma {

	private static final long serialVersionUID = -943910627499639832L;

	/**
	 * Cria um cajado.
	 * @param nome Nome do cajado
	 * @param danoBase Dano base do item
	 * @param alcance Alcance máximo do item
	 * @throws NomeRepetidoException Se já há um item com esse mesmo nome
	 */
	public Cajado(String nome, int danoBase, int alcance) throws NomeRepetidoException {
		super(nome, danoBase, alcance, Profissao.SACERDOTE);
	}

	/**
	 * Calcula o dano causado pelo ataque.
	 * @param pA Personagem atacante
	 * @param pB Personagem alvo
	 * @return O dano causado pelo ataque
	 */
	@Override
	public int calcularDano(Personagem pA, Personagem pB) {
		double dif = (pA.getStat(Stat.INT) +(pB.getStat(Stat.FOR)/2.0))/5.0;
		return (int)Math.ceil(this.getDanoBase() * (dif+1));
	}

}

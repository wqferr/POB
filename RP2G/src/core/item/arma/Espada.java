package core.item.arma;

import core.personagem.Personagem;
import core.personagem.Personagem.Stat;
import core.personagem.Profissao;
import exception.NomeRepetidoException;

/**
 * Arma utilizada por guerreiros.
 */
public class Espada extends Arma {
		
	private static final long serialVersionUID = 5447637813030788558L;

	/**
	 * Cria uma espada.
	 * @param nome Nome da espada
	 * @param danoBase Dano base da espada
	 * @param alcance Alcance máximo do ataque
	 * @throws NomeRepetidoException Caso o nome já exista
	 */
	public Espada(String nome, int danoBase, int alcance) throws NomeRepetidoException {
		super(nome, danoBase, alcance, Profissao.GUERREIRO);
	}
	
	/**
	 * Calcula o dano causado por um ataque de espada.
	 * @param pA Personagem atacante
	 * @param pB Personagem alvo
	 */
	@Override
	public int calcularDano(Personagem pA, Personagem pB) {
		//ForcaA > ForcaB -> (((ForcaA - ForcaB) / 5) * danoBase) + danoBase;
		//else -> -danoBase * (1/(ForcaB - ForcaA - 1))
		double dif = (pA.getStat(Stat.FOR) - pB.getStat(Stat.FOR))/5.0;
		if (dif>=0) return (int)Math.ceil(this.getDanoBase() * (dif+1));
		else return (int)Math.ceil(this.getDanoBase() /(-(dif-1))); 
	}

}

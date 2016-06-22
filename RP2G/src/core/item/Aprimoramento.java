package core.item;

import core.personagem.Personagem;
import core.personagem.Personagem.Stat;
import exception.NomeRepetidoException;
/**
 * Um item que quando utilizado aprimora os Atributos do personagem 
 */
public class Aprimoramento extends Item {
	
	private static final long serialVersionUID = -1448175719260056216L;
	
	private final int bonusFor;
	private final int bonusDex;
	private final int bonusInt;
	private final int bonusVel;
	/**
	 * Cria um Aprimoramento com os Nome e Bônus recebidos
	 * @param nome Nome do Aprimoramento
	 * @param bf Bõnus de Força
	 * @param bd Bônus de Destreza
	 * @param bi Bônus de Inteligência
	 * @param bv Bônus de Velocidade
	 * @throws NomeRepetidoException Se já houver um aprimoramento com o mesmo nome
	 */
	public Aprimoramento(String nome, int bf, int bd, int bi, int bv) throws NomeRepetidoException {
		super(nome);
		this.bonusFor = bf;
		this.bonusDex = bd;
		this.bonusInt = bi;
		this.bonusVel = bv;
	}
	@Override
	/**
	 *Aprimora os Atributos do personagem
	 */
	public boolean usar(Personagem p) {
		p.setStat(Stat.FOR, p.getStat(Stat.FOR) + this.bonusFor);
		p.setStat(Stat.DEX, p.getStat(Stat.DEX) + this.bonusDex);
		p.setStat(Stat.INT, p.getStat(Stat.INT) + this.bonusInt);
		p.setStat(Stat.VEL, p.getStat(Stat.VEL) + this.bonusVel);
		return true;
	}

}

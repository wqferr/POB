package core.item;

import core.personagem.Personagem;
import core.personagem.Personagem.Stat;
import exception.NomeRepetidoException;

/**
 * Representa um item do tipo Pocao, usado para curar HP(Hit Points(Pontos de vida(Vida do personagem(Quanto dano o personagem suporta antes de morrer))))
 *
 */
public class Pocao extends Item {
	
	private int eficacia;
	/**
	 * Cria uma poção que cura HP conforme a eficacia recebida
	 * @param nome Nome da poção
	 * @param eficacia Quanto a poção cura
	 * @throws NomeRepetidoException se Outro item tem o mesmo nome
	 */
	public Pocao(String nome, int eficacia) throws NomeRepetidoException {
		super(nome);
		this.eficacia = eficacia;
	}
	
	@Override
	/**
	 * Usa a poção no personagem
	 */
	public boolean usar(Personagem p) {
		if (p.getHp() == p.getStat(Stat.HP_MAX))
			return false;
        p.curar(this.eficacia);
        return true;
	}

}

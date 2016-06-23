package core.item;

import core.personagem.Personagem;
import core.personagem.Personagem.Stat;
import exception.NomeRepetidoException;

/**
 * Representa um item do tipo Pocao, usado para curar HP.
 */
public class Pocao extends Item {
	
	private static final long serialVersionUID = 4612980588432163127L;
	
	private int eficacia;
	
	/**
	 * Cria uma poção que cura HP conforme a eficacia recebida.
	 * @param nome Nome da poção
	 * @param eficacia Quanto a poção cura
	 * @throws NomeRepetidoException se Outro item tem o mesmo nome
	 */
	public Pocao(String nome, int eficacia) throws NomeRepetidoException {
		super(nome);
		this.eficacia = eficacia;
	}
	
	/**
	 * Usa a poção no personagem.
	 */
	@Override
	public boolean usar(Personagem p) {
		if (p.getHp() == p.getStat(Stat.HP_MAX))
			return false;
        p.curar(this.eficacia);
        return true;
	}

}

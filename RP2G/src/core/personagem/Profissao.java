package core.personagem;

/**
 * Profissões ou "classes" que um personagem pode ter.
 *
 * @author William Ferreira
 */
public enum Profissao {
	/**
	 * Dependem de {@link Personagem.Stat#FOR} para dano. Usam espadas.
	 */
	GUERREIRO,
	
	/**
	 * Dependem de {@link Personagem.Stat#INT} para dano. Usam livros.
	 */
	MAGO,
	
	/**
	 * Usam {@link Personagem.Stat#DEX} para dano. Usam arcos.
	 */
	ARQUEIRO,
	
	/**
	 * Usam {@link Personagem.Stat#INT} para curar aliados. Usam cajados.
	 */
	SACERDOTE;
}

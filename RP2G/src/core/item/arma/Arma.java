package core.item.arma;

import java.util.Arrays;
import java.util.List;

import core.item.Item;
import core.personagem.Personagem;
import core.personagem.Profissao;
import exception.NomeRepetidoException;
/**
 * Representa uma Arma que pode ser usado por um personagem para causar dano a seus oponentes, no máximo um por turno.
 */
public abstract class Arma extends Item {
	
	private static final long serialVersionUID = -8971519113924851432L;
	
	private int danoBase;
	private int alcance;
	private final List<Profissao> equipantes;
	
	/**
	 * Cria uma nova arma.
	 * @param nome Nome da arma
	 * @param danoBase Dano de Base da arma
	 * @param alcance Alcance máximo da arma, maior distância que é possível atacar um oponente
	 * @param e Profissões que podem utilizar esta arma
	 * @throws NomeRepetidoException Se já existe outro item com mesmo nome 
	 */
	public Arma(String nome, int danoBase, int alcance, Profissao... e) throws NomeRepetidoException {
		super(nome);
		this.danoBase = danoBase;
		this.alcance = alcance;
		this.equipantes = Arrays.asList(e);
	}	 
	
	/**
	 * Retorna o dano base da arma.
	 * @return Dano base da arma
	 */
	public int getDanoBase(){
		return this.danoBase;
	}
	
	/**
	 * Retorna o alcance da arma.
	 * @return alcance da arma 
	 */
	public int getAlcance(){
		return this.alcance;
	}
	
	/**
	 * Retorna se arma é ou não equipável pelo personagem recebido.
	 * @param p Personagem para verificar a equipabilidade da arma
	 * @return se a arma é equipável pelo personagem
	 */
	public boolean isEquipavel(Profissao p) {
		return this.equipantes.contains(p);
	}
	
	/**
	 * Implementação obrigatória da função abstrata.
	 */
	public boolean usar(Personagem p) {
		return false;
	}
	
	/**
	 * Metódo abstrato para ser implementado por cada tipo de arma.
	 * @param pA Personagem atacante
	 * @param pB Personagem atacado
	 * @return Se foi possível realizar o ataque
	 */
	public abstract int calcularDano(Personagem pA, Personagem pB);
	
}

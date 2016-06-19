package core.personagem;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import core.item.Item;
import core.item.arma.Arma;
import core.mapa.Posicao;
import core.utils.Dado;
import exception.ItemInexistenteException;
import exception.ItemInvalidoException;
import exception.ItensInsuficientesException;
import exception.NomeRepetidoException;
import exception.PersonagemInexistenteException;

/**
 * @author William Quelho Ferreira
 *
 * Unidade básica do jogo.
 * Uma das "peças" controladas por algum jogador.
 */
public class Personagem implements Serializable {
	/**
	 * @author William Quelho Ferreira
	 *
	 * Atributos que definem o dano, HP máximo e poder de cura dos personagens,
	 * dependendo da profissão.
	 */
	public static enum Stat {
		/**
		 * O maior valor de HP que o personagem pode atingir.
		 */
		HP_MAX,
		
		/**
		 * A força do personagem.
		 */
		FOR,
		
		/**
		 * A inteligência do personagem.
		 */
		INT,
		
		/**
		 * A dextreza do personagem.
		 */
		DEX,
		
		/**
		 * A velocidade do personagem.
		 * Determina quantas unidades por turno o personagem pode mover.
		 */
		VEL;
	}
	
	private String nome;
	// TODO icone
	private Profissao profissao;
	private Posicao pos;
	private int hp;
	private Map<Stat, Integer> stats;
	private Map<Item, Integer> inventario;
	private Arma arma;
	private static final long serialVersionUID = 587923567816495L;
	private static final Map<String, Personagem> registro = new TreeMap<>();

	
	/**
	 * Constrói um guerreiro padrão.
	 * Vide próximo construtor.
	 * 
	 * @param nome O nome do personagem.
	 */
	public Personagem(String nome) throws NomeRepetidoException{
		this(nome, Profissao.GUERREIRO);
	}
	
	/**
	 * Constrói um personagem padrão.
	 * Vide próximo construtor.
	 * 
	 * @param nome O nome do personagem.
	 * @param prof A profissão do personagem.
	 */
	public Personagem(String nome, Profissao prof) throws NomeRepetidoException{
		this(nome, prof, 10, 3);
	}
	
	/**
	 * Constrói um personagem padrão.
	 * 
	 * @param nome O nome do personagem.
	 * @param prof A profissão do personagem.
	 * @param maxHp O HP máximo do personagem.
	 * @param vel A velocidade do personagem.
	 */

	public Personagem(String nome, Profissao prof, int maxHp, int vel) throws NomeRepetidoException{
		this(nome, prof, maxHp, vel, 5, 5, 5);
	}
	
	/**
	 * Constrói um personagem padrão.
	 * 
	 * @param nome O nome do personagem.
	 * @param prof A profissão do personagem.
	 * @param maxHp O HP máximo do personagem.
	 * @param vel A velocidade do personagem.
	 * @param ist A força inicial do personagem.
	 * @param iint A inteligência inicial do personagem.
	 * @param idex A dextreza inicial do personagem.
	 */
	public Personagem(String nome, Profissao prof, int maxHp, int vel, int ist, int iint, int idex) throws NomeRepetidoException{
		this(nome, prof, maxHp, vel, ist, iint, idex, null);
	}
	
	/**
	 * Constrói um personagem padrão.
	 * 
	 * @param nome O nome do personagem.
	 * @param prof A profissão do personagem.
	 * @param maxHp O HP máximo do personagem.
	 * @param vel A velocidade do personagem.
	 * @param ist A força inicial do personagem.
	 * @param iint A inteligência inicial do personagem.
	 * @param idex A dextreza inicial do personagem.
	 * @param icone O ícone usado para representar o personagem na interface gráfica.
	 */
	public Personagem(String nome, Profissao prof, int maxHp, int vel, int ist, int iint, int idex, BufferedImage icone) throws NomeRepetidoException {
		this.nome = nome;
		this.profissao = prof;
		this.hp = maxHp;
		this.stats = new TreeMap<Stat, Integer>();
		this.inventario = new TreeMap<Item, Integer>();
		this.setStat(Stat.HP_MAX, maxHp);
		this.setStat(Stat.VEL, vel);
		this.setStat(Stat.FOR, ist);
		this.setStat(Stat.INT, iint);
		this.setStat(Stat.DEX, idex);
		Personagem.add(this);
		// TODO ícone
	}
	
	
	/**
	 * Atualiza o valor de um {@link Personagem.Stat} do personagem.
	 * @param s O atributo a ser atualizado.
	 * @param valor O novo valor do atributo.
	 */
	public void setStat(Stat s, int valor) {
		this.stats.put(s, valor);
	}
	
	/**
	 * Retorna o valor de um {@link Personagem.Stat} do personagem.
	 * @param s O atributo a ser buscado.
	 * @return O valor do atributo.
	 */
	public int getStat(Stat s) {
		return this.stats.get(s);
	}
	
	/**
	 * Retorna a quantidade de HP atual do personagem.
	 * @return O HP atual do personagem.
	 */
	public int getHp() {
		return this.hp;
	}
	
	/**
	 * Atualiza o HP do personagem.
	 * @param hp O novo valor de HP do personagem.
	 * @throws IllegalArgumentException Se o novo valor de HP for negativo ou acima do máximo.
	 */
	public void setHp(int hp) {
		if (hp > this.getStat(Stat.HP_MAX))
			throw new IllegalArgumentException("Valor de HP acima do máximo");
		if (hp < 0)
			throw new IllegalArgumentException("Valor de HP negativo");
		
		this.hp = hp;
	}
	
	/**
	 * Retorna quantos itens de um determinado tipo o personagem possui.
	 * @param i O item a ser verificado.
	 * @return A quantidade de itens.
	 */
	public int getNroItens(Item i) {
		return this.inventario.getOrDefault(i, 0);
	}
	
	/**
	 * Retorna quantos itens de um determinado tipo o personagem possui.
	 * @param s O nome do item a ser verificado.
	 * @return A quantidade de itens.
	 * 
	 * @throws ItemInexistenteException Se não existir item com esse nome.
	 */
	public int getNroItens(String s) {
		return this.getNroItens(Item.get(s));
	}
	
	/**
	 * Retorna o nome do personagem.
	 * @return O nome do personagem.
	 */
	public String getNome() {
		return this.nome;
	}
	
	/**
	 * Retorna a profissão do personagem.
	 * @return A profissão do personagem.
	 */
	public Profissao getProfissao() {
		return this.profissao;
	}
	
	/**
	 * Atualiza a posição do personagem.
	 * @param p A nova posição do personagem.
	 */
	public void mover(Posicao p) {
		this.pos = p;
	}
	
	/**
	 * Retorna a posição do personagem.
	 * @return A posição do personagem.
	 */
	public Posicao getPosicao() {
		return this.pos;
	}
	
	/**
	 * Reduz o HP do personagem em até {@code d} unidades.
	 * Se {@code this.getHP() < d}, então o valor atribuído ao HP
	 * é 0.
	 * 
	 * @param d O dano a ser causado.
	 */
	public void ferir(int d) {
		if (this.hp < d)
			this.setHp(0);
		else
            this.setHp(this.hp - d);
	}
	
	/**
	 * Aumenta o HP do personagem em até {@code c} unidades.
	 * Se {@code this.getHP() + c > this.getStat(Stat.HP_MAX)}, então
	 * o valor atribuído ao HP é o HP máximo do personagem.
	 * 
	 * @param c A cura a ser feita.
	 */
	public void curar(int c) {
		if (this.getStat(Stat.HP_MAX) - this.hp < c)
			this.setHp(this.getStat(Stat.HP_MAX));
		else
			this.setHp(this.hp + c);
	}
	
	/**
	 * Adiciona uma unidade do item ao inventário do personagem.
	 * @param i O item a ser adicionado.
	 */
	public void adicionar(Item i) {
		this.adicionar(i, 1);
	}
	
	/**
	 * Adiciona {@code q} unidades do item ao inventário do personagem.
	 * @param i O item a ser adicionado.
	 * @param q A quantidade a ser adicionada.
	 */
	public void adicionar(Item i, int q) {
		this.inventario.put(i, this.getNroItens(i) + q);
	}
	
	/**
	 * Adiciona uma unidade do item ao inventário do personagem.
	 * @param s O nome do item a ser adicionado.
	 * 
	 * @throws ItemInexistenteException Se não existir item com esse nome.
	 */
	public void adicionar(String s) {
		this.adicionar(Item.get(s));
	}
	
	/**
	 * Adiciona {@code q} unidades do item ao inventário do personagem.
	 * @param s O nome do item a ser adicionado.
	 * 
	 * @throws ItemInexistenteException Se não existir item com esse nome.
	 */
	public void adicionar(String s, int q) {
		this.adicionar(Item.get(s), q);
	}
	
	/**
	 * Remove uma unidade do item do inventário do personagem.
	 * @param i O item a ser removido.
	 * 
	 * @throws ItensInsuficientesException Se o personagem não possuir esse item.
	 */
	public void remover(Item i) {
		this.remover(i, 1);
	}
	
	/**
	 * Remove {@code q} unidades do item do inventário do personagem.
	 * @param i O item a ser removido.
	 * @param q A quantidade a ser subtraída.
	 * 
	 * @throws ItensInsuficientesException Se o jogador não possuir itens suficientes.
	 */
	public void remover(Item i, int q) {
		int qtdAtual = this.getNroItens(i);
		if (qtdAtual < q)
			throw new ItensInsuficientesException(i.getNome());
		else
            this.inventario.put(i, qtdAtual - q);
	}
	
	/**
	 * Remove uma unidade do item do inventário do personagem.
	 * @param s O nome do item a ser removido.
	 * 
	 * @throws ItensInsuficientesException Se o personagem não possuir esse item.
	 * @throws ItemInexistenteException Se não existir item com esse nome.
	 */
	public void remover(String s) {
		this.remover(Item.get(s));
	}
	
	/**
	 * Remove {@code q} unidades do item do inventário do personagem.
	 * @param s O nome do item a ser removido.
	 * @param q A quantidade a ser subtraída.
	 * 
	 * @throws ItensInsuficientesException Se o jogador não possuir itens suficientes.
	 * @throws ItemInexistenteException Se não existir item com esse nome.
	 */
	public void remover(String s, int q) {
		this.remover(Item.get(s), q);
	}
	
	/**
	 * Ataca o outro personagem usando a arma atualmente equipada.
	 * @param p O alvo do ataque.
	 * @throws NullPointerException se o personagem não possuir arma equipada.
	 */
	public void atacar(Personagem p) {
		Dado d20 = new Dado(20);
		d20.rolar();
		
		//Funcao linear onde f(1) = 1/3 e f(20) = 3
		double bonus = (((3.0 - (1.0/3.0)) / 19.0) * d20.getLado()) + ((1.0/3.0) - ((3.0 - (1.0/3.0)) / 19.0));
		int dano = (int) Math.ceil(this.arma.calcularDano(this, p) * bonus);
		p.ferir(dano);
	}
	
	/**
	 * Muda a arma equipada pelo personagem.
	 * Remove a arma nova do inventário do personagem e, se a personagem
	 * tinha alguma arma já equipada, devolve-a ao inventário.
	 * 
	 * @param arma A nova arma do personagem.
	 * @throws ItensInsuficientesException Se {@code arma != null && this.getNroItens(arma) == 0}.
	 * @throws ItemInvalidoException Se a arma não for equipável pela classe do personagem.
	 */
	public void setArma(Arma arma) throws ItemInvalidoException {
		Arma anterior = this.arma;

		if (arma == null) {
			this.arma = null;
        } else if (arma.isEquipavel(this.profissao)) {
            this.remover(arma);
            this.arma = arma;
		} else {
			throw new ItemInvalidoException(arma.getNome());
		}
		
        this.adicionar(anterior);
	}
	
	/**
	 * Muda a arma equipada pelo personagem.
	 * Remove a arma nova do inventário do personagem e, se a personagem
	 * tinha alguma arma já equipada, devolve-a ao inventário.
	 * 
	 * @param nome O nome da nova arma do personagem.
	 * @throws ItemInexistenteException Se não existir item com esse nome.
	 * @throws ItensInsuficientesException Se {@code arma != null && this.getNroItens(arma) == 0}.
	 * @throws ItemInvalidoException Se a arma não for equipável pela classe do personagem, ou se o item especificado pelo nome não for uma arma.
	 */
	public void setArma(String nome) throws ItemInvalidoException {
		try {
			this.setArma((Arma) Item.get(nome));
		} catch (ClassCastException e) {
			throw new ItemInvalidoException(nome + " não é uma arma");
		}
	}
	
	/**
	 * Retorna a arma atualmente equipada.
	 * @return A arma equipada.
	 */
	public Arma getArma() {
		return this.arma;
	}
	
	/**
	 * Tenta usar o item como este personagem.
	 * O item é removido do inventário do personagem após usá-lo com sucesso.
	 * @param item O item a ser usado.
	 * @return Se foi possível usar o item.
	 */
	public boolean usar(Item item) {
        if (item.usar(this)) {
            this.remover(item);
            return true;
        }
        return false;
	}
	
	/**
	 * Tenta usar o item como este personagem.
	 * O item é removido do inventário do personagem após usá-lo.
	 * @param nome O nome do item a ser usado.
	 * @return Se foi possivel usar o item.
	 * @throws ItemInexistenteException Se não existir item com esse nome.
	 * @throws ItensInsuficientesException Se o personagem não possuir esse item.
	 * @throws ItemInvalidoException Se o item especificado pelo nome não for usável.
	 */
	public boolean usar(String nome) {
        return this.usar(Item.get(nome));
	}
	
	public static Personagem get(String nome) {
		Personagem p = Personagem.registro.get(nome);
		if (p == null) throw new PersonagemInexistenteException();
		
		return p;
	}
	
	public static void add(Personagem pers) throws NomeRepetidoException {
		if (Personagem.registro.putIfAbsent(pers.getNome(), pers) != null)
			throw new NomeRepetidoException(pers.getNome());
	}
	
	public static Iterator<Entry<String, Personagem>> getIterator(){
		return Personagem.registro.entrySet().iterator();
	}
}

package core.personagem;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.TreeMap;

import core.item.Item;
import core.item.arma.Arma;
import core.item.usavel.ItemUsavel;
import core.mapa.Posicao;
import core.utils.Dado;
import exception.ItemInvalidoException;
import exception.ItensInsuficientesException;

public class Personagem {
	public static enum Stat {
		HP_MAX,
		FOR,
		INT,
		DEX;
	}
	
	private String nome;
	// TODO icone
	private Profissao profissao;
	private Posicao pos;
	private int hp;
	private Map<Stat, Integer> stats;
	private Map<Item, Integer> inventario;
	private Arma arma;

	
	public Personagem(String nome) {
		this(nome, Profissao.GUERREIRO);
	}
	
	public Personagem(String nome, Profissao prof) {
		this(nome, prof, 10);
	}
	
	public Personagem(String nome, Profissao prof, int maxHp) {
		this(nome, prof, maxHp, 5, 5, 5);
	}
	
	public Personagem(String nome, Profissao prof, int maxHp, int ist, int iint, int idex) {
		this(nome, prof, maxHp, ist, iint, idex, null);
	}
	
	public Personagem(String nome, Profissao prof, int maxHp, int ist, int iint, int idex, BufferedImage icone) {
		this.nome = nome;
		this.profissao = prof;
		this.hp = maxHp;
		this.stats = new TreeMap<Stat, Integer>();
		this.inventario = new TreeMap<Item, Integer>();
		this.setStat(Stat.HP_MAX, maxHp);
		this.setStat(Stat.FOR, ist);
		this.setStat(Stat.INT, iint);
		this.setStat(Stat.DEX, idex);
		// TODO ícone
	}
	
	
	public void setStat(Stat s, int valor) {
		this.stats.put(s, valor);
	}
	
	public int getStat(Stat s) {
		return this.stats.get(s);
	}
	
	public int getHp() {
		return this.hp;
	}
	
	public void setHp(int hp) {
		if (hp > this.getStat(Stat.HP_MAX))
			throw new IllegalArgumentException("Valor de HP acima do máximo");
		if (hp < 0)
			throw new IllegalArgumentException("Valor de HP negativo");
		
		this.hp = hp;
	}
	
	public int getNroItens(Item i) {
		return this.inventario.getOrDefault(i, 0);
	}
	
	public int getNroItens(String s) {
		return this.getNroItens(Item.get(s));
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public Profissao getProfissao() {
		return this.profissao;
	}
	
	public void mover(Posicao p) {
		this.pos = p;
	}
	
	public Posicao getPosicao() {
		return this.pos;
	}
	
	public void ferir(int d) {
		if (this.hp < d)
			this.setHp(0);
		else
            this.setHp(this.hp - d);
	}
	
	public void curar(int c) {
		if (this.getStat(Stat.HP_MAX) - this.hp < c)
			this.setHp(this.getStat(Stat.HP_MAX));
		else
			this.setHp(this.hp + c);
	}
	
	public void adicionar(Item i) {
		this.adicionar(i, 1);
	}
	
	public void adicionar(Item i, int q) {
		this.inventario.put(i, this.getNroItens(i) + q);
	}
	
	public void adicionar(String s) {
		this.adicionar(Item.get(s));
	}
	
	public void adicionar(String s, int q) {
		this.adicionar(Item.get(s), q);
	}
	
	public void remover(Item i) {
		this.remover(i, 1);
	}
	
	public void remover(Item i, int q) {
		int qtdAtual = this.getNroItens(i);
		if (qtdAtual < q)
			throw new ItensInsuficientesException(i.getNome());
		else
            this.inventario.put(i, qtdAtual - q);
	}
	
	public void remover(String s) {
		this.remover(Item.get(s));
	}
	
	public void remover(String s, int q) {
		this.remover(Item.get(s), q);
	}
	
	public void atacar(Personagem pB) {
		Dado d20 = new Dado(20);
		d20.rolar();
		
		//Funcao linear onde f(1) = 1/3 e f(20) = 3
		double bonus = (((3.0 - (1.0/3.0)) / 19.0) * d20.getLado()) + ((1.0/3.0) - ((3.0 - (1.0/3.0)) / 19.0));
		int dano = (int) Math.ceil(this.arma.calcularDano(this, pB) * bonus);
		pB.ferir(dano);
	}
	
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
	
	public Arma getArma() {
		return this.arma;
	}
	
	public void usar(ItemUsavel item) {
		item.usar(this);
	}
	
}

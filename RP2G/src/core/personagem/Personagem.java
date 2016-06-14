package core.personagem;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.TreeMap;

import core.mapa.Posicao;
import core.utils.Dado;
import core.item.Item;
import exception.NotEnoughManaException;
import core.item.arma.Arma;

public class Personagem {
	public static enum Stat {
		HP_MAX,
		MP_MAX,
		FOR,
		INT,
		DEX;
	}
	
	private String nome;
	// TODO icone
	private Profissao profissao;
	private Posicao pos;
	private int hp;
	private int mp;
	private Map<Stat, Integer> stats;
	private Map<Item, Integer> inventario;
	private Arma arma;

	
	public Personagem(String nome) {
		this(nome, Profissao.GUERREIRO);
	}
	
	public Personagem(String nome, Profissao prof) {
		this(nome, prof, 10, 10);
	}
	
	public Personagem(String nome, Profissao prof, int maxHp, int maxMp) {
		this(nome, prof, maxHp, maxMp, 5, 5, 5);
	}
	
	public Personagem(String nome, Profissao prof, int maxHp, int maxMp, int ist, int iint, int idex) {
		this(nome, prof, maxHp, maxMp, ist, iint, idex, null);
	}
	
	public Personagem(String nome, Profissao prof, int maxHp, int maxMp, int ist, int iint, int idex, BufferedImage icone) {
		this.nome = nome;
		this.profissao = prof;
		this.hp = maxHp;
		this.mp = maxMp;
		this.stats = new TreeMap<Stat, Integer>();
		this.inventario = new TreeMap<Item, Integer>(
			(a, b) -> {
				return a.getNome().compareTo(b.getNome());
			}
		);
		this.setStat(Stat.HP_MAX, maxHp);
		this.setStat(Stat.MP_MAX, maxMp);
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
	
	public void setMp(int mp) {
		if (mp > this.getStat(Stat.MP_MAX))
			throw new IllegalArgumentException("Valor de MP acima do máximo");
		if (mp < 0)
			throw new IllegalArgumentException("Valor de MP negativo");
		
		this.mp = mp;
	}
	
	public int getNroItens(Item i) {
		return this.inventario.getOrDefault(i, 0);
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
	
	public void danificar(int d) {
		if (this.hp > d)
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
	
	public void gastar(int m) throws NotEnoughManaException {
		if (this.mp >= m)
			this.setMp(this.mp - m);
		else
			throw new NotEnoughManaException();
	}
	
	public void recuperar(int m) {
		if (this.getStat(Stat.MP_MAX) - this.mp < m)
			this.setMp(this.getStat(Stat.MP_MAX));
		else
			this.setMp(this.mp + m);
	}
	
	public void dar(Item i) {
		this.dar(i, 1);
	}
	
	public void dar(Item i, int q) {
		this.inventario.put(i, this.getNroItens(i) + q);
	}
	
	public void atacar(Personagem pB) {
		Dado d20 = new Dado(20);
		d20.rolar();
		
		//Funcao linear onde f(1) = 1/3 e f(20) = 3
		double bonus = (((3.0 - (1.0/3.0)) / 19.0) * d20.getLado()) + ((1.0/3.0) - ((3.0 - (1.0/3.0)) / 19.0));
		int dano = (int) Math.ceil(this.arma.calcularDano(this, pB) * bonus);
		pB.danificar(dano);
	}
	
	public void setArma(){
		
	}
	
	public Arma getArma(){
		
	}
	
}

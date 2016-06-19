package utils;

public class Dado {
	private int lados;
	private int ladoAtual;
	private Random gerador;
	
	/**
	 * Gerador padrao para um dado de 6 lados
	 */
	public Dado() {
		this(6);
	}
	
	/**
	 * Gerador com lados especificados
	 * @param newLados
	 */
	public Dado(int lados) {
		this.lados = lados;
		this.gerador = new Random();
		this.ladoAtual = this.rolar();
	}
	
	/**
	 * Retorna uma nova rolagem
	 * @return
	 */
	public int rolar(){
		this.ladoAtual = (int) this.gerador.getRand(1, this.lados);
		return this.ladoAtual;
	}
	
	/**
	 * Retorna o ultimo resultado
	 * @return
	 */
	public int getLado(){
		return this.ladoAtual;
	}
	
}
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
	
	public Dado(int lados, long s) {
		this.lados = lados;
		this.gerador = new Random(s);
		this.ladoAtual = this.rolar();
	}
	
	public void setSeed(int s) {
		this.gerador.setSeed(s);
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
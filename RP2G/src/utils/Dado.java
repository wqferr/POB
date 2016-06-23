package utils;

public class Dado {
	private int lados;
	private int ladoAtual;
	private Random gerador;
	
	/**
	 * Gerador padrão para um dado de 6 lados
	 */
	public Dado() {
		this(6);
	}
	
	/**
	 * Gerador com lados especificados
	 * @param lados Número de lados do dado a ser criado.
	 */
	public Dado(int lados) {
		this.lados = lados;
		this.gerador = new Random();
		this.ladoAtual = this.rolar();
	}

	/**
	 * Cria um dado com o número de lados especificados e usa a seed especifidada para o gerador de números aleatórios 
	 * @param lados Número de lados do dado
	 * @param s Seed do gerador de números aleatórios.
	 */
	public Dado(int lados, long s) {
		this.lados = lados;
		this.gerador = new Random(s);
		this.ladoAtual = this.rolar();
	}
	
	/**
	 * Altera a seed do gerador de números aleatórios
	 * @param s Seed para o gerador de números aleatórios.
	 */
	public void setSeed(long s) {
		this.gerador.setSeed(s);
	}
	
	/**
	 * Retorna uma nova rolagem
	 * @return A face sorteada no lançamento do dado.
	 */
	public int rolar() {
		this.ladoAtual = (int) this.gerador.getRand(1, this.lados);
		return this.ladoAtual;
	}
	
	/**
	 * Retorna o ultimo resultado
	 * @return O valor sorteado no último lançamento.
	 */
	public int getLado() {
		return this.ladoAtual;
	}
	
}

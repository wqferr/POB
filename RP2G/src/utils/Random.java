package utils;

public class Random {
	private long p;
	private long m;
	private long a;
	private long xi;
	
	/**
	 * Construtor com seed padrão
	 */
	public Random(){
		this(System.nanoTime());
	}
	
	/**
	 * Construtor com seed específica
	 * @param seed Seed para ser utilizada na geração dos números aleatórios.
	 */
	public Random(long seed){
		this(seed, 2147483648l, 843314861, 453816693);
	}
	
	/**
	 * Construtor com seed, P, M e A especificados
	 * @param seed Seed para ser utilizada na geração dos números aleatórios.
	 * @param p Coeficiente utilizado pela equação de geração de números aleatórios.
	 * @param m Coeficiente utilizado pela equação de geração de números aleatórios.
	 * @param a Coeficiente utilizado pela equação de geração de números aleatórios.
	 */
	public Random(long seed, long p, long m, long a){
		this.xi = (seed % p);
		this.p = p;
		this.m = m;
		this.a = a;
	}
	
	/**
	 * Define a seed para o gerador
	 * @param seed Seed para ser utilizada na geração dos números aleatórios.
	 */
	public void setSeed(long seed){
		this.xi = (seed % p);
	}
	
	/**
	 * Calcula o próximo número da sequência
	 */
	private void nextRand(){
		this.xi = (a + m*xi) % p;
	}
	
	/**
	 * Gera um número aleatório entre [O, P) e retorna-o
	 * @return O número gerado.
	 */
	public long getRand(){
		return this.getRand(0, this.p-1);
	}
	
	/**
	 * Gera um número aleatório entre [0, max] e retorna-o
	 * @param max Valor máximo que pode ser gerado.
	 * @return O número gerado.
	 */
	public long getRand(long max){
		return this.getRand(0, max);
	}
	
	/**
	 * Gera um número aleatório entre [min, max] e retorna-o
	 * @param min Valor mínimo que pode ser gerado.
	 * @param max Valor máximo que pode ser gerado.
	 * @return O número gerado.
	 */
	public long getRand(long min, long max){
		this.nextRand();
		return (xi % (max-min+1)) + min;
	}
	
	/**
	 * Gera um número real aleatório entre [0,1] e retorna-o
	 * Entre [0, 1]
	 * @return O número aleatório gerado
	 */
	public double getDoubleRand(){
		return this.getRand()/this.p;
	}
	
}

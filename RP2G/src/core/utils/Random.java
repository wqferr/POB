package core.utils;

public class Random {
	private long p;
	private long m;
	private long a;
	private long xi;
	
	/**
	 * Construtor com seed padrao
	 */
	public Random(){
		this(System.nanoTime());
	}
	
	/**
	 * Construtor com seed especifica
	 * @param seed
	 */
	public Random(long seed){
		this(seed, 2147483648l, 843314861, 453816693);
	}
	
	/**
	 * Construtor com seed, P, M e A especificados
	 * @param seed
	 */
	public Random(long seed, long p, long m, long a){
		this.xi = seed;
		this.p = p;
		this.m = m;
		this.a = a;
	}
	
	/**
	 * Define a seed para o gerador
	 * @param newSeed
	 */
	public void setSeed(long seed){
		this.xi = (seed % p);
	}
	
	/**
	 * Calcula o proximo numero da sequencia
	 */
	
	private void nextRand(){
		this.xi = (a + m*xi) % p;
	}
	
	/**
	 * Gera um numero aleatorio e retorna-o
	 * Entre [0, P)
	 * @return
	 */
	public long getRand(){
		return this.getRand(0, this.p-1);
	}
	
	/**
	 * Gera um numero aleatorio e retorna-o
	 * Entre [0, max]
	 * @param max
	 * @return
	 */
	public long getRand(long max){
		return this.getRand(0, max);
	}
	
	/**
	 * Gera um numero aleatorio e retorna-o
	 * Entre [min, max]
	 * @param min
	 * @param max
	 * @return
	 */
	public long getRand(long min, long max){
		this.nextRand();
		return (xi % (max-min+1)) + min;
	}
	
	/**
	 * Gera um numero real aleatorio e retorna-o
	 * Entre [0, 1]
	 * @return
	 */
	public double getDoubleRand(){
		return this.getRand()/this.p;
	}
	

}
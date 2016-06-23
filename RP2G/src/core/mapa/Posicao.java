package core.mapa;

import java.io.Serializable;
/**
 * Um par de numeros inteiros usados para representar as coordendas de um quadrado no mapa.
 */
public class Posicao implements Comparable<Posicao>, Serializable {
	
	private static final long serialVersionUID = 2379129387123897L;
	
	private final int linha;
	private final int coluna;
	/**
	 * Cria uma posição com os números i, j.
	 * @param i Linha
	 * @param j Coluna
	 */
	public Posicao(int i, int j) {
		this.linha = i;
		this.coluna = j;
	}
	/**
	 * Retorna a linha.
	 * @return linha
	 */
	public int getLinha() {
		return this.linha;
	}
	
	/**
	 * Retorna a coluna.
	 * @return coluna
	 */
	public int getColuna() {
		return this.coluna;
	}
	
	/**
	 * Retorna todos os vizinhos dessa posição.
	 * São consideradas apenas posições que compartilham uma coordenada com esta.
	 * @return Array de posições vizinhas
	 */
	public Posicao[] getVizinhos() {
		return new Posicao[] {
			new Posicao(this.linha, this.coluna+1),
			new Posicao(this.linha, this.coluna-1),
			new Posicao(this.linha+1, this.coluna),
			new Posicao(this.linha-1, this.coluna)
		};
	}
	
	/**
	 * Retorna distância entre essa posição e posição p.
	 * @param p outra Posição 
	 * @return Distância entre as posições
	 */
	public int distancia(Posicao p) {
		return Math.abs(p.linha - this.linha)
				+ Math.abs(p.coluna - this.coluna);
	}
	
	/**
	 * Compara duas posições usando, nesta ordem, a linha e a coluna como critérios.
	 */
	@Override
	public int compareTo(Posicao p) {
		int c = Integer.compare(this.linha, p.linha);
		if (c == 0)
			c = Integer.compare(this.coluna, p.coluna);
		return c;
	}
	
	@Override
	/**
	 * Compara esta posição à outra.
	 * Retorna true caso linhas da primeira seja igual a linha da segunda e coluna da primeira seja igual a coluna da segunda 
	 */
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Posicao))
			return false;
		Posicao p = (Posicao) o;
		
		return this.linha == p.linha && this.coluna == p.coluna;
	}
	
	/**
	 * Converte a posição para uma string na forma:
	 * {@code "%d %d", linha, coluna}.
	 */
	@Override
	public String toString() {
		return String.format("%d %d", this.linha, this.coluna);
	}

}

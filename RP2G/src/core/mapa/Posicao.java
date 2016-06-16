package core.mapa;

public class Posicao implements Comparable<Posicao> {
	
	private final int linha;
	private final int coluna;

	public Posicao(int i, int j) {
		this.linha = i;
		this.coluna = j;
	}
	
	public int getLinha() {
		return this.linha;
	}
	
	public int getColuna() {
		return this.coluna;
	}
	
	public Posicao[] getVisinhos() {
		return new Posicao[] {
			new Posicao(this.linha, this.coluna+1),
			new Posicao(this.linha, this.coluna-1),
			new Posicao(this.linha+1, this.coluna),
			new Posicao(this.linha-1, this.coluna)
		};
	}
	
	@Override
	public int compareTo(Posicao p) {
		int c = Integer.compare(this.linha, p.linha);
		if (c == 0)
			c = Integer.compare(this.coluna, p.coluna);
		return c;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Posicao))
			return false;
		Posicao p = (Posicao) o;
		
		return this.linha == p.linha && this.coluna == p.coluna;
	}

}

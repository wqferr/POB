package core;

public class Posicao {
	
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

}

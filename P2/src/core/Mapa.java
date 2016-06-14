package c;


public class Mapa {
	
	private Quadrado[][] topologia;
	
	
	public Mapa(boolean[][] bool){
		for(int i = 0; i < bool.length; i ++){
			for(int j = 0; j < bool[i].length; j ++){
				this.topologia[i][j] = new Quadrado(new Posicao(i, j), bool[i][j]);
			}
		}
	}
	
	public Mapa(Quadrado[][] topologia){
		this.topologia = topologia;
	}
	
	public Quadrado getQuadrado(Posicao posicao){
		return this.topologia[posicao.getLinha()][posicao.getColuna()];
	}
	
	public int getNLinhas(){
		return this.topologia.length;
	}
	public int getNColunas(){
		return this.topologia[0].length;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

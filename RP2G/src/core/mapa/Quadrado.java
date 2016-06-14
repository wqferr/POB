package core.mapa;
import core.personagem.Personagem;


public class Quadrado {

	private Posicao posicao;	
	private boolean transponivel;
	private Personagem ocupante;

	public Quadrado(Posicao posicao, boolean transponivel, Personagem ocupante){
		this.posicao = posicao;
		this.transponivel = transponivel;
		this.ocupante = ocupante;
	}
		
	public Quadrado(Posicao posicao){
		this(posicao, false);
	}
	
	public Quadrado(Posicao posicao, boolean transponivel){
		this(posicao, transponivel, null);
	}
	
	public Posicao getPosicao(){
		return this.posicao;
	} 
	public Personagem getOcupante(){
		return this.ocupante;
	}
	public boolean isTransponivel(){
		return this.transponivel;
	}
	public boolean isOcupado(){
		return this.ocupante != null;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}

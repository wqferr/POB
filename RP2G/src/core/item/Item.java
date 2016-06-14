package core.item;

public abstract class Item implements Comparable{

	private final String nome;
	
	public Item(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return this.nome;
	}
}

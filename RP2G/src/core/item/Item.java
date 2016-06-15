package core.item;

public abstract class Item implements Comparable<Item> {

	private final String nome;
	
	public Item(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	@Override
	public int compareTo(Item outro) {
		return this.nome.compareTo(outro.nome);
	}
}

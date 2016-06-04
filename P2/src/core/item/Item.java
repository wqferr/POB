package core.item;

public abstract class Item {

	private final String nome;
	
	public Item(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public abstract boolean isEquipavel();
}

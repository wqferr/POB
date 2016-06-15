package core.item;

import java.util.Map;
import java.util.TreeMap;

import exception.ItemInexistenteException;
import exception.NomeRepetidoException;

public abstract class Item implements Comparable<Item> {

	private final String nome;
	private static final Map<String, Item> registro = new TreeMap<>();
	
	public Item(String nome) throws NomeRepetidoException {
		if (registro.get(nome) != null)
			throw new NomeRepetidoException(nome);
		this.nome = nome;
		registro.put(nome, this);
	}
	
	public String getNome() {
		return this.nome;
	}
	
	@Override
	public int compareTo(Item outro) {
		return this.nome.compareTo(outro.nome);
	}
	
	public static Item get(String nome) {
		Item i = registro.get(nome);
		if (i == null)
			throw new ItemInexistenteException();
		return i;
	}
}

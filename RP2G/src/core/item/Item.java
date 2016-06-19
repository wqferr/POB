package core.item;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import core.personagem.Personagem;
import exception.ItemInexistenteException;
import exception.NomeRepetidoException;

public abstract class Item implements Comparable<Item>, Serializable {
		
	private static final long serialVersionUID = 652636748467672L;
	
	private final String nome;
	private static final Map<String, Item> registro = new TreeMap<>();
	
	public Item(String nome) throws NomeRepetidoException {
		this.nome = nome;
		Item.add(this);
	}
	
	public String getNome() {
		return this.nome;
	}
	
	@Override
	public int compareTo(Item outro) {
		return this.nome.compareTo(outro.nome);
	}
	
	public static Item get(String nome) {
		Item i = Item.registro.get(nome);
		if (i == null) throw new ItemInexistenteException();
		
		return i;
	}
	
	public static void add(Item item) throws NomeRepetidoException {
		if (Item.registro.putIfAbsent(item.getNome(), item) != null)
			throw new NomeRepetidoException(item.getNome());
	}
	
	public static Iterator<Entry<String, Item>> getIterator(){
		return Item.registro.entrySet().iterator();
	}
	
	public abstract boolean usar(Personagem p);
}

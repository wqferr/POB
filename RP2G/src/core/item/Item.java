package core.item;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import core.personagem.Personagem;
import exception.ItemInexistenteException;
import exception.NomeRepetidoException;
/**
 * Classe abstrata para representar os itens.
 */
public abstract class Item implements Comparable<Item>, Serializable {
		
	private static final long serialVersionUID = 652636748467672L;
	
	private final String nome;
	private static final Map<String, Item> registro = new TreeMap<>();
	
	/**
	 * Cria um item com o nome recebido.
	 * @param nome Nome do item
	 * @throws NomeRepetidoException Se já houver um item com o mesmo nome
	 */
	public Item(String nome) throws NomeRepetidoException {
		this.nome = nome;
		Item.add(this);
	}
	
	/**
	 * Retorna o nome do item.
	 * @return nome do item
	 */
	public String getNome() {
		return this.nome;
	}
	
	/**
	 * Retorna o item com o nome recebido.
	 * @param nome Nome do item a ser retornado
	 * @return O item com o nome desejado
	 * @throws ItemInexistenteException Se não houver um item com o nome
	 */
	public static Item get(String nome) {
		Item i = Item.registro.get(nome);
		if (i == null) throw new ItemInexistenteException();
		
		return i;
	}
	
	/**
	 * Regsitra um novo item.
	 * @param item O item a ser adicionado
	 * @throws NomeRepetidoException Se já houver um item com o mesmo nome
	 */
	public static void add(Item item) throws NomeRepetidoException {
		if (Item.registro.putIfAbsent(item.getNome(), item) != null)
			throw new NomeRepetidoException(item.getNome());
	}
	
	/**
	 * Remove um item do bando de dados
	 * @param nome
	 */
	public static void remove(String nome) {
		if (!Item.registro.containsKey(nome)) throw new ItemInexistenteException();
		Item.registro.remove(nome);
	}
	
	/**
	 * Retorna um iterador para o mapa de itens.
	 * @return O iterador
	 */
	public static Iterator<Entry<String, Item>> getIterator() {
		return Item.registro.entrySet().iterator();
	}
	
	/**
	 * Usa o item no personagem recebido.
	 * @param p Personagem
	 * @return Se foi possível usar o item
	 */
	public abstract boolean usar(Personagem p);
	
	/**
	 * Compara esse item com outro pelo nome.
	 */
	@Override
	public int compareTo(Item outro) {
		return this.nome.compareTo(outro.nome);
	}
	
	/**
	 * Compara dois itens e retorna se sao iguais.
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Item))
			return false;
		return ((Item) o).nome.equals(this.nome);
	}
}

package core.item;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import core.personagem.Personagem;
import core.personagem.Profissao;
import exception.ItemInexistenteException;
import exception.NomeRepetidoException;

public abstract class Item implements Comparable<Item> {

	private final String nome;
	private final List<Profissao> usuarias;
	private static final Map<String, Item> registro = new TreeMap<>();
	
	public Item(String nome, Profissao... usuarias) throws NomeRepetidoException {
		if (registro.get(nome) != null)
			throw new NomeRepetidoException(nome);
		this.nome = nome;
		this.usuarias = Arrays.asList(usuarias);
		registro.put(nome, this);
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public boolean isEquipavel(Profissao p) {
		return this.usuarias.contains(p);
	}
	
	public boolean usar(Personagem p) {
		return false;
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

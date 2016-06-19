package core;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import struct.ListaCircular;
import core.item.Item;
import core.mapa.Mapa;
import core.mapa.Posicao;
import core.personagem.Personagem;

public class Jogo {
	
	private Mapa mapa;
	private ListaCircular<Personagem> personagens;
	private Personagem pAtual;
	private ListIterator<Personagem> pIter;

	public Jogo(Mapa m) {
		this.mapa = m;
		List<Personagem> p = m.getPersonagens();
		Collections.shuffle(p);
		this.personagens = new ListaCircular<>(p);
		this.pIter = this.personagens.listIterator();
		this.pAtual = pIter.next();
	}
	
	public Personagem proximoPersonagem() {
		pAtual = pIter.next();
		return pAtual;
	}
	
	public Personagem personagemAtual() {
		return pAtual;
	}
	
	public boolean mover(Posicao nova) {
		return false;
	}
	
	public boolean atacar(Posicao alvo) {
		return false;
	}
	
	public boolean usar(Item item) {
		return false;
	}
	
	public boolean usar(String item) {
		return false;
	}

}

package core;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import struct.ListaCircular;
import core.item.Item;
import core.mapa.Mapa;
import core.mapa.Posicao;
import core.personagem.Personagem;
import core.personagem.Personagem.Stat;

public class Jogo implements Serializable {
	
	private static final long serialVersionUID = -4082863311966571070L;
	
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
		this.pAtual = pIter.next();
		return this.pAtual;
	}
	
	public Personagem personagemAtual() {
		return this.pAtual;
	}
	
	public boolean mover(Posicao nova) {
		if (this.mapa.alcancavel(this.pAtual.getPosicao(), nova, this.pAtual.getStat(Stat.VEL))) {
			this.mapa.mover(this.pAtual.getPosicao(), nova);
			return true;
		}
		return false;
	}
	
	public boolean atacar(Posicao alvo) {
		if (!this.mapa.isOcupado(alvo) || this.pAtual.getArma() == null)
			return false;
		
		return alvo.distancia(this.pAtual.getPosicao()) > this.pAtual.getArma().getAlcance();
	}
	
	public boolean usar(Item item) {
		return this.pAtual.usar(item);
	}
	
	public boolean usar(String item) {
		return this.pAtual.usar(item);
	}

}

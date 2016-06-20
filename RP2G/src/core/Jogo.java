package core;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import struct.ListaCircular;
import core.item.Item;
import core.mapa.Mapa;
import core.mapa.Posicao;
import core.personagem.Personagem;
import core.personagem.Personagem.Stat;
/**
 * 
 * @author guilherme
 *
 */
public class Jogo {
	private Mapa mapa;
	private boolean proximoTime;
	private ListaCircular<Personagem> personagens1;
	private ListaCircular<Personagem> personagens2;
	private Personagem pAtual;
	private ListIterator<Personagem> pIter1;
	private ListIterator<Personagem> pIter2;
	/**
	 * 
	 * @param m
	 * @param p1
	 * @param p2
	 */
	public Jogo(Mapa m, List<Personagem> p1, List<Personagem> p2) {
		this.mapa = m;
		Collections.shuffle(p1);
		Collections.shuffle(p2);

		initPos(m.getSpawnPointsTime1(), p1);
		initPos(m.getSpawnPointsTime2(), p2);
		this.personagens1 = new ListaCircular<>(p1);
		this.personagens2 = new ListaCircular<>(p2);
		this.pIter1 = this.personagens1.listIterator();
		this.pIter2 = this.personagens2.listIterator();
		this.pAtual = pIter1.next();
		this.proximoTime =  true;
	}
	
	private static void initPos(List<Posicao> spawn, List<Personagem> per) {
		if (spawn.size() != per.size())
			throw new IllegalArgumentException("Lista de spawns e personagens com tamanho diferente");
		ListIterator<Posicao> posIter = spawn.listIterator();
		
		for (Personagem p : per)
			p.mover(posIter.next());
	}
	/**
	 * 
	 * @return
	 */
	public Personagem proximoPersonagem() {
		proximoTime = !proximoTime;
		if(proximoTime)
			this.pAtual = pIter1.next();
		else
			this.pAtual = pIter2.next();
		return this.pAtual;
	}
	/**
	 * 
	 * @return
	 */
	public Personagem personagemAtual() {
		return this.pAtual;
	}
	/**
	 * 
	 * @param nova
	 * @return
	 */
	public boolean mover(Posicao nova) {
		if (this.mapa.alcancavel(this.pAtual.getPosicao(), nova, this.pAtual.getStat(Stat.VEL))) {
			this.mapa.mover(this.pAtual.getPosicao(), nova);
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param alvo
	 * @return
	 */
	public boolean atacar(Posicao alvo) {
		if (!this.mapa.isOcupado(alvo) || this.pAtual.getArma() == null)
			return false;
		
		if(alvo.distancia(this.pAtual.getPosicao()) <= this.pAtual.getArma().getAlcance()){
			Personagem p = this.mapa.getQuadrado(alvo).getOcupante();
			this.pAtual.atacar(p);
			if(p.isMorto())
				this.removePersonagem(p);
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param item
	 * @return
	 */
	public boolean usar(Item item) {
		return this.pAtual.usar(item);
	}
	/**
	 * 
	 * @param item
	 * @return
	 */
	public boolean usar(String item) {
		return this.pAtual.usar(item);
	}
	/**
	 * 
	 * @return
	 */
	public boolean acabou(){
		return personagens1.isEmpty() || personagens2.isEmpty();
	}

	/*
	 * 0 Ninguem 
	 * 1 Time 1
	 * 2 Time 2
	*/
	/**
	 * 
	 * @return
	 */
	public int vencedor(){
		if(personagens1.isEmpty())
			return 2;
		if(personagens2.isEmpty())
			return 1;
		return 0;
	}
	/**
	 * 
	 * @param p
	 */
	private void removePersonagem(Personagem p){
		if (!personagens1.remove(p))
			personagens2.remove(p);
	}
	
	public Mapa getMapa() {
		return this.mapa;
	}

}

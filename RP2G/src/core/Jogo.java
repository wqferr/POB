package core;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;

import struct.ListaCircular;
import core.item.Item;
import core.mapa.Mapa;
import core.mapa.Posicao;
import core.mapa.Quadrado;
import core.personagem.Personagem;
import core.personagem.Personagem.Stat;

/**
 * Contem as mecânicas básicas para o jogo 
 * @author
 *
 */
public class Jogo implements Serializable {
	
	private static final long serialVersionUID = -4082863311966571070L;
	
	public static final int NRO_JOGADORES = 2;
	
	private Mapa mapa;
	private boolean proximoTime;
	private ListaCircular<Personagem> personagens1;
	private ListaCircular<Personagem> personagens2;
	private Personagem pAtual;
	private ListIterator<Personagem> pIter1;
	private ListIterator<Personagem> pIter2;
	private Consumer<Void> ouvinte;
	
	private boolean andou;
	private boolean atacou;
	
	private static final Consumer<Void> OUVINTE_DEFAULT = (Void v) -> {};
	/**
	 * Constrói um novo jogo com o mapa e os times recebidos
	 * @param m Mapa do jogo
	 * @param p1 Lista de personagens do time um 
	 * @param p2 Lista de personagens do time dois
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
		this.andou = false;
		this.setOuvinte(null);
	}
	
	public void setOuvinte(Consumer<Void> c) {
		this.ouvinte = c == null ? OUVINTE_DEFAULT : c;
	}
	
	/**
	 * Inicializa o mapa, colocando os personagens da lista recebida, nas posições recebidas pela lista de Posições de Spawn	 
	 * * @param spawn Lista com as posições iniciais para os personagens
	 * @param per Lista com os personagens
	 */
	private void initPos(List<Posicao> spawn, List<Personagem> per) {
		if (spawn.size() != per.size())
			throw new IllegalArgumentException("Lista de spawns e personagens com tamanho diferente");
		ListIterator<Posicao> posIter = spawn.listIterator();
		
		for (Personagem p : per)
			this.mapa.getQuadrado(posIter.next()).setOcupante(p);
	}
	/**
	 * Retorna o personagem que pode agir no próimo turno
	 * @return Personagem que pode agir no próximo turno 
	 */
	public Personagem proximoPersonagem() {
		this.ouvinte.accept(null);
		proximoTime = !proximoTime;
		if(proximoTime)
			this.pAtual = pIter1.next();
		else
			this.pAtual = pIter2.next();
		this.ouvinte.accept(null);
		this.atacou = false;
		this.andou = false;
		return this.pAtual;
	}
	/**
	 * Retorna o personagem que pode agir no turno atual
	 * @return Personagem que pode agir no turno atual
	 */
	public Personagem personagemAtual() {
		return this.pAtual;
	}
	
	public boolean podeMover(Posicao nova) {
		return this.mapa.alcancavel(this.pAtual.getPosicao(), nova, this.pAtual.getStat(Stat.VEL));
	}
	
	/**
	 * Função para retornar 
	 * @param nova Posição para qual o personagem deve ser movido
	 * @return se foi possível mover o personagem para a posição desejada
	 */
	public boolean mover(Posicao nova) {
		if (this.andou)
			return false;
		if (this.mapa.isOcupado(nova))
			return false;
		if (this.mapa.alcancavel(this.pAtual.getPosicao(), nova, this.pAtual.getStat(Stat.VEL))) {
			this.ouvinte.accept(null);
			this.mapa.mover(this.pAtual.getPosicao(), nova);
			this.andou = true;
            this.ouvinte.accept(null);
			return true;
		}
		return false;
	}
	
	public boolean podeAtacar(Posicao alvo) {
		if (!this.mapa.isOcupado(alvo) || this.pAtual.getArma() == null)
			return false;
		return alvo.distancia(this.pAtual.getPosicao()) <= this.pAtual.getArma().getAlcance();
	}
	
	/**
	 * Ataca, caso houver, o personagem que está na posição alvo
	 * @param alvo Posição que deve ser atacada
	 * @return se foi possível atacar
	 */
	public boolean atacar(Posicao alvo) {
		if (this.atacou)
			return false;
		
		if (this.pAtual == this.mapa.getQuadrado(alvo).getOcupante())
			return false;
		
		if (!this.mapa.isOcupado(alvo) || this.pAtual.getArma() == null)
			return false;
		
		if(alvo.distancia(this.pAtual.getPosicao()) <= this.pAtual.getArma().getAlcance()){
			this.ouvinte.accept(null);
			Personagem p = this.mapa.getQuadrado(alvo).getOcupante();
			this.pAtual.atacar(p);
			if(p.isMorto()) {
				System.out.println("Morreu");
				this.mapa.setOcupante(p.getPosicao(), null);
				this.removePersonagem(p);
			}
			
			this.atacou = true;
            this.ouvinte.accept(null);
			return true;
		}
		return false;
	}

	public boolean podeUsar(String item) {
		return this.pAtual.podeUsar(item);
	}

	public boolean podeUsar(Item item) {
		return this.pAtual.podeUsar(item);
	}
	
	/**
	 * Usa o item recebido
	 * @param item Item que deve ser usado
	 * @return Se foi possível usar o item
	 */
	public boolean usar(Item item) {
        this.ouvinte.accept(null);
		if (this.pAtual.usar(item)) {
            this.ouvinte.accept(null);
            return true;
		}
		return false;
	}
	/**
	 * Usa o item com o nome recebido
	 * @param item String com nome do item que deve ser usado
	 * @return Se foi possível usar o item
	 */
	public boolean usar(String item) {
		return this.pAtual.usar(item);
	}
	/**
	 * Retorna se o jogo acabou ou não
	 * @return Se o jogo acabou
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
	 * Retorna qual o time vencedor ou 0 caso nenhum tenha ganho
	 * @return Time vencedor: 0 Nenhum, 1 Time 1, 2 Time 2
	 */
	public int vencedor(){
		if(personagens1.isEmpty())
			return 2;
		if(personagens2.isEmpty())
			return 1;
		return 0;
	}
	/**
	 * Remove um personagem do jogo
	 * @param p Personagem que deve ser removido
	 */
	private void removePersonagem(Personagem p){
		if (!personagens1.remove(p))
			personagens2.remove(p);
	}
	/**
	 *Retorna o mapa do jogo
	 * @return Mapa
	 */
	public Mapa getMapa() {
		return this.mapa;
	}
	
	public List<Personagem> getPersonagensTimeAtual() {
		return new LinkedList<>(this.proximoTime ? this.personagens2 : this.personagens1);
	}
	
	public List<Personagem> getPersonagensTime1() {
		return new LinkedList<>(this.personagens1);
	}

	public List<Personagem> getPersonagensTime2() {
		return new LinkedList<>(this.personagens2);
	}
	
	public boolean executar(Ordem o) {
		switch (o.getComando()) {
            case ATACAR:
            	return this.atacar((Posicao) o.getArg());
            	
            case MOVER:
                return this.mover((Posicao) o.getArg());
            
            case USAR:
            	return this.usar((String) o.getArg());
            
            case ENCERRAR:
            	if(!this.acabou()){
            		this.proximoPersonagem();
            		return true;
            	}
            	else return false;

                
			default:
				return false;
		}
	}
	
	public void exibir(){
		this.exibir(System.out::print);
	}
	
	public void exibir(Consumer<? super String> printer) {
		printer.accept("  ");
		for (int i = 0; i < this.mapa.getNColunas(); i++)
			printer.accept((i % 10) + " ");
		printer.accept("\n");
		
		for (int i = 0; i < this.mapa.getNLinhas(); i++) {
			printer.accept((i % 10) + " ");
			for (int j = 0; j < this.mapa.getNColunas(); j++) {
				Posicao pos = new Posicao(i, j);
				Quadrado q = this.mapa.getQuadrado(pos);
				
                Personagem p = q.getOcupante();
                if (p == null) {
                    if (q.isTransponivel()) {
                        printer.accept(" ");
                    } else {
                        printer.accept("X");
                    }
                } else {
                    char c = p.getNome().charAt(0);
                    if (this.getPersonagensTimeAtual().contains(p))
                        c = Character.toUpperCase(c);
                    else
                        c = Character.toLowerCase(c);
                    printer.accept(String.valueOf(c));
                }
				printer.accept(" ");
			}
			printer.accept("\n");
		}
	}
	
}

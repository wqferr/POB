package core;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.function.Consumer;

import utils.struct.ListaCircular;
import core.item.Item;
import core.mapa.Mapa;
import core.mapa.Posicao;
import core.mapa.Quadrado;
import core.personagem.Personagem;
import core.personagem.Personagem.Stat;
import exception.ItensInsuficientesException;

/**
 * Contem as mecânicas básicas para o jogo 
 */
public class Jogo implements Serializable {
	
	private static final long serialVersionUID = -4082863311966571070L;
	
	public static final int NRO_JOGADORES = 2;
	
	private Mapa mapa;
	private boolean timeAtual;
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

		init(m.getSpawnPointsTime1(), p1, 1);
		init(m.getSpawnPointsTime2(), p2, 2);
		this.personagens1 = new ListaCircular<>(p1);
		this.personagens2 = new ListaCircular<>(p2);
		this.pIter1 = this.personagens1.listIterator();
		this.pIter2 = this.personagens2.listIterator();
		this.pAtual = pIter1.next();
		this.timeAtual =  false;
		this.andou = false;
		this.setOuvinte(null);
	}
	
	/**
	 * Cosntrutor basico apenas com mapa
	 * @param m
	 */
	public Jogo(Mapa m) {
		List<Personagem> p1 = new LinkedList<>();
		List<Personagem> p2 = new LinkedList<>();
		List<Personagem> todos = new LinkedList<>();
		
		Iterator<Entry<String, Personagem>> iter = Personagem.getIterator();
		while (iter.hasNext())
			todos.add(iter.next().getValue());
		
		ListIterator<Personagem> li = todos.listIterator();
		
		int nSpawns1 = m.getSpawnPointsTime1().size();
		int nSpawns2 = m.getSpawnPointsTime2().size();
		if (todos.size() >= nSpawns1 + nSpawns2) {
			for (int i = 0; i < nSpawns1; i++)
				p1.add(li.next());
			for (int i = 0; i < nSpawns2; i++)
				p2.add(li.next());
		} else {
			int i = 0, j = 0;
			
			while (i + j < todos.size()) {
				if (i < nSpawns1) {
					i++;
					p1.add(li.next());
				}
				if (j < nSpawns2) {
					j++;
					p2.add(li.next());
				}
			}
		}
		
		this.mapa = m;
		init(m.getSpawnPointsTime1(), p1, 1);
		init(m.getSpawnPointsTime2(), p2, 2);
		this.personagens1 = new ListaCircular<>(p1);
		this.personagens2 = new ListaCircular<>(p2);
		this.pIter1 = this.personagens1.listIterator();
		this.pIter2 = this.personagens2.listIterator();
		this.pAtual = pIter1.next();
		this.timeAtual =  false;
		this.andou = false;
		this.setOuvinte(null);
	}
	
	/**
	 * Define o ouvinte do Jogo
	 * @param c
	 */
	public void setOuvinte(Consumer<Void> c) {
		this.ouvinte = c == null ? OUVINTE_DEFAULT : c;
	}
	
	/**
	 * Inicializa o mapa, colocando os personagens da lista recebida, nas posições recebidas pela lista de Posições de Spawn	 
	 * * @param spawn Lista com as posições iniciais para os personagens
	 * @param per Lista com os personagens
	 */
	private void init(List<Posicao> spawn, List<Personagem> per, int time) {
		if (spawn.size() < per.size())
			throw new IllegalArgumentException("Spawns insuficientes");
		ListIterator<Posicao> posIter = spawn.listIterator();
		
		for (Personagem p : per) {
			this.mapa.getQuadrado(posIter.next()).setOcupante(p);
			p.setTime(time);
		}
	}
	
	/**
	 * Retorna o time atual
	 * @return
	 */
	public int getTimeAtual() {
		return this.timeAtual ? 2 : 1;
	}
	
	/**
	 * Retorna o personagem que pode agir no próimo turno
	 * @return Personagem que pode agir no próximo turno 
	 */
	public Personagem proximoPersonagem() {
		this.ouvinte.accept(null);
		timeAtual = !timeAtual;
		if(timeAtual)
			this.pAtual = pIter2.next();
		else
			this.pAtual = pIter1.next();
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
	
	/**
	 * Retorna se o personagem atual pode se mover até a posição dada
	 * @param nova
	 * @return
	 */
	public boolean podeMover(Posicao nova) {
		return this.mapa.alcancavel(this.pAtual.getPosicao(), nova, this.pAtual.getStat(Stat.VEL));
	}
	
	/**
	 * Função para retornar 
	 * @param nova Posição para qual o personagem deve ser movido
	 * @return se foi possível mover o personagem para a posição desejada
	 */
	public boolean mover(Posicao nova) {
		if (this.andou || !this.mapa.contem(nova))
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
	
	/**
	 * Verifica se o personagem atual pode atacar.
	 * @param alvo A posição alvo
	 * @return se o personagem pode atacar.
	 */
	public boolean podeAtacar(Posicao alvo) {
		if (!this.mapa.contem(alvo) || !this.mapa.isOcupado(alvo) || this.pAtual.getArma() == null)
			return false;
		return alvo.distancia(this.pAtual.getPosicao()) <= this.pAtual.getArma().getAlcance();
	}
	
	/**
	 * Ataca, caso houver, o personagem que está na posição alvo
	 * @param alvo Posição que deve ser atacada
	 * @return se foi possível atacar
	 */
	public boolean atacar(Posicao alvo) {
		if (this.atacou || !this.mapa.contem(alvo))
			return false;
		
		if (this.pAtual == this.mapa.getQuadrado(alvo).getOcupante())
			return false;
		
		if (!this.mapa.isOcupado(alvo) || this.pAtual.getArma() == null)
			return false;
		
		if(alvo.distancia(this.pAtual.getPosicao()) <= this.pAtual.getArma().getAlcance()) {
			this.ouvinte.accept(null);
			Personagem p = this.mapa.getQuadrado(alvo).getOcupante();
			this.pAtual.atacar(p);
			if(p.isMorto()) {
				this.mapa.setOcupante(p.getPosicao(), null);
				this.removePersonagem(p);
			}
			
			this.atacou = true;
            this.ouvinte.accept(null);
			return true;
		}
		return false;
	}

	/**
	 * Verifica se o personagem atual pode usar um item.
	 * @param item O item a ser usado
	 * @return Se o personagem pode usar o item
	 */
	public boolean podeUsar(String item) {
		return this.pAtual.podeUsar(item);
	}

	/**
	 * Verifica se o personagem atual pode usar um item.
	 * @param item O item a ser usado
	 * @return Se o personagem pode usar o item
	 */
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
		try {
            this.ouvinte.accept(null);
            if (this.pAtual.usar(item)) {
                this.ouvinte.accept(null);
                return true;
            }
            return false;
		} catch (ItensInsuficientesException e) {
			return false;
		}
	}
	/**
	 * Retorna se o jogo acabou ou não
	 * @return Se o jogo acabou
	 */
	public boolean acabou() {
		return personagens1.isEmpty() || personagens2.isEmpty();
	}

	/**
	 * Retorna qual o time vencedor ou 0 caso nenhum tenha ganho
	 * @return Time vencedor: 0 Nenhum, 1 Time 1, 2 Time 2
	 */
	public int vencedor() {
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
	private void removePersonagem(Personagem p) {
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
	
	/**
	 * Retorna uma lista com os personagens do time atual.
	 * @return Os personagens do time.
	 */
	public List<Personagem> getPersonagensTimeAtual() {
		return new LinkedList<>(this.timeAtual ? this.personagens2 : this.personagens1);
	}
	
	/**
	 * Retorna uma lista com os personagens do time 1.
	 * @return Os personagens do time.
	 */
	public List<Personagem> getPersonagensTime1() {
		return new LinkedList<>(this.personagens1);
	}

	/**
	 * Retorna uma lista com os personagens do time 1.
	 * @return Os personagens do time.
	 */
	public List<Personagem> getPersonagensTime2() {
		return new LinkedList<>(this.personagens2);
	}
	
	/**
	 * Tenta executar uma ordem dada.
	 * @param o A ordem a ser executada
	 * @return Se foi possível fazê-lo.
	 */
	public boolean executar(Ordem o) {
		switch (o.getComando()) {
            case ATACAR:
            	return this.atacar((Posicao) o.getArg());
            	
            case MOVER:
                return this.mover((Posicao) o.getArg());
            
            case USAR:
            	return this.usar((String) o.getArg());
            
            case ENCERRAR:
            	if(!this.acabou()) {
            		this.proximoPersonagem();
            		return true;
            	}
            	else return false;

                
			default:
				return false;
		}
	}
	
	/**
	 * Exibe o jogo usando System.out.print.
	 */
	public void exibir() {
		this.exibir(System.out::print);
	}
	
	/**
	 * Exibe a matriz do jogo.
	 * @param printer O objeto a aceitar a matriz.
	 */
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
		printer.accept("Personagem atual em [");
		printer.accept(this.pAtual.getPosicao().toString());
		printer.accept("]\n\n");
	}
	
}

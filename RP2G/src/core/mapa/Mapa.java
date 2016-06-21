package core.mapa;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import struct.Par;
import utils.Pixel;
import core.personagem.Personagem;
import exception.MapaInexistenteException;
import exception.NomeRepetidoException;

/**
 *
 * @author guilherme
 *
 */
public class Mapa implements Serializable{
	private static final long serialVersionUID = 63278163812368321L;
	
	private String nome;
	private Quadrado[][] topologia;
	private List<Posicao> spawnPointsTime1;
	private List<Posicao> spawnPointsTime2;
	private static final Map<String, Mapa> registro = new TreeMap<>();
	/**
	 * 
	 * @param nome
	 * @param topologia
	 * @throws NomeRepetidoException
	 */
	public Mapa(String nome, Quadrado[][] topologia) throws NomeRepetidoException{
		this.nome = nome;
		this.topologia = topologia;
		this.spawnPointsTime1 = new LinkedList<Posicao>();
		this.spawnPointsTime2 = new LinkedList<Posicao>();
		Mapa.add(this);
	}
	/**
	 * 
	 * @param nome
	 * @param bool
	 * @throws NomeRepetidoException
	 */
	public Mapa(String nome, boolean[][] bool) throws NomeRepetidoException{
		this.nome = nome;
		if (bool.length == 0) throw new IllegalArgumentException("Mapa deve ter ao menos 1 linha");
		if (bool[0].length == 0) throw new IllegalArgumentException("Mapa deve ter ao menos 1 coluna");
		
		this.topologia = new Quadrado[bool.length][bool[0].length];
		for(int i = 0; i < bool.length; i++) {
			if (bool[i].length != bool[0].length) throw new IllegalArgumentException("Mapa não retangular");
			for(int j = 0; j < bool[i].length; j++)
				this.topologia[i][j] = new Quadrado(new Posicao(i, j), bool[i][j]);
		}
		
		this.spawnPointsTime1 = new LinkedList<Posicao>();
		this.spawnPointsTime2 = new LinkedList<Posicao>();
		Mapa.add(this);
	}
	/**
	 * 
	 * @param nome
	 * @param imagem
	 * @throws NomeRepetidoException
	 */
	public Mapa(String nome, BufferedImage imagem) throws NomeRepetidoException{
		this.nome = nome;
		this.topologia = new Quadrado[imagem.getHeight()][imagem.getWidth()];
		this.spawnPointsTime1 = new LinkedList<Posicao>();
		this.spawnPointsTime2 = new LinkedList<Posicao>();
		for (int i=0; i<imagem.getWidth(); i++){
			for (int j=0; j<imagem.getHeight(); j++){
				Pixel curPix = new Pixel(imagem.getRGB(i, j));
				this.topologia[j][i] = new Quadrado(new Posicao(j, i), !curPix.isWhite());
				if (curPix.isRed()) this.spawnPointsTime1.add(new Posicao(j, i));
				else if (curPix.isBlue()) this.spawnPointsTime2.add(new Posicao(j, i));
			}
		}
		
		Mapa.add(this);
	}
	/**
	 * 
	 * @param posicao
	 * @return
	 */
	public Quadrado getQuadrado(Posicao posicao) {
		return this.topologia[posicao.getLinha()][posicao.getColuna()];
	}
	/**
	 * 
	 * @return
	 */
	public int getNLinhas() {
		return this.topologia.length;
	}
	/**
	 * 
	 * @return
	 */
	public int getNColunas() {
		return this.topologia[0].length;
	}
	/**
	 * 
	 * @return
	 */
	public String getNome(){
		return this.nome;
	}
	/**
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public int distancia(Posicao p1, Posicao p2) {
		Queue<Par<Posicao, Integer>> proximas = new LinkedList<>();
		Set<Posicao> visitados = new TreeSet<>();
		proximas.add(new Par<>(p1, 0));
		visitados.add(p1);
		
		while (!proximas.isEmpty()) {
			Par<Posicao, Integer> par = proximas.remove();
			Posicao p = par.getV1();
			int d = par.getV2();
			if (p.equals(p2))
				return d;
			
			visitados.add(p);
			for (Posicao v : p.getVizinhos())
				if (this.contem(v) && !visitados.contains(v) && this.getQuadrado(v).isTransponivel())
                    proximas.add(new Par<>(v, d+1));
		}
		
		return -1;
	}
	/**
	 * 
	 * @param p
	 * @return
	 */
	public boolean contem(Posicao p) {
		return p.getLinha() >= 0 && p.getLinha() < this.getNLinhas()
				&& p.getColuna() >= 0 && p.getColuna() < this.getNColunas();
	}
	/**
	 * 
	 * @param origem
	 * @param destino
	 * @return
	 */
	public boolean alcancavel(Posicao origem, Posicao destino) {
		return this.distancia(origem, destino) >= 0;
	}
	/**
	 * 
	 * @param origem
	 * @param destino
	 * @param max
	 * @return
	 */
	public boolean alcancavel(Posicao origem, Posicao destino, int max) {
		int d = this.distancia(origem, destino);
		return 0 <= d && d <= max;
	}
	/**
	 * 
	 * @param origem
	 * @param destino
	 */
	public void mover(Posicao origem, Posicao destino) {
		Quadrado qOrig = this.getQuadrado(origem),
				 qDest = this.getQuadrado(destino);
		qDest.setOcupante(qOrig.getOcupante());
		qOrig.setOcupante(null);
	}
	/**
	 * 
	 * @param p
	 * @return
	 */
	public boolean isOcupado(Posicao p) {
		return this.getQuadrado(p).isOcupado();
	}
	/**
	 * 
	 * @param pos
	 * @param p
	 */
	public void setOcupante(Posicao pos, Personagem p) {
		this.getQuadrado(pos).setOcupante(p);
	}
	/**
	 * 
	 * @return
	 */
	public List<Personagem> getPersonagensTime1() {
		List<Personagem> l = new LinkedList<>();
		for (Posicao p : this.getSpawnPointsTime1()) {
			Quadrado q = this.getQuadrado(p);
				if (q.isOcupado())
					l.add(q.getOcupante());
		}
		return l;
	}
	/**
	 * 
	 * @return
	 */
	public List<Personagem> getPersonagensTime2() {
		List<Personagem> l = new LinkedList<>();
		for (Posicao p : this.getSpawnPointsTime2()) {
			Quadrado q = this.getQuadrado(p);
				if (q.isOcupado())
					l.add(q.getOcupante());
		}
		return l;
	}
	/**
	 * 
	 * @return
	 */
	public List<Posicao> getSpawnPointsTime1(){
		return this.spawnPointsTime1;
	}
	/**
	 * 
	 * @return
	 */
	public List<Posicao> getSpawnPointsTime2(){
		return this.spawnPointsTime2;
	}
	/**
	 * 
	 * @param nome
	 * @return
	 */
	public static Mapa get(String nome) {
		Mapa m = Mapa.registro.get(nome);
		if (m == null) throw new MapaInexistenteException();
		
		return m;
	}
	/**
	 * 
	 * @param mapa
	 * @throws NomeRepetidoException
	 */
	public static void add(Mapa mapa) throws NomeRepetidoException {
		if (Mapa.registro.putIfAbsent(mapa.getNome(), mapa) != null)
			throw new NomeRepetidoException(mapa.getNome());
	}
	/**
	 * 
	 * @return
	 */
	public static Iterator<Entry<String, Mapa>> getIterator(){
		return Mapa.registro.entrySet().iterator();
	}

}

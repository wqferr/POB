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


public class Mapa implements Serializable {
	
	private static final long serialVersionUID = 2102495729801838463L;
	
	private String nome;
	private Quadrado[][] topologia;
	private List<Posicao> spawnPointsTime1;
	private List<Posicao> spawnPointsTime2;
	private static final Map<String, Mapa> registro = new TreeMap<>();
	
	public Mapa(String nome, Quadrado[][] topologia) throws NomeRepetidoException{
		this.nome = nome;
		this.topologia = topologia;
		this.spawnPointsTime1 = new LinkedList<Posicao>();
		this.spawnPointsTime2 = new LinkedList<Posicao>();
		Mapa.add(this);
	}
	
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
	
	public Quadrado getQuadrado(Posicao posicao) {
		return this.topologia[posicao.getLinha()][posicao.getColuna()];
	}
	
	public int getNLinhas() {
		return this.topologia.length;
	}
	
	public int getNColunas() {
		return this.topologia[0].length;
	}
	
	public String getNome(){
		return this.nome;
	}
	
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
	
	public boolean contem(Posicao p) {
		return p.getLinha() >= 0 && p.getLinha() < this.getNLinhas()
				&& p.getColuna() >= 0 && p.getColuna() < this.getNColunas();
	}
	
	public boolean alcancavel(Posicao origem, Posicao destino) {
		return this.distancia(origem, destino) >= 0;
	}
	
	public boolean alcancavel(Posicao origem, Posicao destino, int max) {
		int d = this.distancia(origem, destino);
		return 0 <= d && d <= max;
	}
	
	public void mover(Posicao origem, Posicao destino) {
		Quadrado qOrig = this.getQuadrado(origem),
				 qDest = this.getQuadrado(destino);
		qDest.setOcupante(qOrig.getOcupante());
		qOrig.setOcupante(null);
	}
	
	public boolean isOcupado(Posicao p) {
		return this.getQuadrado(p).isOcupado();
	}
	
	public void setOcupante(Posicao pos, Personagem p) {
		this.getQuadrado(pos).setOcupante(p);
	}

	public List<Personagem> getPersonagensTime1() {
		List<Personagem> l = new LinkedList<>();
		for (Posicao p : this.getSpawnPointsTime1()) {
			Quadrado q = this.getQuadrado(p);
				if (q.isOcupado())
					l.add(q.getOcupante());
		}
		return l;
	}
	public List<Personagem> getPersonagensTime2() {
		List<Personagem> l = new LinkedList<>();
		for (Posicao p : this.getSpawnPointsTime2()) {
			Quadrado q = this.getQuadrado(p);
				if (q.isOcupado())
					l.add(q.getOcupante());
		}
		return l;
	}
	
	public List<Posicao> getSpawnPointsTime1(){
		return this.spawnPointsTime1;
	}
	
	public List<Posicao> getSpawnPointsTime2(){
		return this.spawnPointsTime2;
	}
	public static Mapa get(String nome) {
		Mapa m = Mapa.registro.get(nome);
		if (m == null) throw new MapaInexistenteException();
		
		return m;
	}
	
	public static void add(Mapa mapa) throws NomeRepetidoException {
		if (Mapa.registro.putIfAbsent(mapa.getNome(), mapa) != null)
			throw new NomeRepetidoException(mapa.getNome());
	}
	
	public static Iterator<Entry<String, Mapa>> getIterator(){
		return Mapa.registro.entrySet().iterator();
	}

}

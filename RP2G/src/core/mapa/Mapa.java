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
 * Mapa do jogo, uma matriz composta de quadrados.
 */
public class Mapa implements Serializable{
	private static final long serialVersionUID = 63278163812368321L;
	
	private String nome;
	private Quadrado[][] topologia;
	private List<Posicao> spawnPointsTime1;
	private List<Posicao> spawnPointsTime2;
	private static final Map<String, Mapa> registro = new TreeMap<>();
	/**
	 * Cria uma mapa Utilizando a matriz de Quadrados recebida
	 * @param nome Nome do mapa
	 * @param topologia Matriz de quadrados
	 */
	public Mapa(String nome, Quadrado[][] topologia) throws NomeRepetidoException{
		this.nome = nome;
		this.topologia = topologia;
		this.spawnPointsTime1 = new LinkedList<Posicao>();
		this.spawnPointsTime2 = new LinkedList<Posicao>();
		Mapa.add(this);
	}
	/**
	 * Cria um mapa de acordo com uma matriz de Boolean. Posições com True representam posições acessíveis do mapa e False posicões não acessíveis.
	 * @param nome Nome do mapa 
	 * @param bool Matriz de Boolean que representa a acessibilidade das posições do mapa
	 * @throws NomeRepetidoException Se já existe outro mapa com o mesmo nome
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
	 * Cria um mapa utilizando uma imagem composta de pixels azuis, pretos, brancos e vermelhos.
	 * brancos: Posições acessíveis
	 * pretos: Não acessíveis
	 * vermelhos: Pontos de Spawn do time 1
	 * azuis: Pontos de Spawn do time 2
	 * @param nome Nome do mapa
	 * @param imagem Imagem para criação do mapa
	 * @throws NomeRepetidoException Se já existe outro mapa com o mesmo nome
	 */
	public Mapa(String nome, BufferedImage imagem) throws NomeRepetidoException{
		this.nome = nome;
		this.topologia = new Quadrado[imagem.getHeight()][imagem.getWidth()];
		this.spawnPointsTime1 = new LinkedList<Posicao>();
		this.spawnPointsTime2 = new LinkedList<Posicao>();
		for (int i=0; i<imagem.getHeight(); i++){
			for (int j=0; j<imagem.getWidth(); j++){
				Pixel curPix = new Pixel(imagem.getRGB(j, i));
				this.topologia[i][j] = new Quadrado(new Posicao(i, j), !curPix.isWhite());
				if (curPix.isRed()) this.spawnPointsTime1.add(new Posicao(i, j));
				else if (curPix.isBlue()) this.spawnPointsTime2.add(new Posicao(i, j));
			}
		}
		
		Mapa.add(this);
	}
	/**
	 * Retorna o quadrado presente no mapa na posição recebida
	 * @param posicao Posição do quadrado
	 * @return Quadrado
	 */
	public Quadrado getQuadrado(Posicao posicao) {
		return this.topologia[posicao.getLinha()][posicao.getColuna()];
	}
	/**
	 * Retorna o numero de linhas da matriz de Quadrados
	 * @return numero de linhas
	 */
	public int getNLinhas() {
		return this.topologia.length;
	}
	/**
	 * Retorna o numero de colunas da matriz de Quadrados
	 * @return numero de colunas
	 */
	public int getNColunas() {
		return this.topologia[0].length;
	}
	/**
	 * Retorna o nome do mapa
	 * @return nome do mapa
	 */
	public String getNome(){
		return this.nome;
	}
	/**
	 * Calcula a distância entre dois pontos do mapa
	 * @param p1 ponto 1
	 * @param p2 ponto 2
	 * @return distância entre os pontos
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
	 * Verifica se essa Posição existe no mapa
	 * @param p Posição
	 * @return Se existe a posição no mapa
	 */
	public boolean contem(Posicao p) {
		return p.getLinha() >= 0 && p.getLinha() < this.getNLinhas()
				&& p.getColuna() >= 0 && p.getColuna() < this.getNColunas();
	}
	/**
	 * Verifica se a posição de destino pode ser alcancada a partir da posição de origem
	 * @param origem Posição de origem
	 * @param destino Posição de destino
	 * @return Se o destino é alcançavel a partir da origem
	 */
	public boolean alcancavel(Posicao origem, Posicao destino) {
		return this.distancia(origem, destino) >= 0;
	}
	/**
	 * Verifica se é possível alcançar o destino a partir da origem, com max distância maxima entre os pontos
	 * @param origem Ponto de origem
	 * @param destino Ponto de destino
	 * @param max Distância Máxima
	 * @return Se é ou não alcançavel.
	 */
	public boolean alcancavel(Posicao origem, Posicao destino, int max) {
		int d = this.distancia(origem, destino);
		return 0 <= d && d <= max;
	}
	/**
	 * Move o ocupante da posição de origem para a posição de destino 
	 * @param origem Posição de Origem
	 * @param destino Posição de Destino
	 */
	public void mover(Posicao origem, Posicao destino) {
		Quadrado qOrig = this.getQuadrado(origem),
				 qDest = this.getQuadrado(destino);
		Personagem p = qOrig.getOcupante();
		qOrig.setOcupante(null);
		qDest.setOcupante(p);
	}
	/**
	 * Verifica se a posição está ocupada por um personagem
	 * @param p Posição
	 * @return Se está ocupada
	 */
	public boolean isOcupado(Posicao p) {
		return this.getQuadrado(p).isOcupado();
	}
	/**
	 * Insere um personagem em uma posição
	 * @param pos Posição
	 * @param p Personagem
	 */
	public void setOcupante(Posicao pos, Personagem p) {
		this.getQuadrado(pos).setOcupante(p);
	}
	/**
	 * Retorna todos os personages do time 1
	 * @return Lista de personagens do time 1
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
	 * Retorna todos os personages do time 2
	 * @return Lista de personagens do time 2
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
	 * Retorna todas as posições de Spawn do time 1 
	 * @return Lista com as posições de Spawn do time 1
	 */
	public List<Posicao> getSpawnPointsTime1(){
		return this.spawnPointsTime1;
	}
	/**
	 * Retorna todas as posições de Spawn do time 2 
	 * @return Lista com as posições de Spawn do time 2
	 */
	public List<Posicao> getSpawnPointsTime2(){
		return this.spawnPointsTime2;
	}
	/**
	 * Retorna o mapa 
	 * @param nome Nome do mapa
	 * @return Mapa
	 * @throws MapaInexistenteExcpetion Se o Mapa for inexistente
	 */
	public static Mapa get(String nome) {
		Mapa m = Mapa.registro.get(nome);
		if (m == null) throw new MapaInexistenteException();
		
		return m;
	}
	/**
	 * Adiciona um novo mapa
	 * @param mapa Mapa
	 * @throws NomeRepetidoException Se já existe um mapa com o mesmo nome
	 */
	public static void add(Mapa mapa) throws NomeRepetidoException {
		if (Mapa.registro.putIfAbsent(mapa.getNome(), mapa) != null)
			throw new NomeRepetidoException(mapa.getNome());
	}
	/**
	 * Retorna um iterador para o registro mapas
	 * @return iterador para os mapas
	 */
	public static Iterator<Entry<String, Mapa>> getIterator(){
		return Mapa.registro.entrySet().iterator();
	}

}

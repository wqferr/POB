package core.mapa;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import struct.Par;


public class Mapa {
	
	private Quadrado[][] topologia;
	
	
	public Mapa(boolean[][] bool) {
		if (bool.length == 0)
			throw new IllegalArgumentException("Mapa deve ter ao menos 1 linha");
		if (bool[0].length == 0)
			throw new IllegalArgumentException("Mapa deve ter ao menos 1 coluna");
		
		for(int i = 0; i < bool.length; i++) {
			if (bool[i].length != bool[0].length)
				throw new IllegalArgumentException("Mapa não retangular");
			
			for(int j = 0; j < bool[i].length; j++)
				this.topologia[i][j] = new Quadrado(new Posicao(i, j), bool[i][j]);
		}
	}
	
	public Mapa(Quadrado[][] topologia) {
		this.topologia = topologia;
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

}

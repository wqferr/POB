package net.client;

import net.Mensagem;
import net.Mensagem.Evento;

public class Comando {
	
	public static enum Ordem {
		MOVER(Evento.MOVIMENTO),
		ATACAR(Evento.ATAQUE),
		ENCERRAR(Evento.FIM_TURNO);
		
		private Evento correspondente;
		
		private Ordem(Evento e) {
			this.correspondente = e;
		}
		
		public Evento getCorrespondente() {
			return this.correspondente;
		}
	}

	private Ordem o;
	private int i;
	private int j;
	
	public Comando(Ordem o) {
		this(o, 0, 0);
	}
	
	public Comando(Ordem o, int i, int j) {
		this.o = o;
		this.i = i;
		this.j = j;
	}
	
	public Ordem getOrdem() {
		return this.o;
	}
	
	public Mensagem empacotar() {
		return new Mensagem(this.o.getCorrespondente(), String.format("%d %d", this.i, this.j));
	}

}

package core;

import net.Mensagem;
import net.Mensagem.Evento;

public class Ordem {
	
	public static enum Comando {
		MOVER(Evento.MOVIMENTO),
		ATACAR(Evento.ATAQUE),
		ENCERRAR(Evento.FIM_TURNO);
		
		private Evento correspondente;
		
		private Comando(Evento e) {
			this.correspondente = e;
		}
		
		public Evento getCorrespondente() {
			return this.correspondente;
		}
	}

	private Comando c;
	private int i;
	private int j;
	
	public Ordem(Comando o) {
		this(o, 0, 0);
	}
	
	public Ordem(Comando o, int i, int j) {
		this.c = o;
		this.i = i;
		this.j = j;
	}
	
	public int getI() {
		return this.i;
	}
	
	public int getJ() {
		return this.j;
	}
	
	public Comando getComando() {
		return this.c;
	}
	
	public Mensagem empacotar() {
		return new Mensagem(this.c.getCorrespondente(), String.format("%d %d", this.i, this.j));
	}

}

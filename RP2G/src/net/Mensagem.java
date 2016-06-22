package net;

import java.io.Serializable;
import java.util.Objects;

import core.Ordem;
import core.Ordem.Comando;

public class Mensagem implements Serializable {
	
	private static final long serialVersionUID = -7296895061227631046L;

	public static enum Evento {
		INICIO_CONEXAO(null),
		CONFIRMACAO(null),
		QUEDA_CONEXAO(null),
		DESSINCRONIA(null),
		
		INICIO_TURNO(null),
		MOVIMENTO(Comando.MOVER),
		ATAQUE(Comando.ATACAR),
		FIM_TURNO(Comando.ENCERRAR);
		
		private Comando correspondente;
		
		private Evento(Comando correspondente) {
			this.correspondente = correspondente;
		}
		
		public Comando getCorrespondente() {
			return this.correspondente;
		}
		
		public static Evento getCorrespondente(Comando c) {
			for (Evento e : Evento.values())
				if (e.correspondente == c)
					return e;
			return null;
		}
	}
	
	private final Evento evento;
	private final String msg;
	
	public Mensagem(Evento e) {
		this(e, "");
	}
	
	public Mensagem(Evento e, String msg) {
		this.evento = Objects.requireNonNull(e);
		this.msg = Objects.requireNonNull(msg);
	}
	
	public Mensagem(Ordem o) {
		this.evento = Evento.getCorrespondente(o.getComando());
		System.err.println(o.getComando() + " " + this.evento);
		this.msg = o.getArg() == null ? "" : o.getArg().toString();
	}
	
	public Evento getEvento() {
		return this.evento;
	}
	
	public String getMsg() {
		return this.msg;
	}
	
	@Override
	public String toString() {
		return String.format("Mensagem: [%s, %s]", this.evento, this.msg);
	}
	
}

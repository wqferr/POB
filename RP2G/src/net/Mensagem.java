package net;

import java.io.Serializable;
import java.util.Objects;

public class Mensagem implements Serializable {
	
	private static final long serialVersionUID = -7296895061227631046L;

	public static enum Evento {
		INICIO_CONEXAO,
		CONFIRMACAO,
		QUEDA_CONEXAO,
		DESSINCRONIA,
		
		INICIO_TURNO,
		MOVIMENTO,
		ATAQUE,
		FIM_TURNO
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
	
	public Evento getEvento() {
		return this.evento;
	}
	
	public String getMsg() {
		return this.msg;
	}
	
	@Override
	public String toString() {
		return String.format("Mensagem: [%s, %s]", this.evento.name(), this.msg);
	}
	
}

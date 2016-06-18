package net.msg;

import java.io.Serializable;
import java.util.Objects;

public class Mensagem implements Serializable {
	
	public static enum Evento {
		INICIO_CONEXAO,
		COMANDO_FEITO,
		QUEDA_CONEXAO,
		DESSINCRONIA,
		
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

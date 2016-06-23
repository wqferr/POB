package net;

import java.io.Serializable;
import java.util.Objects;

import core.Ordem;
import core.Ordem.Comando;

/**
 * Unidade de comunicação padrão entre Cliente e Servidor.
 */
public class Mensagem implements Serializable {
	
	private static final long serialVersionUID = -7296895061227631046L;

	/**
	 * Eventos que podem ser notificados entre os dois hosts.
	 */
	public static enum Evento {
		INICIO_CONEXAO(null),
		CONFIRMACAO(null),
		QUEDA_CONEXAO(null),
		DESSINCRONIA(null),
		
		INICIO_TURNO(null),
		MOVIMENTO(Comando.MOVER),
		ATAQUE(Comando.ATACAR),
		USO(Comando.USAR),
		FIM_TURNO(Comando.ENCERRAR);
		
		private Comando correspondente;
		
		private Evento(Comando correspondente) {
			this.correspondente = correspondente;
		}
		
		public Comando getCorrespondente() {
			return this.correspondente;
		}
		
		/**
		 * Retorna o Evento correspondente a um Comando dado.
		 * @param c O comando a ser convertido.
		 * @return O Evento correspondente, se existir. null caso contrário.
		 */
		public static Evento getCorrespondente(Comando c) {
			for (Evento e : Evento.values())
				if (e.correspondente == c)
					return e;
			return null;
		}
	}
	
	private final Evento evento;
	private final String msg;
	
	/**
	 * Cria uma nova mensagem de argumento vazio.
	 * @param e
	 */
	public Mensagem(Evento e) {
		this(e, "");
	}
	
	/**
	 * Cria uma nova mensagem.
	 * @param e O Evento a ser notificado.
	 * @param msg O argumento da mensagem.
	 */
	public Mensagem(Evento e, String msg) {
		this.evento = Objects.requireNonNull(e);
		this.msg = Objects.requireNonNull(msg);
	}
	
	/**
	 * Converte uma Ordem em uma Mensagem.
	 * @param o A Ordem a ser convertida.
	 */
	public Mensagem(Ordem o) {
		this.evento = Evento.getCorrespondente(o.getComando());
		this.msg = o.getArg() == null ? "" : o.getArg().toString();
	}
	
	/**
	 * Retorna o Evento notificado por esta mensagem.
	 * @return O Evento da mensagem.
	 */
	public Evento getEvento() {
		return this.evento;
	}
	
	/**
	 * Retorna o argumento dado à mensagem.
	 * @return O argumento da mensagem.
	 */
	public String getMsg() {
		return this.msg;
	}
	
	@Override
	public String toString() {
		return String.format("Mensagem: [%s, %s]", this.evento, this.msg);
	}
	
}

package core;

import java.util.Scanner;

import net.Mensagem;
import core.mapa.Posicao;

public class Ordem {
	
	public static enum Comando {
		MOVER,
		ATACAR,
		ENCERRAR;
	}

	private Comando c;
	private Object arg;
	
	public Ordem(Comando c) {
		this(c, null);
	}
	
	public Ordem(Comando o, Object arg) {
		this.c = o;
		this.arg = arg;
	}
	
	public Ordem(Mensagem m) {
		this.c = m.getEvento().getCorrespondente();
		try (Scanner s = new Scanner(m.getMsg())) {
            switch (this.c) {
                case MOVER:
                case ATACAR:
                    this.arg = new Posicao(s.nextInt(), s.nextInt());
                    break;
                    
                default:
                    this.arg = null;
            }
		}
	}
	
	public Object getArg() {
		return this.arg;
	}
	
	public Comando getComando() {
		return this.c;
	}

}

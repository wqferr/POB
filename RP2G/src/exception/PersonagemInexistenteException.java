package exception;

/**
 * Exceção para Personagens não existentes nos registros
 *
 */


public class PersonagemInexistenteException extends RuntimeException {
	private static final long serialVersionUID = 5437124L;

	public PersonagemInexistenteException() {
		super();
	}

	public PersonagemInexistenteException(String arg0) {
		super(arg0);
	}
}

package exception;

/**
 * Exceção para nomes que já estão nos registros
 *
 */


public class NomeRepetidoException extends Exception {

	private static final long serialVersionUID = 5964636055505723369L;

	public NomeRepetidoException() {
		super();
	}

	public NomeRepetidoException(String arg0) {
		super(arg0);
	}

}

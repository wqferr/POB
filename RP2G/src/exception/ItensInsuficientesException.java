package exception;

/**
 * Exceção para quantidade insuficiente de Itens
 *
 */


public class ItensInsuficientesException extends RuntimeException {

	private static final long serialVersionUID = -5746623830208457615L;

	public ItensInsuficientesException() {
		super();
	}

	public ItensInsuficientesException(String message) {
		super(message);
	}

}

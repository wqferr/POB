package exception;

/**
 * Exceção para Itens invalidos
 *
 */


public class ItemInvalidoException extends Exception {
	
	private static final long serialVersionUID = 3930531657758061597L;

	public ItemInvalidoException() {
		super();
	}
	
	public ItemInvalidoException(String msg) {
		super(msg);
	}
	
}

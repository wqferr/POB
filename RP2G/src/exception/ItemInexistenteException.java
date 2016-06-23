package exception;

/**
 * Exceção para Itens não existentes nos registros
 *
 */


public class ItemInexistenteException extends RuntimeException {

	private static final long serialVersionUID = 4455177732306298862L;

	public ItemInexistenteException() {
		super();
	}

	public ItemInexistenteException(String arg0) {
		super(arg0);
	}

}

package exception;

/**
 * Exceção para Mapas não existentes nos registros
 *
 */


public class MapaInexistenteException extends RuntimeException{
	private static final long serialVersionUID = 543237124L;

	public MapaInexistenteException() {
		super();
	}

	public MapaInexistenteException(String arg0) {
		super(arg0);
	}
}

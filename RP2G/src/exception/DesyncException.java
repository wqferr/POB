package exception;

import java.io.IOException;

/**
 * Exceção para desincronização entre cliente e servidor
 *
 */

public class DesyncException extends IOException {

	private static final long serialVersionUID = -5678090440872557777L;

	public DesyncException() {
		super();
	}

	public DesyncException(String arg0) {
		super(arg0);
	}

}

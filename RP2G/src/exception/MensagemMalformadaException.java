package exception;

/**
 * Exceção para mensagens que não seguem o padrão
 *
 */


public class MensagemMalformadaException extends Exception {
	
	private static final long serialVersionUID = -2217567666319694050L;

	public MensagemMalformadaException() {
		super();
	}
	
	public MensagemMalformadaException(String s) {
		super(s);
	}

}

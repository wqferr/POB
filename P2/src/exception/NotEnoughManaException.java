package exception;

public class NotEnoughManaException extends Exception {
	
	private static final long serialVersionUID = -6878332984410862867L;

	public NotEnoughManaException() {
		super();
	}
	
	public NotEnoughManaException(String msg) {
		super(msg);
	}

}
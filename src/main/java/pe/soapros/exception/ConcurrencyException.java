package pe.soapros.exception;

public class ConcurrencyException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2556663300001354606L;
	
	/**
	 * Constructor.
	 */
	public ConcurrencyException() {
		super();
	}

	/**
	 * Constructor.
	 */
	public ConcurrencyException(final String message) {
		super(message);
	}

	/**
	 * Constructor.
	 */
	public ConcurrencyException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 */
	public ConcurrencyException(final String message, final Throwable cause) {
		super(message, cause);
	}

}

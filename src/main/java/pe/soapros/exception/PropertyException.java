package pe.soapros.exception;

public class PropertyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5617196288030767280L;

	/**
	 * Constructor.
	 */
	public PropertyException() {
		super();
	}

	/**
	 * Constructor.
	 */
	public PropertyException(final String message) {
		super(message);
	}

	/**
	 * Constructor.
	 */
	public PropertyException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 */
	public PropertyException(final String message, final Throwable cause) {
		super(message, cause);
	}
}

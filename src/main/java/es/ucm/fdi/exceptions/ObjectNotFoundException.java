package es.ucm.fdi.exceptions;

import java.lang.RuntimeException;
import java.lang.Throwable;

/**
 * Exception indicating the requested <code>Object</code> does not exist in the
 * current context.
 */
public class ObjectNotFoundException extends RuntimeException {
	
	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = -2377283231710934604L;

	/**
	 * Constructor with message.
	 * @param message
	 */
	public ObjectNotFoundException(String message) {
		super(message);
	}

	/**
	 * Constructor with message and cause.
	 * @param message
	 * @param cause
	 */
	public ObjectNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
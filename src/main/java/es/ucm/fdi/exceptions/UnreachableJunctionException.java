package es.ucm.fdi.exceptions;

import java.lang.RuntimeException;
import java.lang.Throwable;

/**
 * 	Thrown when there is no pathway to an objective <code>Junction</code> from the current one.
 */
public class UnreachableJunctionException extends RuntimeException {
	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = 7559395491950698636L;

	/**
	 * Constructor with message.
	 * @param message
	 */
	public UnreachableJunctionException(String message) {
		super(message);
	}

	/**
	 * Constructor with message and cause.
	 * 
	 * @param message
	 * @param cause
	 */
	public UnreachableJunctionException(String message, Throwable cause) {
		super(message, cause);
	}
}

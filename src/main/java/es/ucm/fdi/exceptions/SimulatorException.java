package es.ucm.fdi.exceptions;

import java.lang.Throwable;

/**
 * Exception for wrapping and propagation purposes in the simulator. Indicates
 * an error during simulation.
 */
public class SimulatorException extends Exception {

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = 8261793937609458837L;

	/**
	 * Constructor with message.
	 * @param message
	 */
	public SimulatorException(String message) {
		super(message);
	}

	/**
	 * Constructor with message and cause.
	 * @param message
	 * @param cause
	 */
	public SimulatorException(String message, Throwable cause) {
		super(message, cause);
	}
}

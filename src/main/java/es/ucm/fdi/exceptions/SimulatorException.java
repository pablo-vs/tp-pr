package es.ucm.fdi.exceptions;

import java.lang.Throwable;

/**
 * Most general exception in the simulator.
 */
public class SimulatorException extends Exception {

	private static final long serialVersionUID = 34L;

	public SimulatorException(String message) {
		super(message);
	}

	public SimulatorException(String message, Throwable cause) {
		super(message, cause);
	}
}

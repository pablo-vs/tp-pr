package es.ucm.fdi.exceptions;

import java.lang.RuntimeException;
import java.lang.Throwable;

public class UnreachableJunctionException extends RuntimeException {
	
	private static final long serialVersionUID = 40L;

	public UnreachableJunctionException(String message) {
		super(message);
	}

	public UnreachableJunctionException(String message, Throwable cause) {
		super(message, cause);
	}
}

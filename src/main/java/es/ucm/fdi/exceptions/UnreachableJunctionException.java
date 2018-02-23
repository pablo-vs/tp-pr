package es.ucm.fdi.exceptions;

import java.lang.RuntimeException;

public class UnreachableJunctionException extends RuntimeException {
	
	private static final long serialVersionUID = 40L;

	public UnreachableJunctionException(String message) {
		super(message);
	}
}

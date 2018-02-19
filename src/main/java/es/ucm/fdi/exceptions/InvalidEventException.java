package es.ucm.fdi.exceptions;

import java.lang.RuntimeException;

public class InvalidEventException extends RuntimeException {

	private static final long serialVersionUID = 20L;

	public InvalidEventException(String message) {
		super(message);
	}
}
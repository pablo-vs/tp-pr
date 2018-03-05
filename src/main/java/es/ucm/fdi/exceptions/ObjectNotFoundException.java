package es.ucm.fdi.exceptions;

import java.lang.RuntimeException;
import java.lang.Throwable;

public class ObjectNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 30L;

	public ObjectNotFoundException(String message) {
		super(message);
	}

	public ObjectNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
package es.ucm.fdi.exceptions;

import java.lang.RuntimeException;

public class ObjectNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 30L;

	public ObjectNotFoundException(String message) {
		super(message);
	}
}
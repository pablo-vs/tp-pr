package es.ucm.fdi.exceptions;

import java.lang.RuntimeException;

public class InvalidIdException extends RuntimeException {

	private static final long serialVersionUID = 21L;

	public InvalidIdException(String id) {
		super("Invalid object identifier: " + id);
	}
}
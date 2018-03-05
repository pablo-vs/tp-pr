package es.ucm.fdi.exceptions;

import java.lang.RuntimeException;

public class InvalidIDException extends RuntimeException {

	private static final long serialVersionUID = 21L;

	public InvalidIDException(String id) {
		super("Invalid object identifier: " + id);
	}
}
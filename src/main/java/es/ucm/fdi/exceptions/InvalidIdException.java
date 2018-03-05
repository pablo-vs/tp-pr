package es.ucm.fdi.exceptions;

import java.lang.RuntimeException;
import java.lang.Throwable;

public class InvalidIdException extends RuntimeException {

    private static final long serialVersionUID = 21L;

    public InvalidIdException(String id) {
	super("Invalid object identifier: " + id);
    }
	
    public InvalidIdException(String message) {
	super(message);
    }

    public InvalidIdException(String message, Throwable cause) {
	super(message, cause);
    }
}
package es.ucm.fdi.exceptions;

import java.lang.RuntimeException;
import java.lang.Throwable;

public class InvalidIDException extends RuntimeException {

    private static final long serialVersionUID = 21L;

    public InvalidIDException(String id) {
	super("Invalid object identifier: " + id);
    }
	
    public InvalidIDException(String message) {
	super(message);
    }

    public InvalidIDException(String message, Throwable cause) {
	super(message, cause);
    }
}
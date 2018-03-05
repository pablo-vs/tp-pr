package es.ucm.fdi.exceptions;

import java.lang.RuntimeException;
import java.lang.Throwable;

public class InvalidEventException extends RuntimeException {

    private static final long serialVersionUID = 20L;

    public InvalidEventException(String message) {
	super(message);
    }
    
    public InvalidEventException(String message, Throwable cause) {
	super(message, cause);
    }
}
package com.sanket.chess.service.exception;

/**
 * @author Sanket Revankar
 * Created on 27-01-2021
 */

public class InvalidMoveException extends Exception {
    static final long serialVersionUID = 12345L;

    public InvalidMoveException(String message) {
        super(message);
    }
}

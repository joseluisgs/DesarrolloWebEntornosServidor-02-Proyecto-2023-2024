package dev.joseluisgs.tiendaapispringboot.auth.exceptions;

public abstract class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }
}

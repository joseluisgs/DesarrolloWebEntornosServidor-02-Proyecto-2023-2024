package dev.joseluisgs.tiendaapispringboot.users.exceptions;

public abstract class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }
}

package dev.joseluisgs.tiendaapispringboot.exceptions;

public abstract class ProductoException extends RuntimeException {
    public ProductoException(String message) {
        super(message);
    }
}

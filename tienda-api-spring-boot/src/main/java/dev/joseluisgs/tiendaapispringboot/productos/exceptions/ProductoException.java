package dev.joseluisgs.tiendaapispringboot.productos.exceptions;

public abstract class ProductoException extends RuntimeException {
    public ProductoException(String message) {
        super(message);
    }
}

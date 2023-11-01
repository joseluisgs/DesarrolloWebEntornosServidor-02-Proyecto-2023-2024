package dev.joseluisgs.tiendaapispringboot.rest.categorias.exceptions;

public abstract class CategoriaException extends RuntimeException {
    public CategoriaException(String message) {
        super(message);
    }
}

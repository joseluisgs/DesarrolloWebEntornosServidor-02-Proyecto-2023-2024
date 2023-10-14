package dev.joseluisgs.tiendaapispringboot.exceptions;

public class ProductoBadRequest extends ProductoException {
    public ProductoBadRequest(String message) {
        super(message);
    }
}

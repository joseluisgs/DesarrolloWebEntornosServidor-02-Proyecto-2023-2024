package dev.joseluisgs.tiendaapispringboot.productos.exceptions;

public class ProductoBadRequest extends ProductoException {
    public ProductoBadRequest(String message) {
        super(message);
    }
}

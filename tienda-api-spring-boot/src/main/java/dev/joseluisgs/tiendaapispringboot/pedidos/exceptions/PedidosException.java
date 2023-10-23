package dev.joseluisgs.tiendaapispringboot.pedidos.exceptions;

public abstract class PedidosException extends RuntimeException {
    public PedidosException(String message) {
        super(message);
    }
}

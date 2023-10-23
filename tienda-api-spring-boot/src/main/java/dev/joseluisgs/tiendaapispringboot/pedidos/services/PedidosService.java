package dev.joseluisgs.tiendaapispringboot.pedidos.services;

import dev.joseluisgs.tiendaapispringboot.pedidos.models.Pedido;

import java.util.UUID;

public interface PedidosService {
    Pedido save(Pedido pedido);

    void delete(UUID idPedido);
}

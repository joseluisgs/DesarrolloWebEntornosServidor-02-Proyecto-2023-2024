package dev.joseluisgs.tiendaapispringboot.pedidos.services;

import dev.joseluisgs.tiendaapispringboot.pedidos.models.Pedido;
import org.bson.types.ObjectId;

import java.util.List;

public interface PedidosService {
    List<Pedido> findAll();

    Pedido findById(ObjectId idPedido);

    Pedido save(Pedido pedido);

    void delete(ObjectId idPedido);

    Pedido update(ObjectId idPedido, Pedido pedido);
}

package dev.joseluisgs.tiendaapispringboot.pedidos.repositories;

import dev.joseluisgs.tiendaapispringboot.pedidos.models.Pedido;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface PedidosRepository extends MongoRepository<Pedido, ObjectId> {
}

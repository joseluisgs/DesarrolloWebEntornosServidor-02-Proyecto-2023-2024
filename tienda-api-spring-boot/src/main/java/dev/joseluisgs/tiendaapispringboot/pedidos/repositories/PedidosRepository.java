package dev.joseluisgs.tiendaapispringboot.pedidos.repositories;

import dev.joseluisgs.tiendaapispringboot.pedidos.models.Pedido;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidosRepository extends MongoRepository<Pedido, ObjectId> {
    Page<Pedido> findPedidoByIdUsuario(Long idUsuario, Pageable pageable);
}

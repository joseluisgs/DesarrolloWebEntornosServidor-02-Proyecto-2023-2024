package dev.joseluisgs.tiendaapispringboot.pedidos.services;

import dev.joseluisgs.tiendaapispringboot.pedidos.models.Pedido;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PedidosServiceImpl implements PedidosService {
    @Override
    public Pedido save(Pedido pedido) {
        log.info("Guardando pedido: {}", pedido);
        return pedido;
    }
}

package dev.joseluisgs.tiendaapispringboot.pedidos.controllers;

import dev.joseluisgs.tiendaapispringboot.pedidos.models.Pedido;
import dev.joseluisgs.tiendaapispringboot.pedidos.services.PedidosService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${api.version}/pedidos") // Es la ruta del controlador
@Slf4j
public class PedidosRestController {
    private final PedidosService pedidosService;

    @Autowired
    public PedidosRestController(PedidosService pedidosService) {
        this.pedidosService = pedidosService;
    }

    // Post pedidos
    @PostMapping()
    public ResponseEntity<Pedido> createPedido(@Valid @RequestBody Pedido pedido) {
        log.info("Creando pedido: " + pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidosService.save(pedido));
    }

    @DeleteMapping("/{id}")
    public void deletePedido(@PathVariable("id") UUID idPedido) {
        log.info("Borrando pedido con id: " + idPedido);
        pedidosService.delete(idPedido);
    }
}

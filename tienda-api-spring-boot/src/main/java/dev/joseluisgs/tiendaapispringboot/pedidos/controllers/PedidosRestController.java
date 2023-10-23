package dev.joseluisgs.tiendaapispringboot.pedidos.controllers;

import dev.joseluisgs.tiendaapispringboot.pedidos.models.Pedido;
import dev.joseluisgs.tiendaapispringboot.pedidos.services.PedidosService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.version}/pedidos") // Es la ruta del controlador
@Slf4j
public class PedidosRestController {
    private final PedidosService pedidosService;

    @Autowired
    public PedidosRestController(PedidosService pedidosService) {
        this.pedidosService = pedidosService;
    }

    // Get pedidos
    @GetMapping()
    public ResponseEntity<List<Pedido>> getAllPedidos() {
        log.info("Obteniendo todos los pedidos");
        return ResponseEntity.ok(pedidosService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedido(@PathVariable("id") ObjectId idPedido) {
        log.info("Obteniendo pedido con id: " + idPedido);
        return ResponseEntity.ok(pedidosService.findById(idPedido));
    }

    // Post pedidos
    @PostMapping()
    public ResponseEntity<Pedido> createPedido(@Valid @RequestBody Pedido pedido) {
        log.info("Creando pedido: " + pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidosService.save(pedido));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> updatePedido(@PathVariable("id") ObjectId idPedido, @Valid @RequestBody Pedido pedido) {
        log.info("Actualizando pedido con id: " + idPedido);
        return ResponseEntity.ok(pedidosService.update(idPedido, pedido));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Pedido> deletePedido(@PathVariable("id") ObjectId idPedido) {
        log.info("Borrando pedido con id: " + idPedido);
        pedidosService.delete(idPedido);
        return ResponseEntity.noContent().build();
    }
}

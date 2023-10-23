package dev.joseluisgs.tiendaapispringboot.pedidos.services;

import dev.joseluisgs.tiendaapispringboot.pedidos.exceptions.ProductoBadPrice;
import dev.joseluisgs.tiendaapispringboot.pedidos.exceptions.ProductoNotFound;
import dev.joseluisgs.tiendaapispringboot.pedidos.exceptions.ProductoNotStock;
import dev.joseluisgs.tiendaapispringboot.pedidos.models.LineaPedido;
import dev.joseluisgs.tiendaapispringboot.pedidos.models.Pedido;
import dev.joseluisgs.tiendaapispringboot.productos.repositories.ProductosRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class PedidosServiceImpl implements PedidosService {
    private final ProductosRepository productosRepository;

    public PedidosServiceImpl(ProductosRepository productosRepository) {
        this.productosRepository = productosRepository;
    }

    @Override
    @Transactional
    public Pedido save(Pedido pedido) {
        log.info("Guardando pedido: {}", pedido);

        // Comprobamos el pedido y sus datos
        checkPedido(pedido);

        // Actualizamos el stock de los productos
        pedido.getLineasPedido().forEach(lineaPedido -> {
            var producto = productosRepository.findById(lineaPedido.getIdProducto()).get(); // Siempre existe porque ha pasado el check
            // Si existe, comprobamos si hay stock
            var productoToUpdate = producto.updateStock(
                    producto.getStock() - lineaPedido.getCantidad()
            );
            // producto.setStock(producto.getStock() - lineaPedido.getCantidad());
            productosRepository.save(productoToUpdate);
            // Actualizamos el total de la linea de pedido
            lineaPedido.setTotal(lineaPedido.getCantidad() * lineaPedido.getPrecioProducto());
        });

        // Calculamos el total del pedido
        var total = pedido.getLineasPedido().stream()
                .map(lineaPedido -> lineaPedido.getCantidad() * lineaPedido.getPrecioProducto())
                .reduce(0.0, Double::sum);

        // Calculamos el total de items del pedido
        var totalItems = pedido.getLineasPedido().stream()
                .map(LineaPedido::getCantidad)
                .reduce(0, Integer::sum);

        // Actualizamos el total del pedido y el total de items
        pedido.setTotal(total);
        pedido.setTotalItems(totalItems);

        // Guardamos el pedido en la base de datos
        // Si existe lo actualizamos, son cosas que veremos!!!

        return pedido;
    }

    @Override
    @Transactional
    public void delete(UUID idPedido) {
        log.info("Borrando pedido: " + idPedido);
        // Lo primero que tenemos que ver es si existe el pedido
        // Si no existe, lanzamos una excepción
        // Lo haremos luego
        var pedido = new Pedido(); // Lo simulamos

        // Ahora debemos devolver el stock de los productos
        pedido.getLineasPedido().forEach(lineaPedido -> {
            var producto = productosRepository.findById(lineaPedido.getIdProducto()).get(); // Siempre existe porque ha pasado el check
            // Si existe, comprobamos si hay stock
            var productoToUpdate = producto.updateStock(
                    producto.getStock() + lineaPedido.getCantidad()
            );
            // producto.setStock(producto.getStock() + lineaPedido.getCantidad());
            productosRepository.save(productoToUpdate);
        });

        // Borramos el pedido de la base de datos


    }

    private void checkPedido(Pedido pedido) {
        log.info("Comprobando pedido: {}", pedido);
        // Lo primero que tenemos que hacer es ver si existe el is del usuario
        // Ahora no lo tenemos!!! (Lo veremos más adelante)

        // Siguiente paso, es ver si los productos existen y si hay stock
        // Si no existen, lanzamos una excepción
        pedido.getLineasPedido().forEach(lineaPedido -> {
            var producto = productosRepository.findById(lineaPedido.getIdProducto())
                    .orElseThrow(() -> new ProductoNotFound(lineaPedido.getIdProducto()));
            // Si existe, comprobamos si hay stock
            if (producto.getStock() < lineaPedido.getCantidad()) {
                throw new ProductoNotStock(lineaPedido.getIdProducto());
            }
            // Podemos comprobar más cosas, como si el precio es el mismo, etc...
            if (!producto.getPrecio().equals(lineaPedido.getPrecioProducto())) {
                throw new ProductoBadPrice(lineaPedido.getIdProducto());
            }
        });
    }
}

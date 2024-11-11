package dev.joseluisgs.tiendaapispringboot.rest.pedidos.services;

import dev.joseluisgs.tiendaapispringboot.rest.pedidos.exceptions.*;
import dev.joseluisgs.tiendaapispringboot.rest.pedidos.models.LineaPedido;
import dev.joseluisgs.tiendaapispringboot.rest.pedidos.models.Pedido;
import dev.joseluisgs.tiendaapispringboot.rest.pedidos.repositories.PedidosRepository;
import dev.joseluisgs.tiendaapispringboot.rest.productos.repositories.ProductosRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@CacheConfig(cacheNames = {"pedidos"})
public class PedidosServiceImpl implements PedidosService {
    private final PedidosRepository pedidosRepository;
    private final ProductosRepository productosRepository;

    public PedidosServiceImpl(PedidosRepository pedidosRepository, ProductosRepository productosRepository) {
        this.pedidosRepository = pedidosRepository;
        this.productosRepository = productosRepository;
    }

    @Override
    public Page<Pedido> findAll(Pageable pageable) {
        // Podemos paginar y hacer otras cosas
        log.info("Obteniendo todos los pedidos paginados y ordenados con {}", pageable);
        return pedidosRepository.findAll(pageable);
    }


    @Override
    @Cacheable(key = "#idPedido")
    public Pedido findById(ObjectId idPedido) {
        log.info("Obteniendo pedido con id: " + idPedido);
        return pedidosRepository.findById(idPedido).orElseThrow(() -> new PedidoNotFound(idPedido.toHexString()));
    }

    @Override
    public Page<Pedido> findByIdUsuario(Long idUsuario, Pageable pageable) {
        log.info("Obteniendo pedidos del usuario con id: " + idUsuario);
        return pedidosRepository.findByIdUsuario(idUsuario, pageable);
    }

    @Override
    @Transactional
    @CachePut(key = "#result.id")
    public Pedido save(Pedido pedido) {
        log.info("Guardando pedido: {}", pedido);

        // Comprobamos el pedido y sus datos
        checkPedido(pedido);

        // Actualizamos el stock de los productos
        var pedidoToSave = reserveStockPedidos(pedido);

        // Fecha de creación y actualización
        pedidoToSave.setCreatedAt(LocalDateTime.now());
        pedidoToSave.setUpdatedAt(LocalDateTime.now());

        // Guardamos el pedido en la base de datos
        // Si existe lo actualizamos, son cosas que veremos!!!

        return pedidosRepository.save(pedidoToSave);
    }

    Pedido reserveStockPedidos(Pedido pedido) {
        log.info("Reservando stock del pedido: {}", pedido);

        if (pedido.getLineasPedido() == null || pedido.getLineasPedido().isEmpty()) {
            throw new PedidoNotItems(pedido.getId().toHexString());
        }

        pedido.getLineasPedido().forEach(lineaPedido -> {
            var producto = productosRepository.findById(lineaPedido.getIdProducto()).get(); // Siempre existe porque ha pasado el check
            // Si existe, comprobamos si hay stock
            producto.setStock(producto.getStock() - lineaPedido.getCantidad());
            // producto.setStock(producto.getStock() - lineaPedido.getCantidad());
            productosRepository.save(producto);
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

        return pedido;
    }

    @Override
    @Transactional
    @CacheEvict(key = "#idPedido")
    public void delete(ObjectId idPedido) {
        log.info("Borrando pedido: " + idPedido);
        // Lo primero que tenemos que ver es si existe el pedido
        // Si no existe, lanzamos una excepción
        // Lo haremos luego
        var pedidoToDelete = pedidosRepository.findById(idPedido).orElseThrow(() -> new PedidoNotFound(idPedido.toHexString()));

        // Ahora debemos devolver el stock de los productos
        returnStockPedidos(pedidoToDelete);

        // Borramos el pedido
        pedidosRepository.deleteById(idPedido);
    }

    Pedido returnStockPedidos(Pedido pedido) {
        log.info("Retornando stock del pedido: {}", pedido);
        if (pedido.getLineasPedido() != null) {
            pedido.getLineasPedido().forEach(lineaPedido -> {
                var producto = productosRepository.findById(lineaPedido.getIdProducto()).get(); // Siempre existe porque ha pasado el check
                // Si existe, comprobamos si hay stock
                producto.setStock(producto.getStock() + lineaPedido.getCantidad());
                // producto.setStock(producto.getStock() + lineaPedido.getCantidad());
                productosRepository.save(producto);
            });
        }
        return pedido;
    }


    @Override
    @Transactional
    @CachePut(key = "#idPedido")
    public Pedido update(ObjectId idPedido, Pedido pedido) {
        log.info("Actualizando pedido con id: " + idPedido);

        // Primero lo buscamos
        var pedidoToUpdate = pedidosRepository.findById(idPedido).orElseThrow(() -> new PedidoNotFound(idPedido.toHexString()));

        // Devolvemos el stock de los productos
        returnStockPedidos(pedido);

        // Comprobamos el pedido y sus datos
        checkPedido(pedido);

        // Actualizamos el stock de los productos
        var pedidoToSave = reserveStockPedidos(pedido);

        // Fecha actualización
        pedidoToSave.setUpdatedAt(LocalDateTime.now());

        // Actualizamos el pedido en la base de datos
        // Si existe lo actualizamos, son cosas que veremos!!!
        return pedidosRepository.save(pedidoToSave);

    }

    void checkPedido(Pedido pedido) {
        log.info("Comprobando pedido: {}", pedido);
        // Lo primero que tenemos que hacer es ver si existe el is del usuario
        // Ahora no lo tenemos!!! (Lo veremos más adelante)

        // Siguiente paso, es ver si los productos existen y si hay stock
        // Si no existen, lanzamos una excepción
        if (pedido.getLineasPedido() == null || pedido.getLineasPedido().isEmpty()) {
            throw new PedidoNotItems(pedido.getId().toHexString());
        }
        pedido.getLineasPedido().forEach(lineaPedido -> {
            var producto = productosRepository.findById(lineaPedido.getIdProducto())
                    .orElseThrow(() -> new ProductoNotFound(lineaPedido.getIdProducto()));
            // Si existe, comprobamos si hay stock
            if (producto.getStock() < lineaPedido.getCantidad() && lineaPedido.getCantidad() > 0) {
                throw new ProductoNotStock(lineaPedido.getIdProducto());
            }
            // Podemos comprobar más cosas, como si el precio es el mismo, etc...
            if (!producto.getPrecio().equals(lineaPedido.getPrecioProducto())) {
                throw new ProductoBadPrice(lineaPedido.getIdProducto());
            }
        });
    }
}

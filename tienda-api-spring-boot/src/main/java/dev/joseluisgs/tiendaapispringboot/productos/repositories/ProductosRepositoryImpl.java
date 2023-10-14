package dev.joseluisgs.tiendaapispringboot.productos.repositories;

import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Implementación de nuestro repositorio de productos
 * Usamos @Repository para indicar que es un repositorio
 * y que Spring lo gestione con el contexto de Spring en el arranque IoC (Inversión de Control)
 */

@Repository
public class ProductosRepositoryImpl implements ProductosRepository {
    private final Logger logger = LoggerFactory.getLogger(ProductosRepositoryImpl.class);

    private final Map<Long, Producto> productos = new LinkedHashMap<>(
            Map.of(
                    1L, new Producto(1L, "Nike", "Air Max", "Nike Air Max", 100.0, "http://placeimg.com/640/480/people", "OTROS", 10, LocalDateTime.now(), LocalDateTime.now(), UUID.randomUUID()),
                    2L, new Producto(2L, "Adidas", "Adidas", "Adidas", 100.0, "http://placeimg.com/640/480/people", "COMIDA", 10, LocalDateTime.now(), LocalDateTime.now(), UUID.randomUUID())
            ));

    @Override
    public List<Producto> findAll() {
        logger.info("Buscando todos los productos");
        return productos.values().stream()
                .toList();
    }

    @Override
    public List<Producto> findAllByMarca(String marca) {
        logger.info("Buscando productos por marca: " + marca);
        return productos.values().stream()
                .filter(producto -> producto.getMarca().toLowerCase().contains(marca.toLowerCase()))
                .toList();
    }

    @Override
    public List<Producto> findAllByCategoria(String categoria) {
        logger.info("Buscando productos por categoría: " + categoria);
        return productos.values().stream()
                .filter(producto -> producto.getCategoria().toLowerCase().contains(categoria.toLowerCase()))
                .toList();
    }

    @Override
    public List<Producto> findAllByMarcaAndCategoria(String marca, String categoria) {
        logger.info("Buscando productos por marca: " + marca + " y categoría: " + categoria);
        return productos.values().stream()
                .filter(producto -> producto.getMarca().toLowerCase().contains(marca.toLowerCase()))
                .filter(producto -> producto.getCategoria().toLowerCase().contains(categoria.toLowerCase()))
                .toList();
    }

    @Override
    public Optional<Producto> findById(Long id) {
        logger.info("Buscando producto por id: " + id);
        return productos.get(id) != null ? Optional.of(productos.get(id)) : Optional.empty();
    }

    @Override
    public Optional<Producto> findByUuid(UUID uuid) {
        logger.info("Buscando producto por uuid: " + uuid);
        return productos.values().stream()
                .filter(producto -> producto.getUuid().equals(uuid))
                .findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        logger.info("Comprobando si existe producto por id: " + id);
        return productos.get(id) != null;
    }

    @Override
    public boolean existsByUuid(UUID uuid) {
        logger.info("Comprobando si existe producto por uuid: " + uuid);
        return productos.values().stream()
                .anyMatch(producto -> producto.getUuid().equals(uuid));
    }

    @Override
    public Producto save(Producto producto) {
        logger.info("Guardando producto: " + producto);
        productos.put(producto.getId(), producto);
        return producto;
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Borrando producto por id: " + id);
        productos.remove(id);
    }

    @Override
    public void deleteByUuid(UUID uuid) {
        logger.info("Borrando producto por uuid: " + uuid);
        productos.values().removeIf(producto -> producto.getUuid().equals(uuid));
    }

    @Override
    public Long nextId() {
        logger.debug("Obteniendo siguiente id de producto");
        return productos.keySet().stream()
                .mapToLong(value -> value)
                .max()
                .orElse(0) + 1;
    }
}

package dev.joseluisgs.tiendaapispringboot.productos.repositories;

import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
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
    private final Map<Long, Producto> productos = new LinkedHashMap<>(
            Map.of(
                    1L, new Producto(1L, "Nike", "Air Max", "Nike Air Max", 100.0, "http://placeimg.com/640/480/people", "OTROS", 10, LocalDateTime.now(), LocalDateTime.now(), UUID.randomUUID()),
                    2L, new Producto(2L, "Adidas", "Adidas", "Adidas", 100.0, "http://placeimg.com/640/480/people", "COMIDA", 10, LocalDateTime.now(), LocalDateTime.now(), UUID.randomUUID())
            ));


    @Override
    public List<Producto> findAll() {
        return productos.values().stream()
                .toList();
    }

    @Override
    public List<Producto> findAllByNombre(String nombre) {
        return productos.values().stream()
                .filter(producto -> producto.getMarca().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return productos.get(id) != null ? Optional.of(productos.get(id)) : Optional.empty();
    }

    @Override
    public Optional<Producto> findByUuid(UUID uuid) {
        return productos.values().stream()
                .filter(producto -> producto.getUuid().equals(uuid))
                .findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        return productos.get(id) != null;
    }

    @Override
    public boolean existsByUuid(UUID uuid) {
        return productos.values().stream()
                .anyMatch(producto -> producto.getUuid().equals(uuid));
    }

    @Override
    public Producto save(Producto producto) {
        // Si existe actualizamos los valores
        if (producto.getId() != null && existsById(producto.getId())) {
            return update(producto);
        } else {
            // Si no existe, creamos
            return create(producto);
        }
    }

    private Producto create(Producto producto) {
        // Obtenemos el nuevo id:
        Long id = productos.keySet().stream()
                .mapToLong(value -> value)
                .max()
                .orElse(0) + 1;

        // Creamos el nuevo producto asignando el id y los campos que nos pasan, por inmutabilidad (create, update y uuid ya se asignan)
        Producto nuevoProducto = new Producto(id, producto.getMarca(), producto.getModelo(), producto.getDescripcion(), producto.getPrecio(), producto.getImagen(), producto.getCategoria(), producto.getStock(), LocalDateTime.now(), LocalDateTime.now(), UUID.randomUUID());
        // Lo añadimos a la colección
        productos.put(id, nuevoProducto);
        // Devolvemos el producto creado
        return nuevoProducto;
    }

    private Producto update(Producto producto) {
        // Obtenemos el producto actual
        Producto productoActual = productos.get(producto.getId());

        // Creamos el producto actualizado con los campos que nos llegan actualizando el updateAt y si son null no se actualizan y se quedan los anteriores
        Producto productoActualizado = new Producto(
                productoActual.getId(),
                producto.getMarca() != null ? producto.getMarca() : productoActual.getMarca(),
                producto.getModelo() != null ? producto.getModelo() : productoActual.getModelo(),
                producto.getDescripcion() != null ? producto.getDescripcion() : productoActual.getDescripcion(),
                producto.getPrecio() != null ? producto.getPrecio() : productoActual.getPrecio(),
                producto.getImagen() != null ? producto.getImagen() : productoActual.getImagen(),
                producto.getCategoria() != null ? producto.getCategoria() : productoActual.getCategoria(),
                producto.getStock() != null ? producto.getStock() : productoActual.getStock(),
                productoActual.getCreatedAt(),
                LocalDateTime.now(),
                productoActual.getUuid()
        );

        // Lo actualizamos en la colección
        productos.put(producto.getId(), productoActualizado);
        // Devolvemos el producto actualizado
        return productoActualizado;
    }

    @Override
    public void deleteById(Long id) {
        productos.remove(id);
    }

    @Override
    public void deleteByUuid(UUID uuid) {
        productos.values().removeIf(producto -> producto.getUuid().equals(uuid));
    }
}

package dev.joseluisgs.tiendaapispringboot.productos.repositories;

import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductosRepository {
    List<Producto> findAll();

    List<Producto> findAllByMarca(String marca);

    List<Producto> findAllByCategoria(String categoria);

    List<Producto> findAllByMarcaAndCategoria(String marca, String categoria);

    Optional<Producto> findById(Long id);

    Optional<Producto> findByUuid(UUID uuid);

    boolean existsById(Long id);

    boolean existsByUuid(UUID uuid);

    Producto save(Producto producto);

    void deleteById(Long id);

    void deleteByUuid(UUID uuid);

    Long nextId();
}

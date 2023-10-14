package dev.joseluisgs.tiendaapispringboot.services;

import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;

import java.util.List;

public interface ProductosService {
    List<Producto> findAll(String marca, String categoria);

    Producto findById(Long id);

    Producto findbyUuid(String uuid);

    Producto save(Producto producto);

    Producto update(Long id, Producto producto);

    void deleteById(Long id);
}

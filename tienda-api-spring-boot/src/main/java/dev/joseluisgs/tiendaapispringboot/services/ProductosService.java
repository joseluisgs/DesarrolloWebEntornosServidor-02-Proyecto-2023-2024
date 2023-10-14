package dev.joseluisgs.tiendaapispringboot.services;

import dev.joseluisgs.tiendaapispringboot.dto.ProductoCreateDto;
import dev.joseluisgs.tiendaapispringboot.dto.ProductoUpdateDto;
import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;

import java.util.List;

public interface ProductosService {
    List<Producto> findAll(String marca, String categoria);

    Producto findById(Long id);

    Producto findbyUuid(String uuid);

    Producto save(ProductoCreateDto productoCreateDto);

    Producto update(Long id, ProductoUpdateDto productoUpdateDto);

    void deleteById(Long id);
}

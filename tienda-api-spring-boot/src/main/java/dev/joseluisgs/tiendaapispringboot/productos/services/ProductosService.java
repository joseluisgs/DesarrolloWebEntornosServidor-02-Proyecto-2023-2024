package dev.joseluisgs.tiendaapispringboot.productos.services;

import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoCreateDto;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoUpdateDto;
import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProductosService {
    Page<Producto> findAll(String marca, String categoria, Pageable pageable);

    Producto findById(Long id);

    Producto findbyUuid(String uuid);

    Producto save(ProductoCreateDto productoCreateDto);

    Producto update(Long id, ProductoUpdateDto productoUpdateDto);

    void deleteById(Long id);

    Producto updateImage(Long id, MultipartFile image);
}

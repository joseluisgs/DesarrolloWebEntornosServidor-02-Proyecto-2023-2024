package dev.joseluisgs.tiendaapispringboot.productos.services;

import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoCreateRequest;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoUpdateRequest;
import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface ProductosService {
    Page<Producto> findAll(Optional<String> marca, Optional<String> categoria, Optional<String> modelo, Optional<Boolean> isDeleted, Optional<Double> precioMax, Optional<Double> stockMin, Pageable pageable);

    Producto findById(Long id);

    Producto findbyUuid(String uuid);

    Producto save(ProductoCreateRequest productoCreateRequest);

    Producto update(Long id, ProductoUpdateRequest productoUpdateRequest);

    void deleteById(Long id);

    Producto updateImage(Long id, MultipartFile image, Boolean withUrl);
}

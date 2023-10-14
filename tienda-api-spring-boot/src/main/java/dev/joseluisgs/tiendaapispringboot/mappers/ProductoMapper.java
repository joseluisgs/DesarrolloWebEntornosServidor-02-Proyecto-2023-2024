package dev.joseluisgs.tiendaapispringboot.mappers;

import dev.joseluisgs.tiendaapispringboot.dto.ProductoCreateDto;
import dev.joseluisgs.tiendaapispringboot.dto.ProductoResponseDto;
import dev.joseluisgs.tiendaapispringboot.dto.ProductoUpdateDto;
import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ProductoMapper {
    public Producto toProduct(Long id, ProductoCreateDto dto) {
        return new Producto(
                id,
                dto.getMarca(),
                dto.getModelo(),
                dto.getDescripcion(),
                dto.getPrecio(),
                dto.getImagen(),
                dto.getCategoria(),
                dto.getStock(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                UUID.randomUUID()
        );
    }

    public Producto toProduct(ProductoUpdateDto dto, Producto producto) {
        // Creamos el producto actualizado con los campos que nos llegan actualizando el updateAt y si son null no se actualizan y se quedan los anteriores
        return new Producto(
                producto.getId(),
                dto.getMarca() != null ? dto.getMarca() : producto.getMarca(),
                dto.getModelo() != null ? dto.getModelo() : producto.getModelo(),
                dto.getDescripcion() != null ? dto.getDescripcion() : producto.getDescripcion(),
                dto.getPrecio() != null ? dto.getPrecio() : producto.getPrecio(),
                dto.getImagen() != null ? dto.getImagen() : producto.getImagen(),
                dto.getCategoria() != null ? dto.getCategoria() : producto.getCategoria(),
                dto.getStock() != null ? dto.getStock() : producto.getStock(),
                producto.getCreatedAt(),
                LocalDateTime.now(),
                producto.getUuid()
        );
    }

    public ProductoResponseDto toProductResponseDto(Producto producto) {
        return new ProductoResponseDto(
                producto.getId(),
                producto.getMarca(),
                producto.getModelo(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getImagen(),
                producto.getCategoria(),
                producto.getStock(),
                producto.getCreatedAt(),
                producto.getUpdatedAt(),
                producto.getUuid()
        );
    }
}

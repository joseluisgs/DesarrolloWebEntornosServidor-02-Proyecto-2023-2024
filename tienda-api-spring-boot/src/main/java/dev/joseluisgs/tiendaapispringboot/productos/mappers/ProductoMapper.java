package dev.joseluisgs.tiendaapispringboot.productos.mappers;

import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoCreateDto;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoResponseDto;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoUpdateDto;
import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ProductoMapper {
    public Producto toProduct(ProductoCreateDto dto, Categoria categoria) {
        return new Producto(
                null,
                dto.getMarca(),
                dto.getModelo(),
                dto.getDescripcion(),
                dto.getPrecio(),
                dto.getImagen(),
                dto.getStock(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                UUID.randomUUID(),
                false,
                categoria
        );
    }

    public Producto toProduct(ProductoUpdateDto dto, Producto producto, Categoria categoria) {
        // Creamos el producto actualizado con los campos que nos llegan actualizando el updateAt y si son null no se actualizan y se quedan los anteriores
        return new Producto(
                producto.getId(),
                dto.getMarca() != null ? dto.getMarca() : producto.getMarca(),
                dto.getModelo() != null ? dto.getModelo() : producto.getModelo(),
                dto.getDescripcion() != null ? dto.getDescripcion() : producto.getDescripcion(),
                dto.getPrecio() != null ? dto.getPrecio() : producto.getPrecio(),
                dto.getImagen() != null ? dto.getImagen() : producto.getImagen(),
                dto.getStock() != null ? dto.getStock() : producto.getStock(),
                producto.getCreatedAt(),
                LocalDateTime.now(),
                producto.getUuid(),
                dto.getIsDeleted() != null ? dto.getIsDeleted() : producto.getIsDeleted(),
                categoria
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
                producto.getCategoria().getNombre(),
                producto.getStock(),
                producto.getCreatedAt(),
                producto.getUpdatedAt(),
                producto.getUuid()
        );
    }
}

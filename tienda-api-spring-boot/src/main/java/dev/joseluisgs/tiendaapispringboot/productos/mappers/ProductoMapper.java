package dev.joseluisgs.tiendaapispringboot.productos.mappers;

import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoCreateDto;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoResponseDto;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoUpdateDto;
import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductoMapper {
    public Producto toProduct(ProductoCreateDto dto, Categoria categoria) {
        return Producto.builder()
                .marca(dto.getMarca())
                .modelo(dto.getModelo())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .imagen(dto.getImagen())
                .stock(dto.getStock())
                .categoria(categoria)
                .build();
    }

    public Producto toProduct(ProductoUpdateDto dto, Producto producto, Categoria categoria) {
        // Creamos el producto actualizado con los campos que nos llegan actualizando el updateAt y si son null no se actualizan y se quedan los anteriores
        return Producto.builder()
                .id(producto.getId())
                .marca(dto.getMarca() != null ? dto.getMarca() : producto.getMarca())
                .modelo(dto.getModelo() != null ? dto.getModelo() : producto.getModelo())
                .descripcion(dto.getDescripcion() != null ? dto.getDescripcion() : producto.getDescripcion())
                .precio(dto.getPrecio() != null ? dto.getPrecio() : producto.getPrecio())
                .imagen(dto.getImagen() != null ? dto.getImagen() : producto.getImagen())
                .stock(dto.getStock() != null ? dto.getStock() : producto.getStock())
                .uuid(producto.getUuid())
                .createdAt(producto.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .categoria(categoria)
                .build();
    }

    public ProductoResponseDto toProductResponseDto(Producto producto) {
        return ProductoResponseDto.builder()
                .id(producto.getId())
                .marca(producto.getMarca())
                .modelo(producto.getModelo())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .imagen(producto.getImagen())
                .categoria(producto.getCategoria().getNombre())
                .stock(producto.getStock())
                .createdAt(producto.getCreatedAt())
                .updatedAt(producto.getUpdatedAt())
                .uuid(producto.getUuid())
                .build();
    }
}

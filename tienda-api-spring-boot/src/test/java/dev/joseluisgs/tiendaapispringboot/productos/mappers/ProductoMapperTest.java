package dev.joseluisgs.tiendaapispringboot.productos.mappers;

import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoCreateDto;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoUpdateDto;
import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductoMapperTest {

    private final Categoria categoria = new Categoria(1L, "DEPORTES", LocalDateTime.now(), LocalDateTime.now(), false);

    // Inyectamos el mapper
    private final ProductoMapper productoMapper = new ProductoMapper();

    @Test
    void toProduct() {
        // Arrange
        ProductoCreateDto productoCreateDto = ProductoCreateDto.builder()
                .marca("Samsung")
                .modelo("S20")
                .descripcion("Móvil")
                .precio(1000.0)
                .imagen("https://www.samsung.com/es_es/smartphones/galaxy-s20/")
                .categoria(categoria.getNombre())
                .stock(10)
                .build();

        // Act
        var res = productoMapper.toProduct(productoCreateDto, categoria);

        // Assert
        assertAll(
                () -> assertEquals(productoCreateDto.getMarca(), res.getMarca()),
                () -> assertEquals(productoCreateDto.getModelo(), res.getModelo()),
                () -> assertEquals(productoCreateDto.getDescripcion(), res.getDescripcion()),
                () -> assertEquals(productoCreateDto.getPrecio(), res.getPrecio()),
                () -> assertEquals(productoCreateDto.getImagen(), res.getImagen()),
                () -> assertEquals(productoCreateDto.getCategoria(), res.getCategoria().getNombre()),
                () -> assertEquals(productoCreateDto.getStock(), res.getStock())
        );
    }

    @Test
    void testToProduct() {
        // Arrange
        Long id = 1L;
        ProductoUpdateDto productoUpdateDto = ProductoUpdateDto.builder()
                .marca("Samsung")
                .modelo("S20")
                .descripcion("Móvil")
                .precio(1000.0)
                .imagen("https://www.samsung.com/es_es/smartphones/galaxy-s20/")
                .categoria(categoria.getNombre())
                .stock(10)
                .isDeleted(false)
                .build();

        Producto producto = Producto.builder()
                .id(id)
                .marca(productoUpdateDto.getMarca())
                .modelo(productoUpdateDto.getModelo())
                .descripcion(productoUpdateDto.getDescripcion())
                .precio(productoUpdateDto.getPrecio())
                .imagen(productoUpdateDto.getImagen())
                .stock(productoUpdateDto.getStock())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .uuid(UUID.randomUUID())
                .isDeleted(false)
                .categoria(categoria)
                .build();

        // Act
        var res = productoMapper.toProduct(productoUpdateDto, producto, categoria);

        // Assert
        assertAll(
                () -> assertEquals(id, res.getId()),
                () -> assertEquals(productoUpdateDto.getMarca(), res.getMarca()),
                () -> assertEquals(productoUpdateDto.getModelo(), res.getModelo()),
                () -> assertEquals(productoUpdateDto.getDescripcion(), res.getDescripcion()),
                () -> assertEquals(productoUpdateDto.getPrecio(), res.getPrecio()),
                () -> assertEquals(productoUpdateDto.getImagen(), res.getImagen()),
                () -> assertEquals(productoUpdateDto.getCategoria(), res.getCategoria().getNombre()),
                () -> assertEquals(productoUpdateDto.getStock(), res.getStock())
        );
    }

    @Test
    void toProductResponseDto() {
        // Arrange
        Producto producto = Producto.builder()
                .id(1L)
                .marca("Samsung")
                .modelo("S20")
                .descripcion("Móvil")
                .precio(1000.0)
                .imagen("https://www.samsung.com/es_es/smartphones/galaxy-s20/")
                .stock(10)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .uuid(UUID.randomUUID())
                .isDeleted(false)
                .categoria(categoria)
                .build();
        
        // Act
        var res = productoMapper.toProductResponseDto(producto);

        // Assert
        assertAll(
                () -> assertEquals(producto.getId(), res.getId()),
                () -> assertEquals(producto.getMarca(), res.getMarca()),
                () -> assertEquals(producto.getModelo(), res.getModelo()),
                () -> assertEquals(producto.getDescripcion(), res.getDescripcion()),
                () -> assertEquals(producto.getPrecio(), res.getPrecio()),
                () -> assertEquals(producto.getImagen(), res.getImagen()),
                () -> assertEquals(producto.getCategoria().getNombre(), res.getCategoria()),
                () -> assertEquals(producto.getStock(), res.getStock())
        );
    }
}
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
        ProductoCreateDto productoCreateDto = new ProductoCreateDto(
                "Samsung",
                "S20",
                "Celular",
                1000.0,
                "https://www.samsung.com/es_es/smartphones/galaxy-s20/",
                categoria.getNombre(),
                10
        );

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
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto(
                "Samsung",
                "S20",
                "Celular",
                1000.0,
                "https://www.samsung.com/es_es/smartphones/galaxy-s20/",
                categoria.getNombre(),
                10,
                false
        );

        Producto producto = new Producto(
                id,
                productoUpdateDto.getMarca(),
                productoUpdateDto.getModelo(),
                productoUpdateDto.getDescripcion(),
                productoUpdateDto.getPrecio(),
                productoUpdateDto.getImagen(),
                productoUpdateDto.getStock(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                UUID.randomUUID(),
                false,
                categoria
        );

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
        Producto producto = new Producto(
                1L,
                "Samsung",
                "S20",
                "Celular",
                1000.0,
                "https://www.samsung.com/es_es/smartphones/galaxy-s20/",
                10,
                LocalDateTime.now(),
                LocalDateTime.now(),
                UUID.randomUUID(),
                false,
                categoria
        );

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
package dev.joseluisgs.tiendaapispringboot.productos.mappers;

import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoCreateDto;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoUpdateDto;
import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ProductoMapperTest {

    // Inyectamos el mapper
    @Autowired
    private ProductoMapper productoMapper;

    @Test
    void toProduct() {
        // Arrange
        Long id = 1L;
        ProductoCreateDto productoCreateDto = new ProductoCreateDto(
                "Samsung",
                "S20",
                "Celular",
                1000.0,
                "https://www.samsung.com/es_es/smartphones/galaxy-s20/",
                "Celulares",
                10
        );

        // Act
        var res = productoMapper.toProduct(id, productoCreateDto);

        // Assert
        assertAll(
                () -> assertEquals(id, res.getId()),
                () -> assertEquals(productoCreateDto.getMarca(), res.getMarca()),
                () -> assertEquals(productoCreateDto.getModelo(), res.getModelo()),
                () -> assertEquals(productoCreateDto.getDescripcion(), res.getDescripcion()),
                () -> assertEquals(productoCreateDto.getPrecio(), res.getPrecio()),
                () -> assertEquals(productoCreateDto.getImagen(), res.getImagen()),
                () -> assertEquals(productoCreateDto.getCategoria(), res.getCategoria()),
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
                "Celulares",
                10
        );

        Producto producto = new Producto(
                id,
                productoUpdateDto.getMarca(),
                productoUpdateDto.getModelo(),
                productoUpdateDto.getDescripcion(),
                productoUpdateDto.getPrecio(),
                productoUpdateDto.getImagen(),
                productoUpdateDto.getCategoria(),
                productoUpdateDto.getStock(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                UUID.randomUUID()
        );

        // Act
        var res = productoMapper.toProduct(productoUpdateDto, producto);

        // Assert
        assertAll(
                () -> assertEquals(id, res.getId()),
                () -> assertEquals(productoUpdateDto.getMarca(), res.getMarca()),
                () -> assertEquals(productoUpdateDto.getModelo(), res.getModelo()),
                () -> assertEquals(productoUpdateDto.getDescripcion(), res.getDescripcion()),
                () -> assertEquals(productoUpdateDto.getPrecio(), res.getPrecio()),
                () -> assertEquals(productoUpdateDto.getImagen(), res.getImagen()),
                () -> assertEquals(productoUpdateDto.getCategoria(), res.getCategoria()),
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
                "Celulares",
                10,
                LocalDateTime.now(),
                LocalDateTime.now(),
                UUID.randomUUID()
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
                () -> assertEquals(producto.getCategoria(), res.getCategoria()),
                () -> assertEquals(producto.getStock(), res.getStock())
        );
    }
}
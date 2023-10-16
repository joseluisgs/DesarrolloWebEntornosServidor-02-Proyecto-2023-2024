package dev.joseluisgs.tiendaapispringboot.productos.services;

import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;
import dev.joseluisgs.tiendaapispringboot.categorias.services.CategoriasService;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoCreateDto;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoUpdateDto;
import dev.joseluisgs.tiendaapispringboot.productos.exceptions.ProductoBadUuid;
import dev.joseluisgs.tiendaapispringboot.productos.exceptions.ProductoNotFound;
import dev.joseluisgs.tiendaapispringboot.productos.mappers.ProductoMapper;
import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
import dev.joseluisgs.tiendaapispringboot.productos.repositories.ProductosRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    private final Categoria categoria = new Categoria(1L, "DEPORTES", LocalDateTime.now(), LocalDateTime.now(), false);

    private final Producto producto1 = new Producto(
            1L, "Adidas", "Zapatillas", "Zapatillas de deporte",
            100.0, "http://placeimg.com/640/480/people", 5,
            LocalDateTime.now(), LocalDateTime.now(), UUID.fromString("80e559b5-83c5-4555-ba0b-bb9fddb6e96c"),
            false, categoria
    );
    private final Producto producto2 = new Producto(
            2L, "Nike", "Zapatillas", "Zapatillas de deporte",
            100.0, "http://placeimg.com/640/480/people", 5,
            LocalDateTime.now(), LocalDateTime.now(), UUID.fromString("542f0a0b-064b-4022-b528-3b59f8bae821"),
            false, categoria
    );

    @Mock
    private ProductosRepository productosRepository;
    @Mock
    private CategoriasService categoriaService;
    @Mock
    private ProductoMapper productoMapper;
    @InjectMocks
    private ProductoServiceImpl productoService;
    @Captor // Captor de argumentos
    private ArgumentCaptor<Producto> productoCaptor;

    @Test
    void findAll_ShouldReturnAllProducts_WhenNoParametersProvided() {
        // Arrange
        List<Producto> expectedProducts = Arrays.asList(producto1, producto2);
        when(productosRepository.findAll()).thenReturn(expectedProducts);

        // Act
        List<Producto> actualProducts = productoService.findAll(null, null);

        // Assert
        assertIterableEquals(expectedProducts, actualProducts);

        // Verify
        verify(productosRepository, times(1)).findAll();
    }

    @Test
    void findAll_ShouldReturnProductsByMarca_WhenMarcaParameterProvided() {
        // Arrange
        String marca = "nike";
        List<Producto> expectedProducts = List.of(producto2);
        when(productosRepository.findByMarcaContainsIgnoreCase(marca)).thenReturn(expectedProducts);

        // Act
        List<Producto> actualProducts = productoService.findAll(marca, null);

        // Assert
        assertIterableEquals(expectedProducts, actualProducts);

        // Verify
        verify(productosRepository, times(1)).findByMarcaContainsIgnoreCase(marca);
    }

    @Test
    void findAll_ShouldReturnProductsByCategoria_WhenCategoriaParameterProvided() {
        // Arrange
        String categoriaNombre = "deportes";
        List<Producto> expectedProducts = List.of(producto2);
        when(productosRepository.findByCategoriaContainsIgnoreCase(categoriaNombre)).thenReturn(expectedProducts);

        // Act
        List<Producto> actualProducts = productoService.findAll(null, categoriaNombre);

        // Assert
        assertIterableEquals(expectedProducts, actualProducts);

        // Verify
        verify(productosRepository, times(1)).findByCategoriaContainsIgnoreCase(categoriaNombre);
    }

    @Test
    void findAll_ShouldReturnProductsByMarcaAndCategoria_WhenBothParametersProvided() {
        // Arrange
        String marca = "nike";
        String categoriaNombre = "deportes";
        List<Producto> expectedProducts = List.of(producto2);
        when(productosRepository.findByMarcaContainsIgnoreCaseAndCategoriaIgnoreCase(marca, categoriaNombre)).thenReturn(expectedProducts);

        // Act
        List<Producto> actualProducts = productoService.findAll(marca, categoriaNombre);

        // Assert
        assertIterableEquals(expectedProducts, actualProducts);

        // Verify
        verify(productosRepository, times(1)).findByMarcaContainsIgnoreCaseAndCategoriaIgnoreCase(marca, categoriaNombre);
    }

    @Test
    void findById_ShouldReturnProduct_WhenValidIdProvided() {
        // Arrange
        Long id = 1L;
        Producto expectedProduct = producto1;
        when(productosRepository.findById(id)).thenReturn(Optional.of(expectedProduct));

        // Act
        Producto actualProduct = productoService.findById(id);

        // Assert
        assertEquals(expectedProduct, actualProduct);

        // Verify
        verify(productosRepository, times(1)).findById(id);
    }

    @Test
    void findById_ShouldThrowProductoNotFound_WhenInvalidIdProvided() {
        // Arrange
        Long id = 1L;
        when(productosRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        var res = assertThrows(ProductoNotFound.class, () -> productoService.findById(id));
        assertEquals("Producto con id " + id + " no encontrado", res.getMessage());

        // Verify
        verify(productosRepository, times(1)).findById(id);
    }

    @Test
    void findByUuid_ShouldReturnProduct_WhenValidUuidProvided() {
        // Arrange
        UUID expectedUuid = producto1.getUuid();
        Producto expectedProduct = producto1;
        when(productosRepository.findByUuid(expectedUuid)).thenReturn(Optional.of(expectedProduct));

        // Act
        Producto actualProduct = productoService.findbyUuid(expectedUuid.toString());

        // Assert
        assertEquals(expectedProduct, actualProduct);

        // Verify
        verify(productosRepository, times(1)).findByUuid(expectedUuid);
    }

    @Test
    void findByUuid_ShouldThrowProductoNotFound_WhenInvalidUuidProvided() {
        // Arrange
        String uuid = "98bb49a6-3ae5-4e50-a606-db397a8772bZ";

        // Act & Assert
        var res = assertThrows(ProductoBadUuid.class, () -> productoService.findbyUuid(uuid));
        assertEquals("UUID: " + uuid + " no válido o de formato incorrecto", res.getMessage());

        // Verify
        // verify(productosRepository, times(1)).findByUuid(UUID.fromString(uuid));
    }

    @Test
    void save_ShouldReturnSavedProduct_WhenValidProductCreateDtoProvided() {
        // Arrange
        ProductoCreateDto productoCreateDto = new ProductoCreateDto(
                "Marca1", "Categoria1", "Descripción1", 100.0, "http://placeimg.com/640/480/people", "OTROS", 5
        );
        Producto expectedProduct = new Producto(1L, "Marca1", "Categoria1", "Descripción1", 100.0, "http://placeimg.com/640/480/people", 5, LocalDateTime.now(), LocalDateTime.now(), UUID.randomUUID(), false, categoria);

        when(categoriaService.findByNombre(productoCreateDto.getCategoria())).thenReturn(categoria);
        when(productoMapper.toProduct(productoCreateDto, categoria)).thenReturn(expectedProduct);
        when(productosRepository.save(expectedProduct)).thenReturn(expectedProduct);

        // Act
        Producto actualProduct = productoService.save(productoCreateDto);

        // Assert
        assertEquals(expectedProduct, actualProduct);

        // Verify
        verify(categoriaService, times(1)).findByNombre(productoCreateDto.getCategoria());
        verify(productosRepository, times(1)).save(productoCaptor.capture());
        verify(productoMapper, times(1)).toProduct(productoCreateDto, categoria);
    }

    @Test
    void update_ShouldReturnUpdatedProduct_WhenValidIdAndProductUpdateDtoProvided() {
        // Arrange
        Long id = 1L;
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto("Marca1", "Categoria1", "Descripción1", 100.0, "http://placeimg.com/640/480/people", "OTROS", 5, false);
        Producto existingProduct = producto1;
        when(productosRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        when(categoriaService.findByNombre(productoUpdateDto.getCategoria())).thenReturn(categoria);
        when(productosRepository.save(existingProduct)).thenReturn(existingProduct);
        when(productoMapper.toProduct(productoUpdateDto, producto1, categoria)).thenReturn(existingProduct);

        // Act
        Producto actualProduct = productoService.update(id, productoUpdateDto);

        // Assert
        assertEquals(existingProduct, actualProduct);

        // Verify
        verify(productosRepository, times(1)).findById(id);
        verify(categoriaService, times(1)).findByNombre(productoUpdateDto.getCategoria());
        verify(productosRepository, times(1)).save(productoCaptor.capture());
        verify(productoMapper, times(1)).toProduct(productoUpdateDto, producto1, categoria);
    }

    @Test
    void update_ShouldThrowProductoNotFound_WhenInvalidIdProvided() {
        // Arrange
        Long id = 1L;
        ProductoUpdateDto productoUpdateDto = new ProductoUpdateDto("Marca1", "Categoria1", "Descripción1", 100.0, "http://placeimg.com/640/480/people", "OTROS", 5, false);
        when(productosRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        var res = assertThrows(ProductoNotFound.class, () -> productoService.update(id, productoUpdateDto));
        assertEquals("Producto con id " + id + " no encontrado", res.getMessage());

        // Verify
        verify(productosRepository, times(0)).save(any(Producto.class));
    }

    @Test
    void deleteById_ShouldDeleteProduct_WhenValidIdProvided() {
        // Arrange
        Long id = 1L;
        Producto existingProduct = producto1;
        when(productosRepository.findById(id)).thenReturn(Optional.of(existingProduct));

        // Act
        productoService.deleteById(id);

        // Assert
        verify(productosRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_ShouldThrowProductoNotFound_WhenInvalidIdProvided() {
        // Arrange
        Long id = 1L;
        when(productosRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        var res = assertThrows(ProductoNotFound.class, () -> productoService.deleteById(id));
        assertEquals("Producto con id " + id + " no encontrado", res.getMessage());

        // Verify
        verify(productosRepository, times(0)).deleteById(id);
    }
}

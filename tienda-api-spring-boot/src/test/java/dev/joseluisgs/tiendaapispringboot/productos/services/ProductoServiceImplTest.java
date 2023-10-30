package dev.joseluisgs.tiendaapispringboot.productos.services;

import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;
import dev.joseluisgs.tiendaapispringboot.categorias.services.CategoriasService;
import dev.joseluisgs.tiendaapispringboot.notifications.config.WebSocketConfig;
import dev.joseluisgs.tiendaapispringboot.notifications.config.WebSocketHandler;
import dev.joseluisgs.tiendaapispringboot.notifications.mapper.ProductoNotificationMapper;
import dev.joseluisgs.tiendaapispringboot.notifications.models.Notificacion;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoCreateDto;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoUpdateDto;
import dev.joseluisgs.tiendaapispringboot.productos.exceptions.ProductoBadUuid;
import dev.joseluisgs.tiendaapispringboot.productos.exceptions.ProductoNotFound;
import dev.joseluisgs.tiendaapispringboot.productos.mappers.ProductoMapper;
import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
import dev.joseluisgs.tiendaapispringboot.productos.repositories.ProductosRepository;
import dev.joseluisgs.tiendaapispringboot.storage.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    private final Producto producto1 = Producto.builder()
            .id(1L)
            .marca("Adidas")
            .modelo("Zapatillas")
            .descripcion("Zapatillas de deporte")
            .precio(100.0)
            .imagen("http://placeimg.com/640/480/people")
            .stock(5)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .isDeleted(false)
            .categoria(categoria)
            .build();
    private final Producto producto2 = Producto.builder()
            .id(2L)
            .marca("Nike")
            .modelo("Zapatillas")
            .descripcion("Zapatillas de deporte")
            .precio(100.0)
            .imagen("http://placeimg.com/640/480/people")
            .stock(5)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .isDeleted(false)
            .categoria(categoria)
            .build();
    WebSocketHandler webSocketHandlerMock = mock(WebSocketHandler.class);
    @Mock
    private ProductosRepository productosRepository;
    @Mock
    private StorageService storageService;
    @Mock
    private CategoriasService categoriaService;
    @Mock
    private ProductoMapper productoMapper;
    @Mock
    private WebSocketConfig webSocketConfig;
    @Mock
    private ProductoNotificationMapper productoNotificationMapper;
    @InjectMocks
    private ProductoServiceImpl productoService;
    @Captor // Captor de argumentos
    private ArgumentCaptor<Producto> productoCaptor;


    @BeforeEach
    void setUp() {
        productoService.setWebSocketService(webSocketHandlerMock);
    }

    @Test
    void findAll_ShouldReturnAllProducts_WhenNoParametersProvided() {
        // Arrange
        List<Producto> expectedProducts = Arrays.asList(producto1, producto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending()); // ejemplo de creación de un objeto Pageable
        Page<Producto> expectedPage = new PageImpl<>(expectedProducts);

        when(productosRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        // Act
        Page<Producto> actualPage = productoService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);

        // Assert
        assertAll("findAll",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage, actualPage)
        );

        // Verify
        verify(productosRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_ShouldReturnProductsByMarca_WhenMarcaParameterProvided() {
        // Arrange
        Optional<String> marca = Optional.of("nike");
        List<Producto> expectedProducts = List.of(producto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Producto> expectedPage = new PageImpl<>(expectedProducts);

        when(productosRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        // Act
        Page<Producto> actualPage = productoService.findAll(marca, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);

        // Assert
        assertAll("findAllWithMarca",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage, actualPage)
        );

        // Verify
        verify(productosRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_ShouldReturnProductsByCategoria_WhenCategoriaParameterProvided() {
        // Arrange
        Optional<String> categoriaNombre = Optional.of("deportes");
        List<Producto> expectedProducts = List.of(producto2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Producto> expectedPage = new PageImpl<>(expectedProducts);

        when(productosRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);


        // Act
        Page<Producto> actualPage = productoService.findAll(Optional.empty(), categoriaNombre, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);

        // Assert
        assertAll("findAllWithCategoria",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage, actualPage)
        );

        // Verify
        verify(productosRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_ShouldReturnProductsByMarcaAndCategoria_WhenBothParametersProvided() {
        // Arrange
        Optional<String> marca = Optional.of("nike");
        Optional<String> categoriaNombre = Optional.of("deportes");
        List<Producto> expectedProducts = List.of(producto2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Producto> expectedPage = new PageImpl<>(expectedProducts);

        when(productosRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        // Act
        Page<Producto> actualPage = productoService.findAll(marca, categoriaNombre, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);

        // Assert
        assertAll("findAllWithMarcaAndCategoria",
                () -> assertNotNull(actualPage),
                () -> assertFalse(actualPage.isEmpty()),
                () -> assertEquals(expectedPage, actualPage)
        );

        // Verify
        verify(productosRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
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
    void save_ShouldReturnSavedProduct_WhenValidProductCreateDtoProvided() throws IOException {
        // Arrange
        ProductoCreateDto productoCreateDto = ProductoCreateDto.builder()
                .marca("Marca1")
                .modelo("Categoria1")
                .descripcion("Descripción1")
                .precio(100.0)
                .imagen("http://placeimg.com/640/480/people")
                .categoria("OTROS")
                .stock(5)
                .build();


        Producto expectedProduct = Producto.builder()
                .id(1L)
                .marca("Marca1")
                .modelo("Categoria1")
                .descripcion("Descripción1")
                .precio(100.0)
                .imagen("http://placeimg.com/640/480/people")
                .stock(5)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .uuid(UUID.randomUUID())
                .isDeleted(false)
                .categoria(categoria)
                .build();


        when(categoriaService.findByNombre(productoCreateDto.getCategoria())).thenReturn(categoria);
        when(productoMapper.toProduct(productoCreateDto, categoria)).thenReturn(expectedProduct);
        when(productosRepository.save(expectedProduct)).thenReturn(expectedProduct);
        doNothing().when(webSocketHandlerMock).sendMessage(any());

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
    void update_ShouldReturnUpdatedProduct_WhenValidIdAndProductUpdateDtoProvided() throws IOException {
        // Arrange
        Long id = 1L;
        ProductoUpdateDto productoUpdateDto = ProductoUpdateDto.builder()
                .marca("Marca1")
                .modelo("Categoria1")
                .descripcion("Descripción1")
                .precio(100.0)
                .imagen("http://placeimg.com/640/480/people")
                .categoria("OTROS")
                .stock(5)
                .isDeleted(false)
                .build();

        Producto existingProduct = producto1;

        when(productosRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        when(categoriaService.findByNombre(productoUpdateDto.getCategoria())).thenReturn(categoria);
        when(productosRepository.save(existingProduct)).thenReturn(existingProduct);
        when(productoMapper.toProduct(productoUpdateDto, producto1, categoria)).thenReturn(existingProduct);
        doNothing().when(webSocketHandlerMock).sendMessage(any());

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
        ProductoUpdateDto productoUpdateDto = ProductoUpdateDto.builder()
                .marca("Marca1")
                .modelo("Categoria1")
                .descripcion("Descripción1")
                .precio(100.0)
                .imagen("http://placeimg.com/640/480/people")
                .categoria("OTROS")
                .stock(5)
                .isDeleted(false)
                .build();

        when(productosRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        var res = assertThrows(ProductoNotFound.class, () -> productoService.update(id, productoUpdateDto));
        assertEquals("Producto con id " + id + " no encontrado", res.getMessage());

        // Verify
        verify(productosRepository, times(0)).save(any(Producto.class));
    }

    @Test
    void deleteById_ShouldDeleteProduct_WhenValidIdProvided() throws IOException {
        // Arrange
        Long id = 1L;
        Producto existingProduct = producto1;
        when(productosRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        doNothing().when(webSocketHandlerMock).sendMessage(any());

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

    @Test
    void onChange_ShouldSendMessage_WhenValidDataProvided() throws IOException {
        // Arrange
        doNothing().when(webSocketHandlerMock).sendMessage(any(String.class));

        // Act
        productoService.onChange(Notificacion.Tipo.CREATE, any(Producto.class));
    }

    @Test
    void updateImage_ShouldUpdateImageAndReturnProduct_WhenValidIdAndImageProvided() throws IOException {
        // Arrange
        String imageUrl = "http://placeimg.com/640/480/people";

        MultipartFile multipartFile = mock(MultipartFile.class);

        when(productosRepository.findById(producto1.getId())).thenReturn(Optional.of(producto1));
        when(storageService.store(multipartFile)).thenReturn(imageUrl);
        when(productosRepository.save(any(Producto.class))).thenReturn(producto1);
        doNothing().when(webSocketHandlerMock).sendMessage(anyString());

        // Act
        Producto updatedProduct = productoService.updateImage(producto1.getId(), multipartFile, false);

        // Assert
        assertEquals(updatedProduct.getImagen(), imageUrl);
        verify(productosRepository, times(1)).save(any(Producto.class));
        verify(storageService, times(1)).delete(producto1.getImagen());
        verify(storageService, times(1)).store(multipartFile);
    }
}

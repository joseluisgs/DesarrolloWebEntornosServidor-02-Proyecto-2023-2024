package dev.joseluisgs.tiendaapispringboot.productos.repositories;

import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductosRepositoryImplTest {

    private final Producto producto1 = new Producto(
            1L, "Adidas", "Zapatillas", "Zapatillas de deporte",
            100.0, "http://placeimg.com/640/480/people", "OTROS", 5,
            LocalDateTime.now(), LocalDateTime.now(), UUID.fromString("80e559b5-83c5-4555-ba0b-bb9fddb6e96c")
    );
    private final Producto producto2 = new Producto(
            2L, "Nike", "Zapatillas", "Zapatillas de deporte",
            100.0, "http://placeimg.com/640/480/people", "DEPORTE", 5,
            LocalDateTime.now(), LocalDateTime.now(), UUID.fromString("542f0a0b-064b-4022-b528-3b59f8bae821")
    );
    private ProductosRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new ProductosRepositoryImpl();
        // Vamos a salvar dos productos
        repository.save(producto1);
        repository.save(producto2);
    }

    @Test
    void findAll() {
        // Act
        List<Producto> productos = repository.findAll();

        // Assert
        assertAll("findAll",
                () -> assertNotNull(productos),
                () -> assertEquals(2, productos.size())
        );
    }

    @Test
    void findAllByMarca() {
        // Act
        String marca = "Nike";
        List<Producto> productos = repository.findAllByMarca(marca);

        // Assert
        assertAll("findAllByMarca",
                () -> assertNotNull(productos),
                () -> assertEquals(1, productos.size()),
                () -> assertEquals(marca, productos.get(0).getMarca())
        );
    }

    @Test
    void findAllByCategoria() {
        // Act
        String categoria = "DEPORTE";
        List<Producto> productos = repository.findAllByCategoria(categoria);

        // Assert
        assertAll("findAllByCategoria",
                () -> assertNotNull(productos),
                () -> assertEquals(1, productos.size()),
                () -> assertEquals(categoria, productos.get(0).getCategoria())
        );
    }

    @Test
    void findAllByMarcaAndCategoria() {
        // Act
        String marca = "Nike";
        String categoria = "DEPORTE";
        List<Producto> productos = repository.findAllByMarcaAndCategoria(marca, categoria);

        // Assert
        assertAll("findAllByMarcaAndCategoria",
                () -> assertNotNull(productos),
                () -> assertEquals(1, productos.size()),
                () -> assertEquals(marca, productos.get(0).getMarca()),
                () -> assertEquals(categoria, productos.get(0).getCategoria())
        );
    }

    @Test
    void findById_existingId_returnsOptionalWithProducto() {
        // Act
        Long id = 1L;
        Optional<Producto> optionalProducto = repository.findById(id);

        // Assert
        assertAll("findById_existingId_returnsOptionalWithProducto",
                () -> assertNotNull(optionalProducto),
                () -> assertTrue(optionalProducto.isPresent()),
                () -> assertEquals(id, optionalProducto.get().getId())
        );
    }

    @Test
    void findById_nonExistingId_returnsEmptyOptional() {
        // Act
        Long id = 100L;
        Optional<Producto> optionalProducto = repository.findById(id);

        // Assert
        assertAll("findById_nonExistingId_returnsEmptyOptional",
                () -> assertNotNull(optionalProducto),
                () -> assertTrue(optionalProducto.isEmpty())
        );
    }

    @Test
    void findByUuid_existingUuid_returnsOptionalWithProducto() {
        // Act
        UUID uuid = UUID.fromString("542f0a0b-064b-4022-b528-3b59f8bae821");
        Optional<Producto> optionalProducto = repository.findByUuid(uuid);

        // Assert
        assertAll("findByUuid_existingUuid_returnsOptionalWithProducto",
                () -> assertNotNull(optionalProducto),
                () -> assertTrue(optionalProducto.isPresent()),
                () -> assertEquals(uuid, optionalProducto.get().getUuid())
        );
    }

    @Test
    void findByUuid_nonExistingUuid_returnsEmptyOptional() {
        // Act
        UUID uuid = UUID.fromString("98bb49a6-3ae5-4e50-a606-db397a8772b2");
        Optional<Producto> optionalProducto = repository.findByUuid(uuid);

        // Assert
        assertAll("findByUuid_nonExistingUuid_returnsEmptyOptional",
                () -> assertNotNull(optionalProducto),
                () -> assertTrue(optionalProducto.isEmpty())
        );
    }

    @Test
    void existsById_existingId_returnsTrue() {
        // Act
        Long id = 1L;
        boolean exists = repository.existsById(id);

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsById_nonExistingId_returnsFalse() {
        // Act
        Long id = 100L;
        boolean exists = repository.existsById(id);

        // Assert
        assertFalse(exists);
    }

    @Test
    void existsByUuid_existingUuid_returnsTrue() {
        // Act
        UUID uuid = UUID.fromString("542f0a0b-064b-4022-b528-3b59f8bae821");
        boolean exists = repository.existsByUuid(uuid);

        // Assert
        assertTrue(exists);
    }

    @Test
    void existsByUuid_nonExistingUuid_returnsFalse() {
        // Act
        UUID uuid = UUID.fromString("98bb49a6-3ae5-4e50-a606-db397a8772b2");
        boolean exists = repository.existsByUuid(uuid);

        // Assert
        assertFalse(exists);
    }

    @Test
    void saveButNotExists() {
        // Arrange
        Producto producto = new Producto(
                3L, "New Brand", "New Model", "New Description",
                200.0, "http://placeimg.com/640/480/people", "NEW", 5,
                LocalDateTime.now(), LocalDateTime.now(), UUID.randomUUID()
        );

        // Act
        Producto savedProducto = repository.save(producto);
        var all = repository.findAll();

        // Assert
        assertAll("save",
                () -> assertNotNull(savedProducto),
                () -> assertEquals(producto, savedProducto),
                () -> assertEquals(3, all.size())
        );
    }

    @Test
    void saveExists() {
        // Arrange
        Producto producto = new Producto(
                1L, "New Brand", "New Model", "New Description",
                200.0, "http://placeimg.com/640/480/people", "NEW", 5,
                LocalDateTime.now(), LocalDateTime.now(), UUID.randomUUID()
        );

        // Act
        Producto savedProducto = repository.save(producto);
        var all = repository.findAll();

        // Assert
        assertAll("save",
                () -> assertNotNull(savedProducto),
                () -> assertEquals(producto, savedProducto),
                () -> assertEquals(2, all.size())
        );
    }

    @Test
    void deleteById_existingId() {
        // Act
        Long id = 1L;
        repository.deleteById(id);
        var all = repository.findAll();

        // Assert
        assertAll("deleteById_existingId",
                () -> assertEquals(1, all.size()),
                () -> assertFalse(repository.existsById(id))
        );
    }

    @Test
    void deleteByUuid_existingUuid() {
        // Act
        UUID uuid = UUID.fromString("542f0a0b-064b-4022-b528-3b59f8bae821");
        repository.deleteByUuid(uuid);
        var all = repository.findAll();

        // Assert
        assertAll("deleteByUuid_existingUuid",
                () -> assertEquals(1, all.size()),
                () -> assertFalse(repository.existsByUuid(uuid))
        );
    }

    @Test
    void nextId() {
        // Act
        Long nextId = repository.nextId();
        var all = repository.findAll();

        // Assert
        assertAll("nextId",
                () -> assertEquals(3L, nextId),
                () -> assertEquals(2, all.size())
        );
    }
}

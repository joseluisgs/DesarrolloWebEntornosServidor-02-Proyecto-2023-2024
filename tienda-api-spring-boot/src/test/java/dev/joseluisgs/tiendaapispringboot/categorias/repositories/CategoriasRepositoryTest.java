package dev.joseluisgs.tiendaapispringboot.categorias.repositories;

import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class CategoriasRepositoryTest {
    private final Categoria categoria = new Categoria(null, "TEST", LocalDateTime.now(), LocalDateTime.now(), false);

    @Autowired
    private CategoriasRepository repository;
    @Autowired
    private TestEntityManager entityManager; // EntityManager para hacer las pruebas

    @BeforeEach
    void setUp() {
        // Vamos a salvar una categoria
        entityManager.merge(categoria);
        entityManager.flush();
    }

    @Test
    void findAll() {
        // Act
        List<Categoria> categorias = repository.findAll();

        // Assert
        assertAll("findAll",
                () -> assertNotNull(categorias),
                () -> assertFalse(categorias.isEmpty()),
                () -> assertFalse(categorias.isEmpty())
        );
    }

    @Test
    void findAllByNombre() {
        // Act
        List<Categoria> categorias = repository.findAllByNombreContainingIgnoreCase("TEST");

        // Assert
        assertAll("findAllByNombre",
                () -> assertNotNull(categorias),
                () -> assertFalse(categorias.isEmpty()),
                () -> assertEquals("TEST", categorias.get(0).getNombre())
        );
    }

    @Test
    void findByID() {
        // Act
        Categoria categoria = repository.findById(1L).orElse(null);

        // Assert
        assertAll("findById",
                () -> assertNotNull(categoria),
                () -> assertEquals("DEPORTES", categoria.getNombre())
        );
    }

    @Test
    void findByIdNotFound() {
        // Act
        Categoria categoria = repository.findById(100L).orElse(null);

        // Assert
        assertNull(categoria);
    }

    @Test
    void save() {
        // Act
        Categoria categoria = repository.save(new Categoria(null, "TEST2", LocalDateTime.now(), LocalDateTime.now(), false));

        // Assert
        assertAll("save",
                () -> assertNotNull(categoria),
                () -> assertEquals("TEST2", categoria.getNombre())
        );
    }

    @Test
    void update() {
        // Act
        var categoria = repository.findById(1L).orElse(null);
        Categoria categoriaActualizada = repository.save(new Categoria(categoria.getId(), "TEST3", LocalDateTime.now(), LocalDateTime.now(), false));

        // Assert
        assertAll("update",
                () -> assertNotNull(categoriaActualizada),
                () -> assertEquals("TEST3", categoriaActualizada.getNombre())
        );
    }

    @Test
    void delete() {
        // Act
        var categoria = repository.findById(1L).orElse(null);
        repository.delete(categoria);
        Categoria categoriaBorrada = repository.findById(1L).orElse(null);

        // Assert
        assertNull(categoriaBorrada);
    }

}
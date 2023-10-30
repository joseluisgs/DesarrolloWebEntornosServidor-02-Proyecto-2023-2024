package dev.joseluisgs.tiendaapispringboot.categorias.mappers;

import dev.joseluisgs.tiendaapispringboot.categorias.dto.CategoriaRequest;
import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoriasMapperTest {
    private final Categoria categoria = new Categoria(1L, "TEST", LocalDateTime.now(), LocalDateTime.now(), false);

    // Inyectamos el mapper
    private final CategoriasMapper categoriaMapper = new CategoriasMapper();

    private final CategoriaRequest categoriaRequest = new CategoriaRequest("TEST", false);

    @Test
    public void whenToCategoria_thenReturnCategoria() {
        Categoria mappedCategoria = categoriaMapper.toCategoria(categoriaRequest);

        assertAll("whenToCategoria_thenReturnCategoria",
                () -> assertEquals(categoriaRequest.getNombre(), mappedCategoria.getNombre()),
                () -> assertEquals(categoriaRequest.getIsDeleted(), mappedCategoria.getIsDeleted())
        );
    }

    @Test
    public void whenToCategoriaWithExistingCategoria_thenReturnUpdatedCategoria() {

        Categoria updatedCategoria = categoriaMapper.toCategoria(categoriaRequest);

        assertAll("whenToCategoriaWithExistingCategoria_thenReturnUpdatedCategoria",
                () -> assertEquals(categoriaRequest.getNombre(), updatedCategoria.getNombre()),
                () -> assertEquals(categoriaRequest.getIsDeleted(), updatedCategoria.getIsDeleted())
        );
    }
}

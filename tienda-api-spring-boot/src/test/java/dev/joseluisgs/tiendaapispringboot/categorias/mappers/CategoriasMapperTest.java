package dev.joseluisgs.tiendaapispringboot.categorias.mappers;

import dev.joseluisgs.tiendaapispringboot.categorias.dto.CategoriaDto;
import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoriasMapperTest {
    private final Categoria categoria = new Categoria(1L, "TEST", LocalDateTime.now(), LocalDateTime.now(), false);

    // Inyectamos el mapper
    private final CategoriasMapper categoriaMapper = new CategoriasMapper();

    private final CategoriaDto categoriaDto = new CategoriaDto("TEST", false);

    @Test
    public void whenToCategoria_thenReturnCategoria() {
        Categoria mappedCategoria = categoriaMapper.toCategoria(categoriaDto);

        assertAll("whenToCategoria_thenReturnCategoria",
                () -> assertEquals(categoriaDto.getNombre(), mappedCategoria.getNombre()),
                () -> assertEquals(categoriaDto.getIsDeleted(), mappedCategoria.getIsDeleted())
        );
    }

    @Test
    public void whenToCategoriaWithExistingCategoria_thenReturnUpdatedCategoria() {

        Categoria updatedCategoria = categoriaMapper.toCategoria(categoriaDto);

        assertAll("whenToCategoriaWithExistingCategoria_thenReturnUpdatedCategoria",
                () -> assertEquals(categoriaDto.getNombre(), updatedCategoria.getNombre()),
                () -> assertEquals(categoriaDto.getIsDeleted(), updatedCategoria.getIsDeleted())
        );
    }
}

package dev.joseluisgs.tiendaapispringboot.categorias.mappers;

import dev.joseluisgs.tiendaapispringboot.categorias.dto.CategoriaDto;
import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CategoriasMapper {
    public Categoria toCategoria(CategoriaDto dto) {
        return new Categoria(
                null,
                dto.getNombre(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                false
        );
    }

    public Categoria toCategoria(CategoriaDto dto, Categoria categoria) {
        return new Categoria(
                categoria.getId(),
                dto.getNombre() != null ? dto.getNombre() : categoria.getNombre(),
                categoria.getCreatedAt(),
                LocalDateTime.now(),
                dto.getIsDeleted() != null ? dto.getIsDeleted() : categoria.getIsDeleted()
        );
    }
}

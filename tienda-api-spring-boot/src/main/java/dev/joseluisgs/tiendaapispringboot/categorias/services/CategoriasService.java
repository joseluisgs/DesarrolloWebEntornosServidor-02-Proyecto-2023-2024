package dev.joseluisgs.tiendaapispringboot.categorias.services;

import dev.joseluisgs.tiendaapispringboot.categorias.dto.CategoriaDto;
import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoriasService {
    Page<Categoria> findAll(Optional<String> nombre, Optional<Boolean> isDeleted, Pageable pageable);

    Categoria findByNombre(String nombre);

    Categoria findById(Long id);

    Categoria save(CategoriaDto categoriaDto);

    Categoria update(Long id, CategoriaDto categoriaDto);

    void deleteById(Long id);
}

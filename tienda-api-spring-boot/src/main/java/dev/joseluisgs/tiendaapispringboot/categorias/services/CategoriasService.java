package dev.joseluisgs.tiendaapispringboot.categorias.services;

import dev.joseluisgs.tiendaapispringboot.categorias.dto.CategoriaRequest;
import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoriasService {
    Page<Categoria> findAll(Optional<String> nombre, Optional<Boolean> isDeleted, Pageable pageable);

    Categoria findByNombre(String nombre);

    Categoria findById(Long id);

    Categoria save(CategoriaRequest categoriaRequest);

    Categoria update(Long id, CategoriaRequest categoriaRequest);

    void deleteById(Long id);
}

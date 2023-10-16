package dev.joseluisgs.tiendaapispringboot.categorias.services;

import dev.joseluisgs.tiendaapispringboot.categorias.dto.CategoriaDto;
import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;

import java.util.List;

public interface CategoriasService {
    List<Categoria> findAll(String nombre);

    Categoria findById(Long id);

    Categoria save(CategoriaDto categoriaDto);

    Categoria update(Long id, CategoriaDto categoriaDto);

    void deleteById(Long id);
}

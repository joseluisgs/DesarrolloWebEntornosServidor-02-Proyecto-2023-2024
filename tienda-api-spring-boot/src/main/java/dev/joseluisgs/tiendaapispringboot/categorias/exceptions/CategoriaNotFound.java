package dev.joseluisgs.tiendaapispringboot.categorias.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción de producto no encontrado
 * Status 404
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoriaNotFound extends CategoriaException {
    public CategoriaNotFound(Long id) {
        super("Categoría con id " + id + " no encontrada");
    }

}

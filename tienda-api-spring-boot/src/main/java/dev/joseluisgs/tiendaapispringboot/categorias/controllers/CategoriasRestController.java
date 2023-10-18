package dev.joseluisgs.tiendaapispringboot.categorias.controllers;

import dev.joseluisgs.tiendaapispringboot.categorias.dto.CategoriaDto;
import dev.joseluisgs.tiendaapispringboot.categorias.exceptions.CategoriaConflict;
import dev.joseluisgs.tiendaapispringboot.categorias.exceptions.CategoriaNotFound;
import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;
import dev.joseluisgs.tiendaapispringboot.categorias.services.CategoriasService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.version}/categorias") // Es la ruta del controlador
@Slf4j
public class CategoriasRestController {
    // Repositorio de productos
    private final CategoriasService categoriasService;

    @Autowired
    public CategoriasRestController(CategoriasService categoriasService) {
        this.categoriasService = categoriasService;
    }

    /**
     * Obtiene todas las categorias
     *
     * @param nombre Nombre de la categoría
     * @return Lista de categorías
     */
    @GetMapping()
    public ResponseEntity<List<Categoria>> getAllCategories(
            @RequestParam(required = false) String nombre
    ) {
        log.info("Buscando todos las categorias con nombre: " + nombre);
        return ResponseEntity.ok(categoriasService.findAll(nombre));
    }

    /**
     * Obtiene una categoría por su id
     *
     * @param id del categoría, se pasa como parámetro de la URL /{id}
     * @return Categoria si existe
     * @throws CategoriaNotFound si no existe la categoría (404)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoryById(@PathVariable Long id) {
        log.info("Buscando producto por id: " + id);
        return ResponseEntity.ok(categoriasService.findById(id));
    }

    /**
     * Crear una categoria
     *
     * @param categoriaCreateDto a crear
     * @return Categoria creada
     * @throws HttpClientErrorException.BadRequest si la categoría no es correcta (400)
     */
    @PostMapping()
    public ResponseEntity<Categoria> createCategory(@Valid @RequestBody CategoriaDto categoriaCreateDto) {
        log.info("Creando categegoría: " + categoriaCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriasService.save(categoriaCreateDto));
    }

    /**
     * Actualizar una categoría
     *
     * @param id                 de la categoría, se pasa como parámetro de la URL /{id}
     * @param categoriaUpdateDto a actualizar
     * @return Categoria actualizada
     * @throws CategoriaNotFound                   si no existe el producto (404)
     * @throws HttpClientErrorException.BadRequest si el producto no es correcto (400)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoriaDto categoriaUpdateDto) {
        log.info("Actualizando categoria por id: " + id + " con categoria: " + categoriaUpdateDto);
        return ResponseEntity.ok(categoriasService.update(id, categoriaUpdateDto));
    }


    /**
     * Borrar una categoría
     *
     * @param id del categoria, se pasa como parámetro de la URL /{id}
     * @return No Content si se ha borrado
     * @throws CategoriaNotFound si no existe el producto (404)
     * @throws CategoriaConflict si no se puede borrar porque tiene productos asociados (409)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("Borrando categoria por id: " + id);
        categoriasService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Manejador de excepciones de Validación: 400 Bad Request
     *
     * @param ex excepción
     * @return Mapa de errores de validación con el campo y el mensaje
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}

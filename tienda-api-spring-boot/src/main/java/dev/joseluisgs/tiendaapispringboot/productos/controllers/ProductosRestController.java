package dev.joseluisgs.tiendaapispringboot.productos.controllers;

import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoCreateDto;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoUpdateDto;
import dev.joseluisgs.tiendaapispringboot.productos.exceptions.ProductoBadRequest;
import dev.joseluisgs.tiendaapispringboot.productos.exceptions.ProductoNotFound;
import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
import dev.joseluisgs.tiendaapispringboot.productos.services.ProductosService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador de productos del tipo RestController
 * Fijamos la ruta de acceso a este controlador
 * Usamos el repositorio de productos y lo inyectamos en el constructor con Autowired
 *
 * @Autowired es una anotación que nos permite inyectar dependencias basadas  en las anotaciones @Controller, @Service, @Component, etc.
 * y que se encuentren en nuestro contenedor de Spring.
 */
@RestController // Es un controlador Rest

@RequestMapping("${api.version}/productos") // Es la ruta del controlador
public class ProductosRestController {
    // Repositorio de productos
    private final ProductosService productosService;
    private final Logger logger = LoggerFactory.getLogger(ProductosRestController.class);

    @Autowired
    public ProductosRestController(ProductosService productosService) {
        this.productosService = productosService;
    }

    /**
     * Obtiene todos los productos
     *
     * @param marca     Marca del producto
     * @param categoria Categoria del producto
     * @return Lista de productos
     */
    @GetMapping()
    public ResponseEntity<List<Producto>> getAllProducts(
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String categoria
    ) {
        logger.info("Buscando todos los productos con marca: " + marca + " y categoría: " + categoria);
        return ResponseEntity.ok(productosService.findAll(marca, categoria));
    }

    /**
     * Obtiene un producto por su id
     *
     * @param id del producto, se pasa como parámetro de la URL /{id}
     * @return Producto si existe
     * @throws ProductoNotFound si no existe el producto (404)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductById(@PathVariable Long id) {
        logger.info("Buscando producto por id: " + id);
        return ResponseEntity.ok(productosService.findById(id));
    }

    /**
     * Crear un producto
     *
     * @param productoCreateDto a crear
     * @return Producto creado
     * @throws ProductoBadRequest si el producto no es correcto (400)
     */
    @PostMapping()
    public ResponseEntity<Producto> createProduct(@Valid @RequestBody ProductoCreateDto productoCreateDto) {
        logger.info("Creando producto: " + productoCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productosService.save(productoCreateDto));
    }

    /**
     * Actualizar un producto
     *
     * @param id                del producto a actualizar, se pasa como parámetro de la URL /{id}
     * @param productoUpdateDto a actualizar
     * @return Producto actualizado
     * @throws ProductoNotFound   si no existe el producto (404)
     * @throws ProductoBadRequest si el producto no es correcto (400)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductoUpdateDto productoUpdateDto) {
        logger.info("Actualizando producto por id: " + id + " con producto: " + productoUpdateDto);
        return ResponseEntity.ok(productosService.update(id, productoUpdateDto));
    }

    /**
     * Actualizar un producto parcial
     *
     * @param id                del producto a actualizar, se pasa como parámetro de la URL /{id}
     * @param productoUpdateDto a actualizar
     * @return Producto actualizado
     * @throws ProductoNotFound   si no existe el producto (404)
     * @throws ProductoBadRequest si el producto no es correcto (400)
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Producto> updatePartialProduct(@PathVariable Long id, @Valid @RequestBody ProductoUpdateDto productoUpdateDto) {
        logger.info("Actualizando parcialmente producto por id: " + id + " con producto: " + productoUpdateDto);
        return ResponseEntity.ok(productosService.update(id, productoUpdateDto));
    }

    /**
     * Borrar un producto
     *
     * @param id del producto, se pasa como parámetro de la URL /{id}
     * @return No Content si se ha borrado
     * @throws ProductoNotFound si no existe el producto (404)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        logger.info("Borrando producto por id: " + id);
        productosService.deleteById(id);
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

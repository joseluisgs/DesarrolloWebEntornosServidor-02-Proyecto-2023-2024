package dev.joseluisgs.tiendaapispringboot.rest.productos.controllers;

import dev.joseluisgs.tiendaapispringboot.rest.productos.dto.ProductoCreateRequest;
import dev.joseluisgs.tiendaapispringboot.rest.productos.dto.ProductoResponse;
import dev.joseluisgs.tiendaapispringboot.rest.productos.dto.ProductoUpdateRequest;
import dev.joseluisgs.tiendaapispringboot.rest.productos.exceptions.ProductoNotFound;
import dev.joseluisgs.tiendaapispringboot.rest.productos.services.ProductosService;
import dev.joseluisgs.tiendaapispringboot.utils.pagination.PageResponse;
import dev.joseluisgs.tiendaapispringboot.utils.pagination.PaginationLinksUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador de productos del tipo RestController
 * Fijamos la ruta de acceso a este controlador
 * Usamos el repositorio de productos y lo inyectamos en el constructor con Autowired
 *
 * @Autowired es una anotación que nos permite inyectar dependencias basadas  en las anotaciones @Controller, @Service, @Component, etc.
 * y que se encuentren en nuestro contenedor de Spring.
 */
@RestController // Es un controlador Rest
@Slf4j
@RequestMapping("${api.version}/productos") // Es la ruta del controlador
// @PreAuthorize("hasRole('USER')") // Los productos puede verse por todo el mundo (por defecto)
public class ProductosRestController {
    // Repositorio de productos
    private final ProductosService productosService;
    private final PaginationLinksUtils paginationLinksUtils;


    @Autowired
    public ProductosRestController(ProductosService productosService, PaginationLinksUtils paginationLinksUtils) {
        this.productosService = productosService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    /**
     * Obtiene todos los productos
     *
     * @param marca     Marca del producto
     * @param categoria Categoria del producto
     * @param modelo    Modelo del producto
     * @param isDeleted Si está borrado o no
     * @param precioMax Precio máximo del producto
     * @param stockMin  Stock mínimo del producto
     * @return Pagina de productos
     */
    @GetMapping()
    public ResponseEntity<PageResponse<ProductoResponse>> getAllProducts(
            @RequestParam(required = false) Optional<String> marca,
            @RequestParam(required = false) Optional<String> categoria,
            @RequestParam(required = false) Optional<String> modelo,
            @RequestParam(required = false) Optional<Boolean> isDeleted,
            @RequestParam(required = false) Optional<Double> precioMax,
            @RequestParam(required = false) Optional<Double> stockMin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("Buscando todos los productos con las siguientes opciones: " + marca + " " + categoria + " " + modelo + " " + isDeleted + " " + precioMax + " " + stockMin);
        // Creamos el objeto de ordenación
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        // Creamos cómo va a ser la paginación
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<ProductoResponse> pageResult = productosService.findAll(marca, categoria, modelo, isDeleted, precioMax, stockMin, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    /**
     * Obtiene un producto por su id
     *
     * @param id del producto, se pasa como parámetro de la URL /{id}
     * @return Producto si existe
     * @throws ProductoNotFound si no existe el producto (404)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> getProductById(@PathVariable Long id) {
        log.info("Buscando producto por id: " + id);
        return ResponseEntity.ok(productosService.findById(id));
    }

    /**
     * Crear un producto
     *
     * @param productoCreateRequest a crear
     * @return Producto creado
     * @throws HttpClientErrorException.BadRequest si el producto no es correcto (400)
     */
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden acceder
    public ResponseEntity<ProductoResponse> createProduct(@Valid @RequestBody ProductoCreateRequest productoCreateRequest) {
        log.info("Creando producto: " + productoCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productosService.save(productoCreateRequest));
    }

    /**
     * Actualizar un producto
     *
     * @param id                    del producto a actualizar, se pasa como parámetro de la URL /{id}
     * @param productoUpdateRequest a actualizar
     * @return Producto actualizado
     * @throws ProductoNotFound                    si no existe el producto (404)
     * @throws HttpClientErrorException.BadRequest si el producto no es correcto (400)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden acceder
    public ResponseEntity<ProductoResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductoUpdateRequest productoUpdateRequest) {
        log.info("Actualizando producto por id: " + id + " con producto: " + productoUpdateRequest);
        return ResponseEntity.ok(productosService.update(id, productoUpdateRequest));
    }

    /**
     * Actualizar un producto parcial
     *
     * @param id                    del producto a actualizar, se pasa como parámetro de la URL /{id}
     * @param productoUpdateRequest a actualizar
     * @return Producto actualizado
     * @throws ProductoNotFound                    si no existe el producto (404)
     * @throws HttpClientErrorException.BadRequest si el producto no es correcto (400)
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden acceder
    public ResponseEntity<ProductoResponse> updatePartialProduct(@PathVariable Long id, @Valid @RequestBody ProductoUpdateRequest productoUpdateRequest) {
        log.info("Actualizando parcialmente producto por id: " + id + " con producto: " + productoUpdateRequest);
        return ResponseEntity.ok(productosService.update(id, productoUpdateRequest));
    }

    /**
     * Borrar un producto
     *
     * @param id del producto, se pasa como parámetro de la URL /{id}
     * @return No Content si se ha borrado
     * @throws ProductoNotFound si no existe el producto (404)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden acceder
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("Borrando producto por id: " + id);
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


    /**
     * Actualiza la imagen de un producto en el servidor /imagen/{id}
     * consumes = MediaType.MULTIPART_FORM_DATA_VALUE: Indica que el parámetro de la función es un parámetro del cuerpo de la petición HTTP
     *
     * @param id   Identificador del producto
     * @param file Fichero a subir
     * @return Producto actualizado
     * @throws ProductoNotFound                              Si no existe el producto
     * @throws HttpClientErrorException.BadRequest           Si no se ha enviado una imagen o esta está vacía
     * @throws HttpClientErrorException.UnsupportedMediaType Si no se ha enviado una imagen
     * @PathVariable: Indica que el parámetro de la función es un parámetro de la URL en este caso {id}
     * @RequestPart: Indica que el parámetro de la función es un parámetro del cuerpo de la petición HTTP
     */
    @PatchMapping(value = "/imagen/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden acceder
    public ResponseEntity<ProductoResponse> nuevoProducto(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file) {

        log.info("Actualizando imagen de producto por id: " + id);

        // Buscamos la raqueta
        if (!file.isEmpty()) {
            // Actualizamos el producto
            return ResponseEntity.ok(productosService.updateImage(id, file, true));

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha enviado una imagen para el producto o esta está vacía");
        }
    }
}

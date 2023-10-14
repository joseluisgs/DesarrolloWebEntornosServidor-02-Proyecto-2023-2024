package dev.joseluisgs.tiendaapispringboot.productos.controllers;

import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
import dev.joseluisgs.tiendaapispringboot.productos.repositories.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final ProductosRepository productosRepository;

    @Autowired
    public ProductosRestController(ProductosRepository productosRepository) {
        this.productosRepository = productosRepository;
    }

    /**
     * Obtiene todos los productos
     *
     * @param marca, si se quiere filtrar por marca se pasa como parámetro de consulta en la URL ?marca=valor y se filtra, no es obligatorio
     *               Si no se pasa, se devuelven todos los productos
     * @return Lista de productos
     */
    @GetMapping()
    public ResponseEntity<List<Producto>> getAllProducts(@RequestParam(required = false) String marca) {
        if (marca != null) {
            return ResponseEntity.ok(productosRepository.findAllByMarca(marca));
        } else {
            return ResponseEntity.ok(productosRepository.findAll());
        }
    }

    /**
     * Obtiene un producto por su id
     *
     * @param id del producto, se pasa como parámetro de la URL /{id}
     * @return Producto si existe, si no, Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductById(@PathVariable Long id) {
        return productosRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crear un producto
     *
     * @param producto a crear
     * @return Producto creado
     */
    @PostMapping()
    public ResponseEntity<Producto> createProduct(@RequestBody Producto producto) {
        var saved = productosRepository.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Actualizar un producto
     *
     * @param id       del producto a actualizar, se pasa como parámetro de la URL /{id}
     * @param producto a actualizar
     * @return Producto actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProduct(@PathVariable Long id, @RequestBody Producto producto) {
        // Lo buscamos, si existe lo actualizamos, si no devolvemos Not Found
        return productosRepository.findById(id)
                .map(p -> {
                    var updated = productosRepository.save(producto);
                    return ResponseEntity.status(HttpStatus.CREATED).body(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualizar un producto parcial
     *
     * @param id       del producto a actualizar, se pasa como parámetro de la URL /{id}
     * @param producto a actualizar
     * @return Producto actualizado
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Producto> updatePartialProduct(@PathVariable Long id, @RequestBody Producto producto) {
        // Lo buscamos, si existe lo actualizamos, si no devolvemos Not Found
        return productosRepository.findById(id)
                .map(p -> {
                    var updated = productosRepository.save(producto);
                    return ResponseEntity.status(HttpStatus.CREATED).body(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Borrar un producto
     *
     * @param id del producto, se pasa como parámetro de la URL /{id}
     * @return No Content si se ha borrado, si no, Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        // Lo buscamos, si existe lo borramos, si no devolvemos Not Found
        return productosRepository.findById(id)
                .map(p -> {
                    productosRepository.deleteById(id);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

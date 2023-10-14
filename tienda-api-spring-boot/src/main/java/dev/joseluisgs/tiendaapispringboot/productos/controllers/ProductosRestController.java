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

    @GetMapping()
    public ResponseEntity<List<Producto>> getAllProducts() {
        return ResponseEntity.ok(productosRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductById(@PathVariable Long id) {
        return productosRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Producto> createProduct(@RequestBody Producto producto) {
        var saved = productosRepository.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

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

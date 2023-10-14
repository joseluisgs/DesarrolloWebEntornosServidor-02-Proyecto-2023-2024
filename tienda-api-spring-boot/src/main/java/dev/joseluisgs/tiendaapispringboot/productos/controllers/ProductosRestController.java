package dev.joseluisgs.tiendaapispringboot.productos.controllers;

import dev.joseluisgs.tiendaapispringboot.productos.controllers.models.Producto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador de productos del tipo RestController
 * // Fijamos la ruta de acceso a este controlador
 */
@RestController // Es un controlador Rest
@RequestMapping("${api.version}/productos") // Es la ruta del controlador
public class ProductosRestController {

    @GetMapping()
    public ResponseEntity<List<Producto>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(List.of());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductById(@PathVariable Long id) {
        if (id == 2) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
            //return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping()
    public ResponseEntity<Producto> createProduct(@RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProduct(@PathVariable Long id, @RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.OK).body(producto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Producto> updatePartialProduct(@PathVariable Long id, @RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.OK).body(producto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

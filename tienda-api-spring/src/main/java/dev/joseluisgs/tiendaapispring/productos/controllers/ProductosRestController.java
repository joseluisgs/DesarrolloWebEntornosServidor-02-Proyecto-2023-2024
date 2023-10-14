package dev.joseluisgs.tiendaapispring.productos.controllers;

import org.springframework.web.bind.annotation.*;

/**
 * Controlador de productos del tipo RestController
 * // Fijamos la ruta de acceso a este controlador
 */
@RestController // Es un controlador Rest
@RequestMapping("/productos") // Es la ruta del controlador
public class ProductosRestController {

    @GetMapping()
    public String getAllProducts() {
        return "Hola desde getAllProducts()";
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable String id) {
        return "Hola desde getProductById() con id: " + id;
    }

    @PostMapping()
    public String createProduct() {
        return "Hola desde createProduct()";
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable String id) {
        return "Hola desde updateProduct() con id: " + id;
    }

    @PatchMapping("/{id}")
    public String updatePartialProduct(@PathVariable String id) {
        return "Hola desde updatePartialProduct() con id: " + id;
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable String id) {
        return "Hola desde deleteProduct() con id: " + id;
    }
}

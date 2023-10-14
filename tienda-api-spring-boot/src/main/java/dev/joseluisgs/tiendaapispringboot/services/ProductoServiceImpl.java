package dev.joseluisgs.tiendaapispringboot.services;

import dev.joseluisgs.tiendaapispringboot.exceptions.ProductoBadUuid;
import dev.joseluisgs.tiendaapispringboot.exceptions.ProductoNotFound;
import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
import dev.joseluisgs.tiendaapispringboot.productos.repositories.ProductosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductoServiceImpl implements ProductosService {
    private final Logger logger = LoggerFactory.getLogger(ProductoServiceImpl.class);
    private final ProductosRepository productosRepository;

    @Autowired
    public ProductoServiceImpl(ProductosRepository productosRepository) {
        this.productosRepository = productosRepository;
    }

    @Override
    public List<Producto> findAll(String marca, String categoria) {
        // Si todo está vacío o nulo, devolvemos todos los productos
        if ((marca == null || marca.isEmpty()) && (categoria == null || categoria.isEmpty())) {
            logger.info("Buscando todos los productos");
            return productosRepository.findAll();
        }
        // Si la marca no está vacía, pero la categoría si, buscamos por marca
        if ((marca != null && !marca.isEmpty()) && (categoria == null || categoria.isEmpty())) {
            logger.info("Buscando productos por marca: " + marca);
            return productosRepository.findAllByMarca(marca);
        }
        // Si la marca está vacía, pero la categoría no, buscamos por categoría
        if (marca == null || marca.isEmpty()) {
            logger.info("Buscando productos por categoría: " + categoria);
            return productosRepository.findAllByCategoria(categoria);
        }
        // Si la marca y la categoría no están vacías, buscamos por ambas
        logger.info("Buscando productos por marca: " + marca + " y categoría: " + categoria);
        return productosRepository.findAllByMarcaAndCategoria(marca, categoria);
    }

    @Override
    public Producto findById(Long id) {
        logger.info("Buscando producto por id: " + id);
        return productosRepository.findById(id).orElseThrow(() -> new ProductoNotFound(id));
    }

    @Override
    public Producto findbyUuid(String uuid) {
        logger.info("Buscando producto por uuid: " + uuid);
        try {
            var myUUID = UUID.fromString(uuid);
            return productosRepository.findByUuid(myUUID).orElseThrow(() -> new ProductoNotFound(myUUID));
        } catch (IllegalArgumentException e) {
            throw new ProductoBadUuid(uuid);
        }
    }

    @Override
    public Producto save(Producto producto) {
        logger.info("Guardando producto: " + producto);
        // obtenemos el id de producto
        Long id = productosRepository.nextId();
        // Creamos el producto nuevo con los datos que nos vienen
        Producto nuevoProducto = new Producto(id, producto.getMarca(), producto.getModelo(), producto.getDescripcion(), producto.getPrecio(), producto.getImagen(),
                producto.getCategoria(), producto.getStock(), LocalDateTime.now(), LocalDateTime.now(), UUID.randomUUID()
        );
        // Lo guardamos en el repositorio
        return productosRepository.save(nuevoProducto);
    }

    @Override
    public Producto update(Long id, Producto producto) {
        logger.info("Actualizando producto por id: " + id);
        // Si no existe lanza excepción, por eso ya llamamos a lo que hemos implementado antes
        var productoActual = this.findById(id);

        // Creamos el producto actualizado con los campos que nos llegan actualizando el updateAt y si son null no se actualizan y se quedan los anteriores
        Producto productoActualizado = new Producto(
                productoActual.getId(),
                producto.getMarca() != null ? producto.getMarca() : productoActual.getMarca(),
                producto.getModelo() != null ? producto.getModelo() : productoActual.getModelo(),
                producto.getDescripcion() != null ? producto.getDescripcion() : productoActual.getDescripcion(),
                producto.getPrecio() != null ? producto.getPrecio() : productoActual.getPrecio(),
                producto.getImagen() != null ? producto.getImagen() : productoActual.getImagen(),
                producto.getCategoria() != null ? producto.getCategoria() : productoActual.getCategoria(),
                producto.getStock() != null ? producto.getStock() : productoActual.getStock(),
                productoActual.getCreatedAt(),
                LocalDateTime.now(),
                productoActual.getUuid()
        );

        // Lo guardamos en el repositorio
        return productosRepository.save(productoActualizado);
    }

    @Override
    public void deleteById(Long id) {
        logger.debug("Borrando producto por id: " + id);
        // Si no existe lanza excepción, por eso ya llamamos a lo que hemos implementado antes
        this.findById(id);
        // Lo borramos del repositorio
        productosRepository.deleteById(id);

    }
}

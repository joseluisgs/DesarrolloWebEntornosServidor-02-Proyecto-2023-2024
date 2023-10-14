package dev.joseluisgs.tiendaapispringboot.services;

import dev.joseluisgs.tiendaapispringboot.dto.ProductoCreateDto;
import dev.joseluisgs.tiendaapispringboot.dto.ProductoUpdateDto;
import dev.joseluisgs.tiendaapispringboot.exceptions.ProductoBadUuid;
import dev.joseluisgs.tiendaapispringboot.exceptions.ProductoNotFound;
import dev.joseluisgs.tiendaapispringboot.mappers.ProductoMapper;
import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
import dev.joseluisgs.tiendaapispringboot.productos.repositories.ProductosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = {"productos"})
public class ProductoServiceImpl implements ProductosService {
    private final Logger logger = LoggerFactory.getLogger(ProductoServiceImpl.class);
    private final ProductosRepository productosRepository;
    private final ProductoMapper productoMapper;

    @Autowired
    public ProductoServiceImpl(ProductosRepository productosRepository, ProductoMapper productoMapper) {
        this.productosRepository = productosRepository;
        this.productoMapper = productoMapper;
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
    @Cacheable
    public Producto findById(Long id) {
        logger.info("Buscando producto por id: " + id);
        return productosRepository.findById(id).orElseThrow(() -> new ProductoNotFound(id));
    }

    @Override
    @Cacheable
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
    @CachePut
    public Producto save(ProductoCreateDto productoCreateDto) {
        logger.info("Guardando producto: " + productoCreateDto);
        // obtenemos el id de producto
        Long id = productosRepository.nextId();
        // Creamos el producto nuevo con los datos que nos vienen del dto, podríamos usar el mapper
        Producto nuevoProducto = productoMapper.toProduct(id, productoCreateDto);
        // Lo guardamos en el repositorio
        return productosRepository.save(nuevoProducto);
    }

    @Override
    @CachePut
    public Producto update(Long id, ProductoUpdateDto productoUpdateDto) {
        logger.info("Actualizando producto por id: " + id);
        // Si no existe lanza excepción, por eso ya llamamos a lo que hemos implementado antes
        var productoActual = this.findById(id);
        // Actualizamos el producto con los datos que nos vienen del dto, podríamos usar el mapper
        Producto productoActualizado = productoMapper.toProduct(productoUpdateDto, productoActual);
        // Lo guardamos en el repositorio
        return productosRepository.save(productoActualizado);
    }

    @Override
    @CacheEvict
    public void deleteById(Long id) {
        logger.debug("Borrando producto por id: " + id);
        // Si no existe lanza excepción, por eso ya llamamos a lo que hemos implementado antes
        this.findById(id);
        // Lo borramos del repositorio
        productosRepository.deleteById(id);

    }
}

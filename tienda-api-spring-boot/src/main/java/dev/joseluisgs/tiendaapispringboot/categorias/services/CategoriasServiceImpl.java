package dev.joseluisgs.tiendaapispringboot.categorias.services;

import dev.joseluisgs.tiendaapispringboot.categorias.dto.CategoriaDto;
import dev.joseluisgs.tiendaapispringboot.categorias.exceptions.CategoriaNotFound;
import dev.joseluisgs.tiendaapispringboot.categorias.mappers.CategoriasMapper;
import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;
import dev.joseluisgs.tiendaapispringboot.categorias.repositories.CategoriasRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = {"categorias"})
public class CategoriasServiceImpl implements CategoriasService {
    private final Logger logger = LoggerFactory.getLogger(CategoriasServiceImpl.class);
    private final CategoriasRepository categoriasRepository;
    private final CategoriasMapper categoriasMapper;

    @Autowired
    public CategoriasServiceImpl(CategoriasRepository categoriasRepository, CategoriasMapper categoriasMapper) {
        this.categoriasRepository = categoriasRepository;
        this.categoriasMapper = categoriasMapper;
    }

    @Override
    public List<Categoria> findAll(String nombre) {
        logger.info("Buscando categorías por nombre: " + nombre);
        if (nombre == null || nombre.isEmpty()) {
            return categoriasRepository.findAll();
        } else {
            return categoriasRepository.findAllByNombreContainingIgnoreCase(nombre);
        }
    }

    @Override
    public Categoria findByNombre(String nombre) {
        logger.info("Buscando categoría por nombre: " + nombre);
        return categoriasRepository.findByNombreEqualsIgnoreCase(nombre).orElseThrow(() -> new CategoriaNotFound(nombre));
    }


    @Override
    @Cacheable
    public Categoria findById(Long id) {
        logger.info("Buscando categoría por id: " + id);
        return categoriasRepository.findById(id).orElseThrow(() -> new CategoriaNotFound(id));
    }

    @Override
    @CachePut
    public Categoria save(CategoriaDto categoriaDto) {
        logger.info("Guardando categoría: " + categoriaDto);
        return categoriasRepository.save(categoriasMapper.toCategoria(categoriaDto));
    }

    @Override
    @CachePut
    public Categoria update(Long id, CategoriaDto categoriaDto) {
        logger.info("Actualizando categoría: " + categoriaDto);
        Categoria categoriaActual = findById(id);
        // Actualizamos los datos
        return categoriasRepository.save(categoriasMapper.toCategoria(categoriaDto, categoriaActual));
    }

    @Override
    @CacheEvict
    @Transactional // Para que se haga todo o nada y no se quede a medias (por el update)
    public void deleteById(Long id) {
        logger.info("Borrando categoría por id: " + id);
        Categoria categoria = findById(id);
        //categoriasRepository.deleteById(id);
        // O lo marcamos como borrado, para evitar problemas de cascada, no podemos borrar categorías con productos!!!
        // La otra forma es que comprobaramos si hay productos para borrarlos antes
        categoriasRepository.updateIsDeletedToTrueById(id);

    }
}

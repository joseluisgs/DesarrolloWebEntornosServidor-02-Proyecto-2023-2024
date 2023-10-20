package dev.joseluisgs.tiendaapispringboot.productos.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;
import dev.joseluisgs.tiendaapispringboot.categorias.services.CategoriasService;
import dev.joseluisgs.tiendaapispringboot.notifications.config.WebSocketConfig;
import dev.joseluisgs.tiendaapispringboot.notifications.config.WebSocketHandler;
import dev.joseluisgs.tiendaapispringboot.notifications.dto.ProductoNotificationDto;
import dev.joseluisgs.tiendaapispringboot.notifications.mapper.ProductoNotificationMapper;
import dev.joseluisgs.tiendaapispringboot.notifications.models.Notificacion;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoCreateDto;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoUpdateDto;
import dev.joseluisgs.tiendaapispringboot.productos.exceptions.ProductoBadUuid;
import dev.joseluisgs.tiendaapispringboot.productos.exceptions.ProductoNotFound;
import dev.joseluisgs.tiendaapispringboot.productos.mappers.ProductoMapper;
import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
import dev.joseluisgs.tiendaapispringboot.productos.repositories.ProductosRepository;
import dev.joseluisgs.tiendaapispringboot.storage.StorageService;
import jakarta.persistence.criteria.Join;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementación de nuestro servicio de productos
 * Aquí implementamos la lógica de negocio
 * Además es cacheable
 */
@Service
@CacheConfig(cacheNames = {"productos"})
@Slf4j
public class ProductoServiceImpl implements ProductosService {
    private final ProductosRepository productosRepository;
    private final CategoriasService categoriaService;
    private final ProductoMapper productosMapper;
    private final StorageService storageService;

    private final WebSocketConfig webSocketConfig;
    private final ObjectMapper mapper;
    private final ProductoNotificationMapper productoNotificationMapper;
    private WebSocketHandler webSocketService;

    @Autowired
    public ProductoServiceImpl(ProductosRepository productosRepository, CategoriasService categoriaService, ProductoMapper productoMapper, StorageService storageService, WebSocketConfig webSocketConfig, ProductoNotificationMapper productoNotificationMapper) {
        this.productosRepository = productosRepository;
        this.categoriaService = categoriaService;
        this.productosMapper = productoMapper;
        this.storageService = storageService;
        this.webSocketConfig = webSocketConfig;
        // Para enviar mensajes a los clientes ws normales
        webSocketService = webSocketConfig.webSocketRaquetasHandler();
        mapper = new ObjectMapper();
        this.productoNotificationMapper = productoNotificationMapper;
        // mapper.registerModule(new JavaTimeModule()); // Necesario para que funcione LocalDateTime
    }

    /**
     * Busca todos los productos
     *
     * @param marca     Marca del producto
     * @param categoria Categoría del producto
     * @param isDeleted Si está borrado o no
     * @param pageable  Paginación y ordenación
     * @return Lista de productos
     */
    @Override
    public Page<Producto> findAll(Optional<String> marca, Optional<String> categoria, Optional<Boolean> isDeleted, Pageable pageable) {
        // Criterio de búsqueda por marca
        Specification<Producto> specMarcaProducto = (root, query, criteriaBuilder) ->
                marca.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("marca")), "%" + m + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        // Criterio de búsqueda por categoría
        Specification<Producto> specCategoriaProducto = (root, query, criteriaBuilder) ->
                categoria.map(c -> {
                    Join<Producto, Categoria> categoriaJoin = root.join("categoria");
                    return criteriaBuilder.like(criteriaBuilder.lower(categoriaJoin.get("nombre")), "%" + c + "%");
                }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        // Criterio de búsqueda por isDeleted
        Specification<Producto> specIsDeleted = (root, query, criteriaBuilder) ->
                isDeleted.map(d -> criteriaBuilder.equal(root.get("isDeleted"), d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        // Combinamos las especificaciones
        Specification<Producto> criterio = Specification.where(specMarcaProducto)
                .and(specCategoriaProducto)
                .and(specIsDeleted);
        return productosRepository.findAll(criterio, pageable);
    }


    /**
     * Busca un producto por su id
     *
     * @param id Id del producto
     * @return Producto encontrado
     * @throws ProductoNotFound Si no lo encuentra
     */
    @Override
    @Cacheable
    public Producto findById(Long id) {
        log.info("Buscando producto por id: " + id);
        return productosRepository.findById(id).orElseThrow(() -> new ProductoNotFound(id));
    }

    /**
     * Busca un producto por su uuid
     *
     * @param uuid Uuid del producto en formato string
     * @return Producto encontrado
     * @throws ProductoNotFound Si no lo encuentra
     * @throws ProductoBadUuid  Si el uuid no es válido
     */
    @Override
    @Cacheable
    public Producto findbyUuid(String uuid) {
        log.info("Buscando producto por uuid: " + uuid);
        try {
            var myUUID = UUID.fromString(uuid);
            return productosRepository.findByUuid(myUUID).orElseThrow(() -> new ProductoNotFound(myUUID));
        } catch (IllegalArgumentException e) {
            throw new ProductoBadUuid(uuid);
        }
    }

    /**
     * Guarda un producto
     *
     * @param productoCreateDto Producto a guardar
     * @return Producto guardado
     */
    @Override
    @CachePut
    public Producto save(ProductoCreateDto productoCreateDto) {
        log.info("Guardando producto: " + productoCreateDto);
        // Buscamos la categoría por su nombre
        var categoria = categoriaService.findByNombre(productoCreateDto.getCategoria());
        // Creamos el producto nuevo con los datos que nos vienen del dto, podríamos usar el mapper
        // Lo guardamos en el repositorio
        var productoSaved = productosRepository.save(productosMapper.toProduct(productoCreateDto, categoria));
        // Enviamos la notificación a los clientes ws
        onChange(Notificacion.Tipo.CREATE, productoSaved);
        // Devolvemos el producto guardado
        return productoSaved;
    }

    /**
     * Actualiza un producto
     *
     * @param id                Id del producto a actualizar
     * @param productoUpdateDto Producto a actualizar
     * @return Producto actualizado
     * @throws ProductoNotFound Si no lo encuentra
     */
    @Override
    @CachePut
    public Producto update(Long id, ProductoUpdateDto productoUpdateDto) {
        log.info("Actualizando producto por id: " + id);
        // Si no existe lanza excepción, por eso ya llamamos a lo que hemos implementado antes
        var productoActual = this.findById(id);
        // Buscamos la categoría por su nombre
        // Si no tenemos categoría, no la actualizamos
        Categoria categoria = null;
        if (productoUpdateDto.getCategoria() != null && !productoUpdateDto.getCategoria().isEmpty()) {
            categoria = categoriaService.findByNombre(productoUpdateDto.getCategoria());
        } else {
            categoria = productoActual.getCategoria();
        }
        // Actualizamos el producto con los datos que nos vienen del dto, podríamos usar el mapper
        // Lo guardamos en el repositorio
        var productoUpdated = productosRepository.save(productosMapper.toProduct(productoUpdateDto, productoActual, categoria));
        // Enviamos la notificación a los clientes ws
        onChange(Notificacion.Tipo.UPDATE, productoUpdated);
        // Devolvemos el producto actualizado
        return productoUpdated;
    }

    /**
     * Borra un producto
     *
     * @param id Id del producto a borrar
     * @throws ProductoNotFound Si no lo encuentra
     */
    @Override
    @CacheEvict
    // @Transactional // Para que se haga todo o nada y no se quede a medias (por el update)
    public void deleteById(Long id) {
        log.debug("Borrando producto por id: " + id);
        // Si no existe lanza excepción, por eso ya llamamos a lo que hemos implementado antes
        var prod = this.findById(id);
        // Lo borramos del repositorio
        productosRepository.deleteById(id);
        // O lo marcamos como borrado, para evitar problemas de cascada, no podemos borrar productos en pedidos!!!
        //productosRepository.updateIsDeletedToTrueById(id);
        // Borramos la imagen del producto si existe y es distinta a la por defecto
        if (prod.getImagen() != null && !prod.getImagen().equals(Producto.IMAGE_DEFAULT)) {
            storageService.delete(prod.getImagen());
        }
        // Enviamos la notificación a los clientes ws
        onChange(Notificacion.Tipo.DELETE, prod);
    }

    /**
     * Actualiza la imagen de un producto
     *
     * @param id    Id del producto a actualizar
     * @param image Imagen a actualizar del producto en formato Multipart
     * @return Producto actualizado
     * @throws ProductoNotFound Si no lo encuentra
     */
    @Override
    @CachePut
    public Producto updateImage(Long id, MultipartFile image) {
        log.info("Actualizando imagen de producto por id: " + id);
        // Si no existe lanza excepción, por eso ya llamamos a lo que hemos implementado antes
        var productoActual = this.findById(id);
        // Borramos la imagen anterior si existe y no es la de por defecto
        if (productoActual.getImagen() != null && !productoActual.getImagen().equals(Producto.IMAGE_DEFAULT)) {
            storageService.delete(productoActual.getImagen());
        }
        String imageStored = storageService.store(image);
        String imageUrl = imageStored; //storageService.getUrl(imageStored); // Si quiero la url completa
        // Clonamos el producto con la nueva imagen, porque inmutabilidad de los objetos
        var productoActualizado = new Producto(
                productoActual.getId(),
                productoActual.getMarca(),
                productoActual.getModelo(),
                productoActual.getDescripcion(),
                productoActual.getPrecio(),
                imageUrl,
                productoActual.getStock(),
                productoActual.getCreatedAt(),
                LocalDateTime.now(),
                productoActual.getUuid(),
                productoActual.getIsDeleted(),
                productoActual.getCategoria()
        );
        // Lo guardamos en el repositorio
        var productoUpdated = productosRepository.save(productoActualizado);
        // Enviamos la notificación a los clientes ws
        onChange(Notificacion.Tipo.UPDATE, productoUpdated);
        // Devolvemos el producto actualizado
        return productoUpdated;
    }

    void onChange(Notificacion.Tipo tipo, Producto data) {
        log.debug("Servicio de productos onChange con tipo: " + tipo + " y datos: " + data);

        if (webSocketService == null) {
            log.warn("No se ha podido enviar la notificación a los clientes ws, no se ha encontrado el servicio");
            webSocketService = this.webSocketConfig.webSocketRaquetasHandler();
        }

        try {
            Notificacion<ProductoNotificationDto> notificacion = new Notificacion<>(
                    "PRODUCTOS",
                    tipo,
                    productoNotificationMapper.toProductNotificationDto(data),
                    LocalDateTime.now().toString()
            );

            String json = mapper.writeValueAsString((notificacion));

            log.info("Enviando mensaje a los clientes ws");
            // Enviamos el mensaje a los clientes ws con un hilo, si hay muchos clientes, puede tardar
            // no bloqueamos el hilo principal que atiende las peticiones http
            Thread senderThread = new Thread(() -> {
                try {
                    webSocketService.sendMessage(json);
                } catch (Exception e) {
                    log.error("Error al enviar el mensaje a través del servicio WebSocket", e);
                }
            });
            senderThread.start();
        } catch (JsonProcessingException e) {
            log.error("Error al convertir la notificación a JSON", e);
        }
    }

    // Para los test
    public void setWebSocketService(WebSocketHandler webSocketHandlerMock) {
        this.webSocketService = webSocketHandlerMock;
    }
}

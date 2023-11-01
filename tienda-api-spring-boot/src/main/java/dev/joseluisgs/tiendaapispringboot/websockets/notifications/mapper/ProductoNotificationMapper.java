package dev.joseluisgs.tiendaapispringboot.websockets.notifications.mapper;

import dev.joseluisgs.tiendaapispringboot.rest.productos.models.Producto;
import dev.joseluisgs.tiendaapispringboot.websockets.notifications.dto.ProductoNotificationResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductoNotificationMapper {
    public ProductoNotificationResponse toProductNotificationDto(Producto producto) {
        return new ProductoNotificationResponse(
                producto.getId(),
                producto.getMarca(),
                producto.getModelo(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getImagen(),
                producto.getStock(),
                producto.getCategoria().getNombre(),
                producto.getUuid().toString(),
                producto.getIsDeleted(),
                producto.getCreatedAt().toString(),
                producto.getUpdatedAt().toString()
        );
    }
}

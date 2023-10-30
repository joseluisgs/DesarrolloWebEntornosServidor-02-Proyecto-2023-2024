package dev.joseluisgs.tiendaapispringboot.notifications.mapper;

import dev.joseluisgs.tiendaapispringboot.notifications.dto.ProductoNotificationResponse;
import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
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

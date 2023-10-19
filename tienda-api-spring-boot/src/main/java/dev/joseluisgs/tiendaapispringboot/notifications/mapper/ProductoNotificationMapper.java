package dev.joseluisgs.tiendaapispringboot.notifications.mapper;

import dev.joseluisgs.tiendaapispringboot.notifications.dto.ProductoNotificationDto;
import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoNotificationMapper {
    public ProductoNotificationDto toProductNotificationDto(Producto producto) {
        return new ProductoNotificationDto(
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

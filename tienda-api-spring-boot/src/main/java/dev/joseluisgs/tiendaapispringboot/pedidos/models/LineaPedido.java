package dev.joseluisgs.tiendaapispringboot.pedidos.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true) // JPA Necesita un constructor vacío
public class LineaPedido {
    private final UUID id;
    @NotNull(message = "El id del pedido no puede ser nulo")
    private final UUID idPedido;
    @NotNull(message = "El id del producto no puede ser nulo")
    private final Long idProducto;
    @Min(value = 0, message = "El precio del producto no puede ser negativo")
    private final Double precioProducto;
    @Min(value = 1, message = "La cantidad del producto no puede ser negativa")
    private final Long cantidad = 1L;
    // No hace falta pasarlo, lo calculamos, pero si lo pasamos lo usamos
    private final Double total = 0.0;
}
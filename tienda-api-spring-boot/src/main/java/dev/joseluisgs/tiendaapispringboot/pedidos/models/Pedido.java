package dev.joseluisgs.tiendaapispringboot.pedidos.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true) // JPA Necesita un constructor vacío
public class Pedido {
    // Datos del pedido
    private final UUID id;
    @NotNull(message = "El id del usuario no puede ser nulo")
    private final Long idUsuario;
    @NotNull(message = "El id del cliente no puede ser nulo")
    private final Cliente cliente;
    @NotNull(message = "El pedido debe tener al menos una línea de pedido")
    private final List<LineaPedido> lineasPedido;
    // No hace falta pasarlo, lo calculamos, pero si lo pasamos lo usamos
    private final LocalDateTime createdAt = LocalDateTime.now();
    // No hace falta pasarlo, lo calculamos, pero si lo pasamos lo usamos
    private final LocalDateTime updatedAt = LocalDateTime.now();
    // No hace falta pasarlo, lo calculamos, pero si lo pasamos lo usamos
    private final Boolean isDeleted = false;
    // No hace falta pasarlo, lo calculamos, pero si lo pasamos lo usamos
    private Integer totalItems = 0;
    // No hace falta pasarlo, lo calculamos, pero si lo pasamos lo usamos
    private Double total = 0.0;

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }
}

package dev.joseluisgs.tiendaapispringboot.pedidos.models;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LineaPedido {
    @Min(value = 1, message = "La cantidad del producto no puede ser negativa")
    private Integer cantidad = 1;
    private Long idProducto;
    @Min(value = 0, message = "El precio del producto no puede ser negativo")
    private Double precioProducto = 0.0;
    // No hace falta pasarlo, lo calculamos, pero si lo pasamos lo usamos
    private Double total = 0.0;
}
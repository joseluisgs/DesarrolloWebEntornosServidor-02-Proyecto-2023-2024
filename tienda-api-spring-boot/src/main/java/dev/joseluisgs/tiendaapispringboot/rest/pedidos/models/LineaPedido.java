package dev.joseluisgs.tiendaapispringboot.rest.pedidos.models;

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
    @Builder.Default
    private Integer cantidad = 1;

    private Long idProducto;

    @Min(value = 0, message = "El precio del producto no puede ser negativo")
    @Builder.Default
    private Double precioProducto = 0.0;

    // No hace falta pasarlo, lo calculamos, pero si lo pasamos lo usamos
    @Builder.Default
    private Double total = 0.0;

    // Campos calculados
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
        this.total = this.cantidad * this.precioProducto;
    }

    public void setPrecioProducto(Double precioProducto) {
        this.precioProducto = precioProducto;
        this.total = this.cantidad * this.precioProducto;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
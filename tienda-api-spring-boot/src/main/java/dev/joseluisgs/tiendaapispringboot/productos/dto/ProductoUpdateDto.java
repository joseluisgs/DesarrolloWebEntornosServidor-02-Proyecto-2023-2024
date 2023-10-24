package dev.joseluisgs.tiendaapispringboot.productos.dto;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class ProductoUpdateDto {
    @Length(min = 3, message = "El nombre debe tener al menos 3 caracteres")
    private final String marca;
    private final String modelo;
    @Length(min = 3, message = "La descripción debe tener al menos 3 caracteres")
    private final String descripcion;
    @Min(value = 0, message = "El precio no puede ser negativo")
    private final Double precio;
    private final String imagen;
    private final String categoria;
    @Min(value = 0, message = "El stock no puede ser negativo")
    private final Integer stock;
    private final Boolean isDeleted;
}


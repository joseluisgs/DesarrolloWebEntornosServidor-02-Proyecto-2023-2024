package dev.joseluisgs.tiendaapispringboot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ProductoCreateDto {
    @NotBlank(message = "El nombre no puede estar vacío")
    @Length(min = 3, message = "El nombre debe tener al menos 3 caracteres")
    private final String marca;
    @NotBlank(message = "El modelo no puede estar vacío")
    private final String modelo;
    @NotBlank(message = "La descripción no puede estar vacía")
    @Length(min = 3, message = "La descripción debe tener al menos 3 caracteres")
    private final String descripcion;
    @Min(value = 0, message = "El precio no puede ser negativo")
    private final Double precio;
    private final String imagen;
    @NotBlank(message = "La categoría no puede estar vacía")
    private final String categoria;
    @Min(value = 0, message = "El stock no puede ser negativo")
    private final Integer stock;
}

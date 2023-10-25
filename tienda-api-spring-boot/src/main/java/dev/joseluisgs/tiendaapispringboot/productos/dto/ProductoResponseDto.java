package dev.joseluisgs.tiendaapispringboot.productos.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ProductoResponseDto {
    private final Long id;
    private final String marca;
    private final String modelo;
    private final String descripcion;
    private final Double precio;
    private final String imagen;
    private final String categoria;
    private final Integer stock;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final UUID uuid;
}

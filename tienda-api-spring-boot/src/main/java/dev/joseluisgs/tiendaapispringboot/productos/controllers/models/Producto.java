package dev.joseluisgs.tiendaapispringboot.productos.controllers.models;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Producto {
    private final Long id;
    private final String marca;
    private final String modelo;
    private final String descripcion;
    private final Double precio;
    private final String imagen;
    private final String categoria;
    private final Integer stock;
    private final LocalDateTime createdAt = LocalDateTime.now();
    private final LocalDateTime updatedAt = LocalDateTime.now();
    private final UUID uuid = UUID.randomUUID();

}

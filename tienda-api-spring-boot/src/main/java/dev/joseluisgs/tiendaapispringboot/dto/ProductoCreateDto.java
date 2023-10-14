package dev.joseluisgs.tiendaapispringboot.dto;

import lombok.Data;

@Data
public class ProductoCreateDto {
    private final String marca;
    private final String modelo;
    private final String descripcion;
    private final Double precio;
    private final String imagen;
    private final String categoria;
    private final Integer stock;
}

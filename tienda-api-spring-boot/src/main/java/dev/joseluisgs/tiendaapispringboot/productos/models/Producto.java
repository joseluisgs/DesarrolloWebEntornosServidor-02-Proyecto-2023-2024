package dev.joseluisgs.tiendaapispringboot.productos.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Producto {
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

    @JsonCreator
    public Producto(
            @JsonProperty("id") Long id,
            @JsonProperty("marca") String marca,
            @JsonProperty("modelo") String modelo,
            @JsonProperty("descripcion") String descripcion,
            @JsonProperty("precio") Double precio,
            @JsonProperty("imagen") String imagen,
            @JsonProperty("categoria") String categoria,
            @JsonProperty("stock") Integer stock
    ) {
        // Inicializar los atributos con los valores proporcionados
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
        this.categoria = categoria;
        this.stock = stock;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.uuid = UUID.randomUUID();
    }

}

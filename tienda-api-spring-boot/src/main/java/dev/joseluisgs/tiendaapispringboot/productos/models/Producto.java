package dev.joseluisgs.tiendaapispringboot.productos.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true) // JPA Necesita un constructor vacío
@Entity // Para que sea una entidad de JPA
@Table(name = "PRODUCTOS") // Para indicar la tabla de la BD, si no coge el nombre de la clase
public class Producto {
    @Id // Indicamos que es el ID de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Indicamos que es autoincremental y sobre todo por el script de datos
    private final Long id;
    @NotBlank(message = "El nombre no puede estar vacío")
    private final String marca;
    @NotBlank(message = "El modelo no puede estar vacío")
    private final String modelo;
    @NotBlank(message = "La descripción no puede estar vacía")
    @Length(min = 3, message = "La descripción debe tener al menos 3 caracteres")
    private final String descripcion;
    @Min(value = 0, message = "El precio no puede ser negativo")
    private final Double precio;
    @Column(columnDefinition = "TEXT default 'https://via.placeholder.com/150'") // Por defecto una imagen
    private final String imagen;
    @Min(value = 0, message = "El stock no puede ser negativo")
    private final Integer stock;
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private final LocalDateTime createdAt;
    @Column(updatable = true, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private final LocalDateTime updatedAt;
    @Column(unique = true, updatable = false, nullable = false)
    private final UUID uuid;
    @Column(columnDefinition = "boolean default false")
    private final Boolean isDeleted;
    // Relación con categoría, muchos productos pueden tener una categoría
    @ManyToOne
    @JoinColumn(name = "categoria_id") // Así la vamos a llamar en la BB.DD
    private Categoria categoria;


    // Esto lo ponemos para que Jackson sepa como crear el objeto (TEST)
    @JsonCreator
    public Producto(
            @JsonProperty("id") Long id,
            @JsonProperty("marca") String marca,
            @JsonProperty("modelo") String modelo,
            @JsonProperty("descripcion") String descripcion,
            @JsonProperty("precio") Double precio,
            @JsonProperty("imagen") String imagen,
            @JsonProperty("categoria") Categoria categoria,
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
        this.isDeleted = false;
    }

}

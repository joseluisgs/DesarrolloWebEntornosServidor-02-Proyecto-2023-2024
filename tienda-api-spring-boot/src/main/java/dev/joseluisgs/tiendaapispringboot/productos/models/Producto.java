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
    public static final String IMAGE_DEFAULT = "https://via.placeholder.com/150";
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
    @Column(columnDefinition = "TEXT default '" + IMAGE_DEFAULT + "'") // Por defecto una imagen
    private final String imagen;
    @Min(value = 0, message = "El stock no puede ser negativo")
    private final Integer stock;
    @Temporal(TemporalType.TIMESTAMP) // Indicamos que es un campo de tipo fecha y hora
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private final LocalDateTime createdAt;
    @Temporal(TemporalType.TIMESTAMP) // Indicamos que es un campo de tipo fecha y hora
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
    //Es para decirle a Jackson como crear los objetos y casar propiedades
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

    // Método para actualizar un producto
    public Producto updateProducto(Producto producto) {
        // Creamos el producto actualizado con los campos que nos llegan actualizando el updateAt y si son null no se actualizan y se quedan los anteriores
        return new Producto(
                this.id,
                producto.getMarca() != null ? producto.getMarca() : this.marca,
                producto.getModelo() != null ? producto.getModelo() : this.modelo,
                producto.getDescripcion() != null ? producto.getDescripcion() : this.descripcion,
                producto.getPrecio() != null ? producto.getPrecio() : this.precio,
                producto.getImagen() != null ? producto.getImagen() : this.imagen,
                producto.getCategoria() != null ? producto.getCategoria() : this.categoria,
                producto.getStock() != null ? producto.getStock() : this.stock
        );
    }

    public Producto updateStock(Integer stock) {
        // Creamos el producto actualizado con los campos que nos llegan actualizando el updateAt y si son null no se actualizan y se quedan los anteriores
        return new Producto(
                this.id,
                this.marca,
                this.modelo,
                this.descripcion,
                this.precio,
                this.imagen,
                stock,
                this.createdAt,
                LocalDateTime.now(),
                this.uuid,
                this.isDeleted,
                this.categoria
        );
    }
}

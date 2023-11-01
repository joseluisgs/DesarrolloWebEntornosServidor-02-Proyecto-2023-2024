package dev.joseluisgs.tiendaapispringboot.rest.productos.models;

import dev.joseluisgs.tiendaapispringboot.rest.categorias.models.Categoria;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true) // JPA Necesita un constructor vacío
@Builder
@Entity // Para que sea una entidad de JPA
@Table(name = "PRODUCTOS") // Para indicar la tabla de la BD, si no coge el nombre de la clase
@EntityListeners(AuditingEntityListener.class) // Para que sea auditada y se autorellene
public class Producto {
    public static final String IMAGE_DEFAULT = "https://via.placeholder.com/150";

    @Id // Indicamos que es el ID de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Indicamos que es autoincremental y sobre todo por el script de datos
    private Long id;
    @NotBlank(message = "El nombre no puede estar vacío")
    private String marca;
    @NotBlank(message = "El modelo no puede estar vacío")
    private String modelo;
    @NotBlank(message = "La descripción no puede estar vacía")
    @Length(min = 3, message = "La descripción debe tener al menos 3 caracteres")
    private String descripcion;
    @Min(value = 0, message = "El precio no puede ser negativo")
    @Column(columnDefinition = "double default 0.0")
    @Builder.Default
    private Double precio = 0.0;
    @Column(columnDefinition = "TEXT default '" + IMAGE_DEFAULT + "'") // Por defecto una imagen
    @Builder.Default
    private String imagen = IMAGE_DEFAULT;
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(columnDefinition = "integer default 0")
    @Builder.Default
    private Integer stock = 0;
    @Column(unique = true, updatable = false, nullable = false)
    @Builder.Default
    private UUID uuid = UUID.randomUUID();
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP) // Indicamos que es un campo de tipo fecha y hora
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP) // Indicamos que es un campo de tipo fecha y hora
    @Column(updatable = true, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    @Column(columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isDeleted = false;


    // Relación con categoría, muchos productos pueden tener una categoría
    @ManyToOne
    @JoinColumn(name = "categoria_id") // Así la vamos a llamar en la BB.DD
    private Categoria categoria;

}

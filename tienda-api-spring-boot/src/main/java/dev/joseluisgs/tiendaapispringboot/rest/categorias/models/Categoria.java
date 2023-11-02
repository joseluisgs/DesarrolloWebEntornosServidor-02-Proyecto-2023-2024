package dev.joseluisgs.tiendaapispringboot.rest.categorias.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
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
@Table(name = "CATEGORIAS") // Para indicar la tabla de la BD, si no coge el nombre de la clase
@EntityListeners(AuditingEntityListener.class) // Para que sea auditada y se autorellene
@Schema(name = "CATEGORIAS") // Para indicar el nombre de la tabla
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @ColumnDefault("random_uuid()") // Para que se genere un UUID por defecto, analizar el SGDB que se usa
    @Schema(description = "Identificador de la categoría", example = "1234-1234-1234-1234")
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column(unique = true, nullable = false)
    @Length(min = 3, message = "La categoría debe tener al menos 3 caracteres")
    @Schema(description = "Nombre de la categoría", example = "Informática")
    private String nombre;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP) // Indicamos que es un campo de tipo fecha y hora
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Schema(description = "Fecha de creación de la categoría", example = "2021-01-01T00:00:00.000000")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP) // Indicamos que es un campo de tipo fecha y hora
    @Column(updatable = true, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Schema(description = "Fecha de actualización de la categoría", example = "2021-01-01T00:00:00.000000")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(columnDefinition = "boolean default false")
    @Schema(description = "Indica si la categoría está eliminada", example = "false")
    @Builder.Default
    private Boolean isDeleted = false;

}
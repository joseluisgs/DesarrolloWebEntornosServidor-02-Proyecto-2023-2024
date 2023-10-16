package dev.joseluisgs.tiendaapispringboot.categorias.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true) // JPA Necesita un constructor vacío
@Entity // Para que sea una entidad de JPA
@Table(name = "CATEGORIAS") // Para indicar la tabla de la BD, si no coge el nombre de la clase
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @Column(unique = true, nullable = false)
    @Length(min = 3, message = "La categoría debe tener al menos 3 caracteres")
    private final String nombre;
    @Column(updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private final LocalDateTime createdAt;
    @Column(updatable = true, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private final LocalDateTime updatedAt;
    @Column(columnDefinition = "boolean default false")
    private final Boolean isDeleted;

}
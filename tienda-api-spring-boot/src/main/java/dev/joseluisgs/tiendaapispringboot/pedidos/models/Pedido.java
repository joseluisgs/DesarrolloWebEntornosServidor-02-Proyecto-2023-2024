package dev.joseluisgs.tiendaapispringboot.pedidos.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

// Nombre de la colección en MongoDB
@Document("pedidos")
// Para que sepa con qué clase recuperarlo al traerlo con MongoDB y aplicar polimorfismo
@TypeAlias("Pedido")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pedido {
    // No hace falta pasarlo, lo calculamos, pero si lo pasamos lo usamos
    private Boolean isDeleted = false;
    // No hace falta pasarlo, lo calculamos, pero si lo pasamos lo usamos
    private LocalDateTime createdAt = LocalDateTime.now();
    // No hace falta pasarlo, lo calculamos, pero si lo pasamos lo usamos
    private LocalDateTime updatedAt = LocalDateTime.now();
    @NotNull(message = "El id del usuario no puede ser nulo")
    private Long idUsuario;
    @NotNull(message = "El id del cliente no puede ser nulo")
    private Cliente cliente;
    @NotNull(message = "El pedido debe tener al menos una línea de pedido")
    private List<LineaPedido> lineasPedido;
    // No hace falta pasarlo, lo calculamos, pero si lo pasamos lo usamos
    private Integer totalItems = 0;
    // No hace falta pasarlo, lo calculamos, pero si lo pasamos lo usamos
    private Double total = 0.0;

    // Id de mongo
    @Id
    private ObjectId id;

    @JsonProperty("id")
    public String get_id() {
        return id.toHexString();
    }
}

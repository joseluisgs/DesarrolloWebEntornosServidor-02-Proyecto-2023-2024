package dev.joseluisgs.tiendaapispringboot.pedidos.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {
    @Length(min = 3, message = "El nombre debe tener al menos 3 caracteres")
    private String nombreCompleto;
    @Email(message = "El email debe ser válido")
    private String email;
    @NotBlank(message = "El teléfono no puede estar vacío")
    private String telefono;
    @NotNull(message = "La dirección no puede ser nula")
    private Direccion direccion;
}
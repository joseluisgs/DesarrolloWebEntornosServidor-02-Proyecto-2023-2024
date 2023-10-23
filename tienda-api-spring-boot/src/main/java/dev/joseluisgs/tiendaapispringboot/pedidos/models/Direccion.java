package dev.joseluisgs.tiendaapispringboot.pedidos.models;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record Direccion(
        @Length(min = 3, message = "La calle debe tener al menos 3 caracteres")
        String calle,
        @NotBlank(message = "El número no puede estar vacío")
        String numero,
        @Length(min = 3, message = "La ciudad debe tener al menos 3 caracteres")
        String ciudad,
        @Length(min = 3, message = "La provincia debe tener al menos 3 caracteres")
        String provincia,
        @Length(min = 3, message = "El país debe tener al menos 3 caracteres")
        String pais,
        @NotBlank(message = "El código postal no puede estar vacío")
        String codigoPostal
) {
}

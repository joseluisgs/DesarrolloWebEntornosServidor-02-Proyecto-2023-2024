package dev.joseluisgs.tiendaapispringboot.pedidos.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true) // JPA Necesita un constructor vacío
public class Direccion {
    @Length(min = 3, message = "La calle debe tener al menos 3 caracteres")
    private final String calle;
    @NotBlank(message = "El número no puede estar vacío")
    private final String numero;
    @Length(min = 3, message = "La ciudad debe tener al menos 3 caracteres")
    private final String ciudad;
    @Length(min = 3, message = "La provincia debe tener al menos 3 caracteres")
    private final String provincia;
    @Length(min = 3, message = "El país debe tener al menos 3 caracteres")
    private final String pais;
    @NotBlank(message = "El código postal no puede estar vacío")
    private final String codigoPostal;
}

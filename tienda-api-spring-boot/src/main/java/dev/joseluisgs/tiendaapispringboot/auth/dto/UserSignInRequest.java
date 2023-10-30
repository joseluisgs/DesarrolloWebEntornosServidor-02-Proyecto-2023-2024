package dev.joseluisgs.tiendaapispringboot.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignInRequest {
    @NotBlank(message = "nombre no puede estar vacío")
    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    private String email;
    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 5, message = "Password debe tener al menos 5 caracteres")
    private String password;
}

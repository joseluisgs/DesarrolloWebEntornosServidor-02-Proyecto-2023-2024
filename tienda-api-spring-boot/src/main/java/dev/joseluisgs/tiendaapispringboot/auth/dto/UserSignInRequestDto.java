package dev.joseluisgs.tiendaapispringboot.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignInRequestDto {

    @NotBlank(message = "nombre no puede estar vacío")
    private String nombre;
    @NotBlank(message = "apellidos no puede estar vacío")
    private String apellidos;
    @NotBlank(message = "Username no puede estar vacío")
    private String username;
    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    private String email;
    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 5, message = "Password debe tener al menos 5 caracteres")
    private String password;
    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 5, message = "Password de comprobación debe tener al menos 5 caracteres")
    private String passwordComprobacion;

}
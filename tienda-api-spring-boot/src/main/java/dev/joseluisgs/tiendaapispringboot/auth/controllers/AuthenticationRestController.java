package dev.joseluisgs.tiendaapispringboot.auth.controllers;

import dev.joseluisgs.tiendaapispringboot.auth.dto.JwtAuthResponse;
import dev.joseluisgs.tiendaapispringboot.auth.dto.UserResponse;
import dev.joseluisgs.tiendaapispringboot.auth.dto.UserSignInRequest;
import dev.joseluisgs.tiendaapispringboot.auth.dto.UserSignUpRequest;
import dev.joseluisgs.tiendaapispringboot.auth.models.User;
import dev.joseluisgs.tiendaapispringboot.auth.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("${api.version}/auth") // Es la ruta del controlador
public class AuthenticationRestController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationRestController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Registra un usuario
     *
     * @param request datos del usuario
     * @return Token de autenticación
     */
    @PostMapping("/signup")
    public ResponseEntity<JwtAuthResponse> signUp(@Valid @RequestBody UserSignUpRequest request) {
        log.info("Registrando usuario: {}", request);
        return ResponseEntity.ok(authenticationService.signUp(request));
    }

    /**
     * Inicia sesión de un usuario
     *
     * @param request datos del usuario
     * @return Token de autenticación
     */
    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResponse> signIn(@Valid @RequestBody UserSignInRequest request) {
        log.info("Iniciando sesión de usuario: {}", request);
        return ResponseEntity.ok(authenticationService.signIn(request));
    }

    /**
     * Obtiene el usuario actual
     *
     * @param user usuario autenticado
     * @return Datos del usuario
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal User user) {
        log.info("Obteniendo usuario");
        System.out.println(user.getAuthorities());
        return ResponseEntity.ok(UserResponse.builder()
                .id(user.getId())
                .nombre(user.getNombre())
                .apellidos(user.getApellidos())
                .username(user.getUsername())
                .email(user.getEmail())
                // Depende de como lo queramos devolver
                //.roles(user.getRoles().stream().map(Enum::name).collect(Collectors.joining(",")))
                .roles(user.getRoles().stream().map(Enum::name).toList().toString())
                .build()
        );
    }

    /**
     * Manejador de excepciones de Validación: 400 Bad Request
     *
     * @param ex excepción
     * @return Mapa de errores de validación con el campo y el mensaje
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
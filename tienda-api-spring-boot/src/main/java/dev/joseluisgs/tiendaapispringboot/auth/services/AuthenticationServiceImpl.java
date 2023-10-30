package dev.joseluisgs.tiendaapispringboot.auth.services;

import dev.joseluisgs.tiendaapispringboot.auth.dto.JwtAuthResponse;
import dev.joseluisgs.tiendaapispringboot.auth.dto.UserSignInRequest;
import dev.joseluisgs.tiendaapispringboot.auth.dto.UserSignUpRequest;
import dev.joseluisgs.tiendaapispringboot.auth.exceptions.UserDiferentePasswords;
import dev.joseluisgs.tiendaapispringboot.auth.exceptions.UserSingInInvalid;
import dev.joseluisgs.tiendaapispringboot.auth.exceptions.UserUserNameOrEmailExisten;
import dev.joseluisgs.tiendaapispringboot.auth.models.Role;
import dev.joseluisgs.tiendaapispringboot.auth.models.User;
import dev.joseluisgs.tiendaapispringboot.auth.repositories.UserRepository;
import dev.joseluisgs.tiendaapispringboot.utils.jwt.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementación de nuestro servicio de autenticación
 */
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                     JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registra un usuario
     *
     * @param request datos del usuario
     * @return Token de autenticación
     */
    @Override
    public JwtAuthResponse signUp(UserSignUpRequest request) {
        log.info("Creando usuario: {}", request);
        if (request.getPassword().contentEquals(request.getPasswordComprobacion())) {
            User user = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .nombre(request.getNombre())
                    .apellidos(request.getApellidos())
                    .roles(Stream.of(Role.USER).collect(Collectors.toList()))
                    .build();
            try {
                // Salvamos y devolvemos el token
                var userStored = userRepository.save(user);
                return JwtAuthResponse.builder().token(jwtService.generateToken(userStored)).build();
            } catch (DataIntegrityViolationException ex) {
                throw new UserUserNameOrEmailExisten("El usuario con username " + request.getUsername() + " o email " + request.getEmail() + " ya existe");
            }
        } else {
            throw new UserDiferentePasswords("Las contraseñas no coinciden");

        }
    }

    /**
     * Autentica un usuario
     *
     * @param request datos del usuario
     * @return Token de autenticación
     */
    @Override
    public JwtAuthResponse signIn(UserSignInRequest request) {
        log.info("Autenticando usuario: {}", request);
        // Autenticamos y devolvemos el token
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserSingInInvalid("Usuario o contraseña incorrectos"));
        var jwt = jwtService.generateToken(user);
        return JwtAuthResponse.builder().token(jwt).build();
    }
}
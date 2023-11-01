package dev.joseluisgs.tiendaapispringboot.users.controllers;

import dev.joseluisgs.tiendaapispringboot.users.dto.UserInfoResponse;
import dev.joseluisgs.tiendaapispringboot.users.dto.UserRequest;
import dev.joseluisgs.tiendaapispringboot.users.dto.UserResponse;
import dev.joseluisgs.tiendaapispringboot.users.exceptions.UserNameOrEmailExists;
import dev.joseluisgs.tiendaapispringboot.users.exceptions.UserNotFound;
import dev.joseluisgs.tiendaapispringboot.users.models.User;
import dev.joseluisgs.tiendaapispringboot.users.services.UsersService;
import dev.joseluisgs.tiendaapispringboot.utils.pagination.PageResponse;
import dev.joseluisgs.tiendaapispringboot.utils.pagination.PaginationLinksUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("${api.version}/users") // Es la ruta del controlador
@PreAuthorize("hasRole('ADMIN')") // Solo los usuarios pueden acceder
public class UsersRestController {
    private final UsersService usersService;
    private final PaginationLinksUtils paginationLinksUtils;

    @Autowired
    public UsersRestController(UsersService usersService, PaginationLinksUtils paginationLinksUtils) {
        this.usersService = usersService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    /**
     * Obtiene todos los usuarios
     *
     * @param username  username del usuario
     * @param email     email del usuario
     * @param isDeleted si está borrado o no
     * @param page      página
     * @param size      tamaño
     * @param sortBy    campo de ordenación
     * @param direction dirección de ordenación
     * @param request   petición
     * @return Respuesta con la página de usuarios
     */
    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> findAll(
            @RequestParam(required = false) Optional<String> username,
            @RequestParam(required = false) Optional<String> email,
            @RequestParam(required = false) Optional<Boolean> isDeleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        log.info("findAll: username: {}, email: {}, isDeleted: {}, page: {}, size: {}, sortBy: {}, direction: {}",
                username, email, isDeleted, page, size, sortBy, direction);
        // Creamos el objeto de ordenación
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        // Creamos cómo va a ser la paginación
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<UserResponse> pageResult = usersService.findAll(username, email, isDeleted, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    /**
     * Obtiene un usuario por su id
     *
     * @param id del usuario, se pasa como parámetro de la URL /{id}
     * @return Usuario si existe
     * @throws UserNotFound si no existe el usuario (404)
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserInfoResponse> findById(@PathVariable Long id) {
        log.info("findById: id: {}", id);
        return ResponseEntity.ok(usersService.findById(id));
    }

    /**
     * Crea un nuevo usuario
     *
     * @param userRequest usuario a crear
     * @return Usuario creado
     * @throws UserNameOrEmailExists               si el nombre de usuario o el email ya existen
     * @throws HttpClientErrorException.BadRequest si hay algún error de validación
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("save: userRequest: {}", userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.save(userRequest));
    }

    /**
     * Actualiza un usuario
     *
     * @param id          id del usuario
     * @param userRequest usuario a actualizar
     * @return Usuario actualizado
     * @throws UserNotFound                        si no existe el usuario (404)
     * @throws HttpClientErrorException.BadRequest si hay algún error de validación (400)
     * @throws UserNameOrEmailExists               si el nombre de usuario o el email ya existen (400)
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        log.info("update: id: {}, userRequest: {}", id, userRequest);
        return ResponseEntity.ok(usersService.update(id, userRequest));
    }

    /**
     * Borra un usuario
     *
     * @param id id del usuario
     * @return Respuesta vacía
     * @throws UserNotFound si no existe el usuario (404)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("delete: id: {}", id);
        usersService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene el usuario actual
     *
     * @param user usuario autenticado
     * @return Datos del usuario
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')") // Solo los usuarios pueden acceder
    public ResponseEntity<dev.joseluisgs.tiendaapispringboot.auth.dto.UserResponse> me(@AuthenticationPrincipal User user) {
        log.info("Obteniendo usuario");
        System.out.println(user.getAuthorities());
        return ResponseEntity.ok(dev.joseluisgs.tiendaapispringboot.auth.dto.UserResponse.builder()
                .id(user.getId())
                .nombre(user.getNombre())
                .apellidos(user.getApellidos())
                .username(user.getUsername())
                .email(user.getEmail())
                // Depende de como lo queramos devolver
                //.roles(user.getRoles().stream().map(Enum::name).collect(Collectors.joining(",")))
                .roles(user.getRoles())
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

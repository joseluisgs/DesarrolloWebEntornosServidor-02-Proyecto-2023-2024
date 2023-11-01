package dev.joseluisgs.tiendaapispringboot.users.services;

import dev.joseluisgs.tiendaapispringboot.users.dto.UserInfoResponse;
import dev.joseluisgs.tiendaapispringboot.users.dto.UserRequest;
import dev.joseluisgs.tiendaapispringboot.users.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

// Indicamos que es uns ervicio de detalles de usuario
// Es muy importante esta línea para decir que vamos a usar el servicio de usuarios Spring
// Otra forma de hacerlo es

/**
 * public interface UsersService {
 * UserDetailsService userDetailsService();
 * }
 * <p>
 * y lugeo usarlo aqui con implements UsersService
 */
public interface UsersService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username);

    Page<UserResponse> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isDeleted, Pageable pageable);

    UserInfoResponse findById(Long id);

    UserResponse save(UserRequest userRequest);

    UserResponse update(Long id, UserRequest userRequest);

    void deleteById(Long id);

}
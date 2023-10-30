package dev.joseluisgs.tiendaapispringboot.auth.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

// Indicamos que es uns ervicio de detalles de usuario
// Es muy importante esta línea para decir que vamos a usar el servicio de usuarios Spring
// Otra forma de hacerlo es

/**
 * public interface UserService {
 * UserDetailsService userDetailsService();
 * }
 * <p>
 * y lugeo usarlo aqui con implements UserService
 */
public interface UserService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username);

}
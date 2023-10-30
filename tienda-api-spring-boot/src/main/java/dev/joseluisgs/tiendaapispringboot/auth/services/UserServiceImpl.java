package dev.joseluisgs.tiendaapispringboot.auth.services;

import dev.joseluisgs.tiendaapispringboot.auth.exceptions.UserUsernameNotFound;
import dev.joseluisgs.tiendaapispringboot.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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
@Service("userDetailsService")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UserUsernameNotFound {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserUsernameNotFound("Usuario con username " + username + " no encontrado"));
    }

}
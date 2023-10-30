package dev.joseluisgs.tiendaapispringboot.auth.repositories;

import dev.joseluisgs.tiendaapispringboot.auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}
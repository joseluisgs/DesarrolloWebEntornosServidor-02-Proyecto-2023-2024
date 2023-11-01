package dev.joseluisgs.tiendaapispringboot.rest.auth.repositories;

import dev.joseluisgs.tiendaapispringboot.rest.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
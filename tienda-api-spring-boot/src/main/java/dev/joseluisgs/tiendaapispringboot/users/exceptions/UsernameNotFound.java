package dev.joseluisgs.tiendaapispringboot.users.exceptions;

import dev.joseluisgs.tiendaapispringboot.auth.exceptions.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsernameNotFound extends UserException {
    public UsernameNotFound(String message) {
        super(message);
    }
}
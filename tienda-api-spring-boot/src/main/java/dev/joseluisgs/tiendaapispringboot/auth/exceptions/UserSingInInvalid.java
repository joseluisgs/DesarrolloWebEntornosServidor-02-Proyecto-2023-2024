package dev.joseluisgs.tiendaapispringboot.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserSingInInvalid extends UserException {
    public UserSingInInvalid(String message) {
        super(message);
    }
}
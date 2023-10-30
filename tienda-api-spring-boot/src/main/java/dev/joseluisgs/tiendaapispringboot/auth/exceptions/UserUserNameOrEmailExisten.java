package dev.joseluisgs.tiendaapispringboot.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserUserNameOrEmailExisten extends UserException {
    public UserUserNameOrEmailExisten(String message) {
        super(message);
    }
}

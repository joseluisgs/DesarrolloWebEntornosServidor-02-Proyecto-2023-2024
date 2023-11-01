package dev.joseluisgs.tiendaapispringboot.rest.auth.services.authentication;

import dev.joseluisgs.tiendaapispringboot.rest.auth.dto.JwtAuthResponse;
import dev.joseluisgs.tiendaapispringboot.rest.auth.dto.UserSignInRequest;
import dev.joseluisgs.tiendaapispringboot.rest.auth.dto.UserSignUpRequest;

public interface AuthenticationService {
    JwtAuthResponse signUp(UserSignUpRequest request);

    JwtAuthResponse signIn(UserSignInRequest request);
}
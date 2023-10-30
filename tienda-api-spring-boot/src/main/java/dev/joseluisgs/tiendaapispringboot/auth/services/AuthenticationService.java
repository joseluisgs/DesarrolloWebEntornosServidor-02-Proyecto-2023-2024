package dev.joseluisgs.tiendaapispringboot.auth.services;

import dev.joseluisgs.tiendaapispringboot.auth.dto.JwtAuthResponse;
import dev.joseluisgs.tiendaapispringboot.auth.dto.UserSignInRequest;
import dev.joseluisgs.tiendaapispringboot.auth.dto.UserSignUpRequest;

public interface AuthenticationService {
    JwtAuthResponse signUp(UserSignUpRequest request);

    JwtAuthResponse signIn(UserSignInRequest request);
}
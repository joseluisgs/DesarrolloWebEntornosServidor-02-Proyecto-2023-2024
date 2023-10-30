package dev.joseluisgs.tiendaapispringboot.auth.services;

import dev.joseluisgs.tiendaapispringboot.auth.dto.JwtAuthResponse;
import dev.joseluisgs.tiendaapispringboot.auth.dto.UserSignInRequestDto;

public interface AuthenticationService {
    JwtAuthResponse signUp(UserSignInRequestDto request);

    JwtAuthResponse signIn(UserSignInRequestDto request);
}
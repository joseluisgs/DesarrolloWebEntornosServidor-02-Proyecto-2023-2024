package dev.joseluisgs.tiendaapispringboot.users.services;

import dev.joseluisgs.tiendaapispringboot.users.dto.UserInfoResponse;
import dev.joseluisgs.tiendaapispringboot.users.dto.UserRequest;
import dev.joseluisgs.tiendaapispringboot.users.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UsersService {

    Page<UserResponse> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isDeleted, Pageable pageable);

    UserInfoResponse findById(Long id);

    UserResponse save(UserRequest userRequest);

    UserResponse update(Long id, UserRequest userRequest);

    void deleteById(Long id);

}

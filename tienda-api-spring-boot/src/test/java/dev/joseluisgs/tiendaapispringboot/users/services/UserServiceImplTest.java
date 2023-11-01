package dev.joseluisgs.tiendaapispringboot.users.services;

import dev.joseluisgs.tiendaapispringboot.pedidos.repositories.PedidosRepository;
import dev.joseluisgs.tiendaapispringboot.users.dto.UserRequest;
import dev.joseluisgs.tiendaapispringboot.users.dto.UserResponse;
import dev.joseluisgs.tiendaapispringboot.users.exceptions.UserNameOrEmailExists;
import dev.joseluisgs.tiendaapispringboot.users.exceptions.UserNotFound;
import dev.joseluisgs.tiendaapispringboot.users.mappers.UsersMapper;
import dev.joseluisgs.tiendaapispringboot.users.models.User;
import dev.joseluisgs.tiendaapispringboot.users.repositories.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private final UserRequest userRequest = UserRequest.builder().username("test").email("test@test.com").build();
    private final User user = User.builder().id(99L).username("test").email("test@test.com").build();
    private final UserResponse userResponse = UserResponse.builder().username("test").email("test@test.com").build();
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private PedidosRepository pedidosRepository;
    @Mock
    private UsersMapper usersMapper;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testLoadUserByUsername_UserFound_ReturnsUserDetails() {
        // Arrange
        String username = "test";
        User user = new User();
        user.setUsername(username);
        when(usersRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userService.loadUserByUsername(username);

        // Assert
        assertAll(
                () -> assertNotNull(userDetails),
                () -> assertEquals(username, userDetails.getUsername())
        );

        // Verify
        verify(usersRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testLoadUserByUsername_UserNotFound_ThrowsUserNotFound() {
        // Arrange
        String username = "test";
        when(usersRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserNotFound.class, () -> userService.loadUserByUsername(username));
    }

    @Test
    public void testFindAll_NoFilters_ReturnsPageOfUsers() {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        Page<User> page = new PageImpl<>(users);
        when(usersRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(usersMapper.toUserResponse(any(User.class))).thenReturn(new UserResponse());

        // Act
        Page<UserResponse> result = userService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Pageable.unpaged());

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.getTotalElements())
        );

        // Verify
        verify(usersRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    public void testFindById() {
        // Arrange
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(usersMapper.toUserResponse(user)).thenReturn(userResponse);

        // Act
        UserResponse result = userService.findById(userId);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userResponse.getUsername(), result.getUsername()),
                () -> assertEquals(userResponse.getEmail(), result.getEmail())
        );

        // Verify
        verify(usersRepository, times(1)).findById(userId);
        verify(usersMapper, times(1)).toUserResponse(user);

    }

    @Test
    public void testFindById_UserNotFound_ThrowsUserNotFound() {
        // Arrange
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserNotFound.class, () -> userService.findById(userId));

        // Verify
        verify(usersRepository, times(1)).findById(userId);
    }

    @Test
    public void testSave_ValidUserRequest_ReturnsUserResponse() {
        // Arrange


        when(usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString())).thenReturn(Optional.empty());
        when(usersMapper.toUser(userRequest)).thenReturn(user);
        when(usersMapper.toUserResponse(user)).thenReturn(userResponse);
        when(usersRepository.save(user)).thenReturn(user);

        // Act
        UserResponse result = userService.save(userRequest);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userRequest.getUsername(), result.getUsername()),
                () -> assertEquals(userRequest.getEmail(), result.getEmail())
        );

        // Verify
        verify(usersRepository, times(1)).findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString());
        verify(usersMapper, times(1)).toUser(userRequest);
        verify(usersMapper, times(1)).toUserResponse(user);
        verify(usersRepository, times(1)).save(user);

    }

    @Test
    public void testSave_DuplicateUsernameOrEmail_ThrowsUserNameOrEmailExists() {
        // Arrange
        when(usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString())).thenReturn(Optional.of(new User()));

        // Act and Assert
        assertThrows(UserNameOrEmailExists.class, () -> userService.save(userRequest));
    }

    @Test
    public void testUpdate_ValidUserRequest_ReturnsUserResponse() {
        // Arrange
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString())).thenReturn(Optional.empty());
        when(usersMapper.toUser(userRequest)).thenReturn(user);
        when(usersMapper.toUserResponse(user)).thenReturn(userResponse);
        when(usersRepository.save(user)).thenReturn(user);

        // Act
        UserResponse result = userService.update(userId, userRequest);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userRequest.getUsername(), result.getUsername()),
                () -> assertEquals(userRequest.getEmail(), result.getEmail())
        );

        // Verify
        verify(usersRepository, times(1)).findById(userId);
        verify(usersRepository, times(1)).findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString());
        verify(usersMapper, times(1)).toUser(userRequest);
        verify(usersMapper, times(1)).toUserResponse(user);
        verify(usersRepository, times(1)).save(user);
    }

    @Test
    public void testUpdate_DuplicateUsernameOrEmail_ThrowsUserNameOrEmailExists() {
        // Arrange
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString())).thenReturn(Optional.of(user));

        // Act and Assert
        assertThrows(UserNameOrEmailExists.class, () -> userService.update(userId, userRequest));
    }

    @Test
    public void testUpdate_UserNotFound_ThrowsUserNotFound() {
        // Arrange
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserNotFound.class, () -> userService.update(userId, userRequest));
    }

    @Test
    public void testDeleteById_PhisicalDelete() {
        // Arrange
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pedidosRepository.existsByIdUsuario(userId)).thenReturn(false);

        // Act
        userService.deleteById(userId);

        // Assert
        assertThrows(UserNotFound.class, () -> userService.findById(userId));

        // Verify
        verify(usersRepository, times(1)).delete(user);
        verify(pedidosRepository, times(1)).existsByIdUsuario(userId);
    }

    @Test
    public void testDeleteById_LogicalDelete() {
        // Arrange
        Long userId = 1L;
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pedidosRepository.existsByIdUsuario(userId)).thenReturn(true);
        doNothing().when(usersRepository).updateIsDeletedToTrueById(userId);

        // Act
        userService.deleteById(userId);

        // Assert

        // Verify
        verify(usersRepository, times(1)).updateIsDeletedToTrueById(userId);
        verify(pedidosRepository, times(1)).existsByIdUsuario(userId);
    }

    @Test
    public void testDeleteByIdNotExists() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pedidosRepository.existsByIdUsuario(userId)).thenReturn(true);

        // Act
        userService.deleteById(userId);

        // Assert
        Assertions.assertAll(
                () -> assertThrows(UserNotFound.class, () -> userService.findById(userId)),
                () -> Assertions.assertFalse(usersRepository.existsById(userId))
        );
    }
}
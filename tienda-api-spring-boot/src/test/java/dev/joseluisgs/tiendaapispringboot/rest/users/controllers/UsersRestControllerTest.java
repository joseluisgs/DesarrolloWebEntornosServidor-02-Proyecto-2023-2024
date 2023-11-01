package dev.joseluisgs.tiendaapispringboot.rest.users.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.joseluisgs.tiendaapispringboot.rest.users.dto.UserInfoResponse;
import dev.joseluisgs.tiendaapispringboot.rest.users.dto.UserRequest;
import dev.joseluisgs.tiendaapispringboot.rest.users.dto.UserResponse;
import dev.joseluisgs.tiendaapispringboot.rest.users.models.User;
import dev.joseluisgs.tiendaapispringboot.rest.users.services.UsersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class) // Extensión de Mockito para usarlo
class UsersRestControllerTest {

    private final UserRequest userRequest = UserRequest.builder().username("test").email("test@test.com").build();
    private final User user = User.builder().id(99L).username("test").email("test@test.com").build();
    private final UserResponse userResponse = UserResponse.builder().username("test").email("test@test.com").build();
    private final UserInfoResponse userIResponse = UserInfoResponse.builder().username("test").email("test@test.com").build();

    private final String myEndpoint = "/v1/users";
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc; // Cliente MVC

    @MockBean
    private UsersService UsersService;


    @Autowired
    public UsersRestControllerTest(UsersService UsersService) {
        this.UsersService = UsersService;
        mapper.registerModule(new JavaTimeModule()); // Necesario para que funcione LocalDateTime
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void createUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    // Podemos usar el contexto de Spring para autenticar
    // o podemos usar un usuario mockeado
    // @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    @WithMockUser(username = "pepe", authorities = {"USER"})
        //@WithUserDetails(value = "admin")
        // porque tengo un usuario admin en el data.sql
    void me() throws Exception {
        // Localpoint
        var myLocalEndpoint = myEndpoint + "/me";
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }

    @Test
    @WithAnonymousUser
    void me_AnonymousUser() throws Exception {
        // Localpoint
        var myLocalEndpoint = myEndpoint + "/me";
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(403, response.getStatus());
    }
}
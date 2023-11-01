package dev.joseluisgs.tiendaapispringboot.users.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.joseluisgs.tiendaapispringboot.auth.dto.UserResponse;
import dev.joseluisgs.tiendaapispringboot.users.services.UsersService;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class) // Extensión de Mockito para usarlo
class UsersRestControllerTest {

    private final String myEndpoint = "/v1/users";
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc; // Cliente MVC

    @MockBean
    private UsersService usersService;


    @Autowired
    public UsersRestControllerTest(UsersService usersService) {
        this.usersService = usersService;
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
    // @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    //@WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    @WithUserDetails(value = "admin")
    void me() throws Exception {
        // Localpoint
        var myLocalEndpoint = myEndpoint + "/me";
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        UserResponse res = mapper.readValue(response.getContentAsString(), UserResponse.class);

        // Assert, compruebo user,
        // porque es lo que he puesto de en el método @PreAuthorize("hasRole('USER')")
        assertAll("me",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(res.getUsername(), "admin"),
                () -> assertTrue(res.getRoles().toString().contains("ADMIN")));
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
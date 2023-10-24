package dev.joseluisgs.tiendaapispringboot.pedidos.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.joseluisgs.tiendaapispringboot.pedidos.models.Pedido;
import dev.joseluisgs.tiendaapispringboot.pedidos.services.PedidosService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class) // Extensión de Mockito para usarlo
class PedidosRestControllerTest {
    private final String myEndpoint = "/v1/pedidos";
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc; // Cliente MVC
    @MockBean
    private PedidosService pedidosService;
    // Para testear los JSON
    @Autowired
    private JacksonTester<Pedido> jsonPedido;

    @Autowired
    public PedidosRestControllerTest(PedidosService pedidosService) {
        this.pedidosService = pedidosService;
        mapper.registerModule(new JavaTimeModule()); // Necesario para que funcione LocalDateTime
    }

    @Test
    void getAllPedidos() throws Exception {
        // Arrange
        when(pedidosService.findAll()).thenReturn(new ArrayList<>());

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus())
        );

        // Verify
        verify(pedidosService, times(1)).findAll();
    }

    @Test
    void getPedido() {
    }

    @Test
    void getPedidosByUsuario() {
    }

    @Test
    void createPedido() {
    }

    @Test
    void updatePedido() {
    }

    @Test
    void deletePedido() {
    }
}
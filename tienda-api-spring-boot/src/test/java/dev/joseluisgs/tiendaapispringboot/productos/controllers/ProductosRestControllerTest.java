package dev.joseluisgs.tiendaapispringboot.productos.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoCreateDto;
import dev.joseluisgs.tiendaapispringboot.productos.dto.ProductoUpdateDto;
import dev.joseluisgs.tiendaapispringboot.productos.exceptions.ProductoNotFound;
import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
import dev.joseluisgs.tiendaapispringboot.productos.services.ProductosService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ExtendWith(MockitoExtension.class) // Extensión de Mockito para usarlo
class ProductosRestControllerTest {
    private final String myEndpoint = "/v1/productos";

    private final Categoria categoria = new Categoria(1L, "DEPORTES", LocalDateTime.now(), LocalDateTime.now(), false);
    private final Producto producto1 = new Producto(
            1L, "Adidas", "Zapatillas", "Zapatillas de deporte",
            100.0, "http://placeimg.com/640/480/people", 5,
            LocalDateTime.now(), LocalDateTime.now(), UUID.fromString("80e559b5-83c5-4555-ba0b-bb9fddb6e96c"),
            false, categoria
    );
    private final Producto producto2 = new Producto(
            2L, "Nike", "Zapatillas", "Zapatillas de deporte",
            100.0, "http://placeimg.com/640/480/people", 5,
            LocalDateTime.now(), LocalDateTime.now(), UUID.fromString("542f0a0b-064b-4022-b528-3b59f8bae821"),
            false, categoria
    );
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc; // Cliente MVC
    @MockBean
    private ProductosService productosService;
    // Para testear los JSON
    @Autowired
    private JacksonTester<ProductoCreateDto> jsonProductoCreateDto;
    @Autowired
    private JacksonTester<ProductoUpdateDto> jsonProductoUpdateDto;

    @Autowired
    public ProductosRestControllerTest(ProductosService productosService) {
        this.productosService = productosService;
        mapper.registerModule(new JavaTimeModule()); // Necesario para que funcione LocalDateTime
    }

    @Test
    void getAllProducts() throws Exception {
        var productosList = List.of(producto1, producto2);

        // Arrange
        when(productosService.findAll(null, null)).thenReturn(productosList);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        List<Producto> res = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, Producto.class));

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(2, res.size()),
                () -> assertEquals(producto1, res.get(0)),
                () -> assertEquals(producto2, res.get(1))
        );

        // Verify
        verify(productosService, times(1)).findAll(null, null);


    }

    @Test
    void getAllProductsByMarca() throws Exception {
        var productosList = List.of(producto2);
        var localEndpoint = myEndpoint + "?marca=nike";

        // Arrange
        when(productosService.findAll(anyString(), isNull())).thenReturn(productosList);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        List<Producto> res = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, Producto.class));

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.size()),
                () -> assertEquals(producto2, res.get(0))
        );

        // Verify
        verify(productosService, times(1)).findAll(anyString(), isNull());
    }

    @Test
    void getAllProductsByCategoria() throws Exception {
        var productosList = List.of(producto2);
        var localEndpoint = myEndpoint + "?categoria=DEPORTE";

        // Arrange
        when(productosService.findAll(isNull(), anyString())).thenReturn(productosList);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        List<Producto> res = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, Producto.class));

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.size()),
                () -> assertEquals(producto2, res.get(0))
        );

        // Verify
        verify(productosService, times(1)).findAll(isNull(), anyString());
    }

    @Test
    void getAllProductsByMarcaAndCategoria() throws Exception {
        var productosList = List.of(producto2);
        var localEndpoint = myEndpoint + "?marca=nike&categoria=DEPORTE";

        // Arrange
        when(productosService.findAll(anyString(), anyString())).thenReturn(productosList);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(localEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        List<Producto> res = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, Producto.class));

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, res.size()),
                () -> assertEquals(producto2, res.get(0))
        );

        // Verify
        verify(productosService, times(1)).findAll(anyString(), anyString());
    }

    @Test
    void getProductById() throws Exception {
        var myLocalEndpoint = myEndpoint + "/1";

        // Arrange
        when(productosService.findById(anyLong())).thenReturn(producto1);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Producto res = mapper.readValue(response.getContentAsString(), Producto.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(producto1, res)
        );

        // Verify
        verify(productosService, times(1)).findById(anyLong());
    }

    @Test
    void getProductByIdNotFound() throws Exception {
        var myLocalEndpoint = myEndpoint + "/1";

        // Arrange
        when(productosService.findById(anyLong())).thenThrow(new ProductoNotFound(1L));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        get(myLocalEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        // Assert
        assertEquals(404, response.getStatus());

        // Verify
        verify(productosService, times(1)).findById(anyLong());
    }

    @Test
    void createProduct() throws Exception {
        var productoDto = new ProductoCreateDto(
                "Adidas", "Zapatillas", "Zapatillas de deporte",
                100.0, "http://placeimg.com/640/480/people", "OTROS", 5);

        // Arrange
        when(productosService.save(any(ProductoCreateDto.class))).thenReturn(producto1);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(jsonProductoCreateDto.write(productoDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Producto res = mapper.readValue(response.getContentAsString(), Producto.class);

        // Assert
        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(producto1, res)
        );

        // Verify
        verify(productosService, times(1)).save(any(ProductoCreateDto.class));
    }

    @Test
    void createProductWithBadRequest() throws Exception {
        var productoDto = new ProductoCreateDto(
                "Ad", "", "Zapatillas de deporte",
                -100.0, "http://placeimg.com/640/480/people", "OTROS", -5);


        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(jsonProductoCreateDto.write(productoDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // System.out.println(response.getContentAsString());

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("El precio no puede ser negativo")),
                () -> assertTrue(response.getContentAsString().contains("El nombre debe tener al menos 3 caracteres")),
                () -> assertTrue(response.getContentAsString().contains("El modelo no puede estar vacÃ\u00ADo")),
                () -> assertTrue(response.getContentAsString().contains("El stock no puede ser negativo"))
        );


    }

    @Test
    void updateProduct() throws Exception {
        var myLocalEndpoint = myEndpoint + "/1";
        var productoDto = new ProductoUpdateDto(
                "Adidas", "Zapatillas", "Zapatillas de deporte",
                100.0, "http://placeimg.com/640/480/people", "OTROS", 5, false);

        // Arrange
        when(productosService.update(anyLong(), any(ProductoUpdateDto.class))).thenReturn(producto1);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(jsonProductoUpdateDto.write(productoDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Producto res = mapper.readValue(response.getContentAsString(), Producto.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(producto1, res)
        );

        // Verify
        verify(productosService, times(1)).update(anyLong(), any(ProductoUpdateDto.class));
    }

    @Test
    void updateProductNotFound() throws Exception {
        var myLocalEndpoint = myEndpoint + "/1";
        var productoDto = new ProductoUpdateDto(
                "Adidas", "Zapatillas", "Zapatillas de deporte",
                100.0, "http://placeimg.com/640/480/people", "OTROS", 5, false);

        // Arrange
        when(productosService.update(anyLong(), any(ProductoUpdateDto.class))).thenThrow(new ProductoNotFound(1L));

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(jsonProductoUpdateDto.write(productoDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(404, response.getStatus());
    }

    @Test
    void updateProductWithBadRequest() throws Exception {
        var myLocalEndpoint = myEndpoint + "/1";
        var productoDto = new ProductoUpdateDto(
                "Ad", "", "Zapatillas de deporte",
                -100.0, "http://placeimg.com/640/480/people", "OTROS", -5, false);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        put(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(jsonProductoUpdateDto.write(productoDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        System.out.println(response.getContentAsString());

        // Assert
        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("El precio no puede ser negativo")),
                () -> assertTrue(response.getContentAsString().contains("El nombre debe tener al menos 3 caracteres")),
                () -> assertTrue(response.getContentAsString().contains("El stock no puede ser negativo"))
        );
    }

    @Test
    void updatePartialProduct() throws Exception {
        var myLocalEndpoint = myEndpoint + "/1";
        var productoDto = new ProductoUpdateDto(
                "Adidas", null, null,
                null, "http://placeimg.com/640/480/people", "OTROS", 5, false);

        // Arrange
        when(productosService.update(anyLong(), any(ProductoUpdateDto.class))).thenReturn(producto1);

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        patch(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(jsonProductoUpdateDto.write(productoDto).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Producto res = mapper.readValue(response.getContentAsString(), Producto.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(producto1, res)
        );

        // Verify
        verify(productosService, times(1)).update(anyLong(), any(ProductoUpdateDto.class));
    }

    @Test
    void deleteProduct() throws Exception {
        var myLocalEndpoint = myEndpoint + "/1";

        // Arrange
        doNothing().when(productosService).deleteById(anyLong());

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(204, response.getStatus())
        );

        // Verify
        verify(productosService, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteProductNotFound() throws Exception {
        var myLocalEndpoint = myEndpoint + "/1";

        // Arrange
        doThrow(new ProductoNotFound(1L)).when(productosService).deleteById(anyLong());

        // Consulto el endpoint
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myLocalEndpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(404, response.getStatus());

        // Verify
        verify(productosService, times(1)).deleteById(anyLong());
    }

    @Test
    void updateProductImage() throws Exception {
        var myLocalEndpoint = myEndpoint + "/imagen/1";

        // Arrange
        when(productosService.updateImage(anyLong(), any(MultipartFile.class))).thenReturn(producto1);

        // Crear un archivo simulado
        MockMultipartFile file = new MockMultipartFile(
                "file", // Nombre del parámetro del archivo en el controlador
                "filename.jpg", // Nombre del archivo
                MediaType.IMAGE_JPEG_VALUE, // Tipo de contenido del archivo
                "contenido del archivo".getBytes() // Contenido del archivo
        );

        // Crear una solicitud PATCH multipart con el fichero simulado
        MockHttpServletResponse response = mockMvc.perform(
                multipart(myLocalEndpoint)
                        .file(file)
                        .with(req -> {
                            req.setMethod("PATCH");
                            return req;
                        })
        ).andReturn().getResponse();


        Producto res = mapper.readValue(response.getContentAsString(), Producto.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(producto1, res)
        );

        // Verify
        verify(productosService, times(1)).updateImage(anyLong(), any(MultipartFile.class));
    }

}
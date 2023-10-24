package dev.joseluisgs.tiendaapispringboot.pedidos.services;

import dev.joseluisgs.tiendaapispringboot.pedidos.exceptions.PedidoNotFound;
import dev.joseluisgs.tiendaapispringboot.pedidos.models.Pedido;
import dev.joseluisgs.tiendaapispringboot.pedidos.repositories.PedidosRepository;
import dev.joseluisgs.tiendaapispringboot.productos.repositories.ProductosRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidosServiceImplTest {
    @Mock
    private PedidosRepository pedidosRepository;
    @Mock
    private ProductosRepository productosRepository;

    @InjectMocks
    private PedidosServiceImpl pedidosService;

    @Test
    void findAll_ReturnsPageOfPedidos() {
        // Arrange
        List<Pedido> pedidos = List.of(new Pedido(), new Pedido());
        Page<Pedido> expectedPage = new PageImpl<>(pedidos);
        Pageable pageable = PageRequest.of(0, 10);

        when(pedidosRepository.findAll(pageable)).thenReturn(expectedPage);

        // Act
        Page<Pedido> result = pedidosService.findAll(pageable);

        // Assert
        assertAll(
                () -> assertEquals(expectedPage, result),
                () -> assertEquals(expectedPage.getContent(), result.getContent()),
                () -> assertEquals(expectedPage.getTotalElements(), result.getTotalElements())
        );

        // Verify
        verify(pedidosRepository, times(1)).findAll(pageable);
    }

    @Test
    void testFindById() {
        // Arrange
        ObjectId idPedido = new ObjectId();
        Pedido expectedPedido = new Pedido();
        when(pedidosRepository.findById(idPedido)).thenReturn(Optional.of(expectedPedido));

        // Act
        Pedido resultPedido = pedidosService.findById(idPedido);

        // Assert
        assertEquals(expectedPedido, resultPedido);

        // Verify
        verify(pedidosRepository).findById(idPedido);
    }

    @Test
    void testFindById_ThrowsPedidoNotFound() {
        // Arrange
        ObjectId idPedido = new ObjectId();
        when(pedidosRepository.findById(idPedido)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PedidoNotFound.class, () -> pedidosService.findById(idPedido));

        // Verify
        verify(pedidosRepository).findById(idPedido);
    }

    @Test
    void testFindByIdUsuario() {
        // Arrange
        Long idUsuario = 1L;
        Pageable pageable = mock(Pageable.class);
        Page<Pedido> expectedPage = mock(Page.class);
        when(pedidosRepository.findByIdUsuario(idUsuario, pageable)).thenReturn(expectedPage);

        // Act
        Page<Pedido> resultPage = pedidosService.findByIdUsuario(idUsuario, pageable);

        // Assert
        assertEquals(expectedPage, resultPage);

        // Verify
        verify(pedidosRepository).findByIdUsuario(idUsuario, pageable);
    }

    @Test
    void testSave() {
        // Arrange
        Pedido pedido = new Pedido();
        pedido.setLineasPedido(new ArrayList<>());
        Pedido pedidoToSave = new Pedido();
        pedidoToSave.setLineasPedido(new ArrayList<>());
        when(pedidosRepository.save(any(Pedido.class))).thenReturn(pedidoToSave); // Utiliza any(Pedido.class) para cualquier instancia de Pedido

        // Act
        Pedido resultPedido = pedidosService.save(pedido);

        // Assert
        assertEquals(pedidoToSave, resultPedido);

        // Verify
        verify(pedidosRepository).save(any(Pedido.class));
    }

    @Test
    void testDelete() {
        // Arrange
        ObjectId idPedido = new ObjectId();
        Pedido pedidoToDelete = new Pedido();
        when(pedidosRepository.findById(idPedido)).thenReturn(Optional.of(pedidoToDelete));

        // Act
        pedidosService.delete(idPedido);

        // Assert


        // Verify
        verify(pedidosRepository).findById(idPedido);
        verify(pedidosRepository).deleteById(idPedido);
    }

    @Test
    void testDelete_ThrowsPedidoNotFound() {
        // Arrange
        ObjectId idPedido = new ObjectId();
        when(pedidosRepository.findById(idPedido)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PedidoNotFound.class, () -> pedidosService.delete(idPedido));

        // Verify
        verify(pedidosRepository).findById(idPedido);
        verify(pedidosRepository, never()).deleteById(idPedido);
    }

    @Test
    void testUpdate() {
        // Arrange
        ObjectId idPedido = new ObjectId();
        Pedido pedido = new Pedido();
        Pedido pedidoToUpdate = new Pedido();
        pedidoToUpdate.setLineasPedido(new ArrayList<>()); // Inicializar la lista de líneas de pedido
        when(pedidosRepository.findById(idPedido)).thenReturn(Optional.of(pedidoToUpdate));
        when(pedidosRepository.save(any(Pedido.class))).thenReturn(pedidoToUpdate);

        // Act
        Pedido resultPedido = pedidosService.update(idPedido, pedido);

        // Assert
        assertAll(
                () -> assertEquals(pedidoToUpdate, resultPedido),
                () -> assertEquals(pedidoToUpdate.getLineasPedido(), resultPedido.getLineasPedido()),
                () -> assertEquals(pedidoToUpdate.getLineasPedido().size(), resultPedido.getLineasPedido().size())
        );

        // Verify
        verify(pedidosRepository).findById(idPedido);
        verify(pedidosRepository).save(any(Pedido.class));
    }
}
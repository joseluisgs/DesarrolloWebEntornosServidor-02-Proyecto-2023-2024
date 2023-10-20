package dev.joseluisgs.tiendaapispringboot.categorias.services;

import dev.joseluisgs.tiendaapispringboot.categorias.dto.CategoriaDto;
import dev.joseluisgs.tiendaapispringboot.categorias.exceptions.CategoriaConflict;
import dev.joseluisgs.tiendaapispringboot.categorias.mappers.CategoriasMapper;
import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;
import dev.joseluisgs.tiendaapispringboot.categorias.repositories.CategoriasRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriasServiceImplTest {

    private final Categoria categoria = new Categoria(1L, "TEST", LocalDateTime.now(), LocalDateTime.now(), false);
    private final CategoriaDto categoriaDto = new CategoriaDto("TEST", false);
    @Mock
    private CategoriasRepository categoriasRepository;

    @Mock
    private CategoriasMapper categoriasMapper;

    @InjectMocks
    private CategoriasServiceImpl categoriasService;


    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10); // ejemplo de creación de un objeto Pageable
        // Arrange
        when(categoriasRepository.findAll()).thenReturn(List.of(categoria));


        // Act
        var res = categoriasService.findAll(Optional.empty(), Optional.empty(), pageable);

        // Assert
        assertAll("findAll",
                () -> assertNotNull(res),
                () -> assertFalse(res.isEmpty())
        );

        // Verify
        verify(categoriasRepository, times(1)).findAll();
    }

    @Test
    public void testFindByNombre() {
        // Arrange
        when(categoriasRepository.findByNombreEqualsIgnoreCase(any(String.class))).thenReturn(Optional.of(categoria));

        // Act
        var res = categoriasService.findByNombre("Test");

        // Assert
        assertAll("findByNombre",
                () -> assertNotNull(res),
                () -> assertEquals("TEST", res.getNombre())
        );

        // Verify
        verify(categoriasRepository, times(1)).findByNombreEqualsIgnoreCase(any(String.class));
    }


    @Test
    public void testFindById() {
        // Arrange
        when(categoriasRepository.findById(any(Long.class))).thenReturn(Optional.of(categoria));

        // Act
        var res = categoriasService.findById(1L);

        // Assert
        assertAll("findById",
                () -> assertNotNull(res),
                () -> assertEquals("TEST", res.getNombre())
        );

        // Verify
        verify(categoriasRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void testSave() {
        // Arrange
        when(categoriasRepository.findByNombreEqualsIgnoreCase(any(String.class))).thenReturn(Optional.empty());
        when(categoriasMapper.toCategoria(any(CategoriaDto.class))).thenReturn(categoria);
        when(categoriasRepository.save(any(Categoria.class))).thenReturn(categoria);

        // Act
        categoriasService.save(categoriaDto);

        // Assert
        assertAll("save",
                () -> assertNotNull(categoria),
                () -> assertEquals("TEST", categoria.getNombre())
        );

        // Verify
        verify(categoriasRepository, times(1)).findByNombreEqualsIgnoreCase(any(String.class));
        verify(categoriasRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    public void testSaveConflict() {
        // Arrange
        when(categoriasRepository.findByNombreEqualsIgnoreCase(any(String.class))).thenReturn(Optional.of(categoria));

        // Act
        var res = assertThrows(CategoriaConflict.class, () -> categoriasService.save(categoriaDto));

        // Assert
        assertAll("saveConflict",
                () -> assertNotNull(res),
                () -> assertEquals("Ya existe una categoría con el nombre TEST", res.getMessage())
        );

        // Verify
        verify(categoriasRepository, times(1)).findByNombreEqualsIgnoreCase(any(String.class));
        verify(categoriasRepository, times(0)).save(any(Categoria.class));
    }

    @Test
    public void testUpdate() {
        // Arrange
        when(categoriasRepository.findById(any(Long.class))).thenReturn(Optional.of(categoria));
        when(categoriasRepository.findByNombreEqualsIgnoreCase(any(String.class))).thenReturn(Optional.of(categoria));
        when(categoriasMapper.toCategoria(any(CategoriaDto.class), any(Categoria.class))).thenReturn(categoria);
        when(categoriasRepository.save(any(Categoria.class))).thenReturn(categoria);

        // Act
        categoriasService.update(1L, categoriaDto);

        // Assert
        assertAll("update",
                () -> assertNotNull(categoria),
                () -> assertEquals("TEST", categoria.getNombre())
        );


        // Verify
        verify(categoriasRepository, times(1)).findById(any(Long.class));
        verify(categoriasRepository, times(1)).findByNombreEqualsIgnoreCase(any(String.class));
        verify(categoriasRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    public void testUpdateConflict() {
        // Arrange
        when(categoriasRepository.findById(any(Long.class))).thenReturn(Optional.of(categoria));
        when(categoriasRepository.findByNombreEqualsIgnoreCase(any(String.class))).thenReturn(Optional.of(categoria));

        // Act, el id no debe ser igual, no se puede actualizar, porqe ya existe
        var res = assertThrows(CategoriaConflict.class, () -> categoriasService.update(2L, categoriaDto));

        // Assert
        assertAll("updateConflict",
                () -> assertNotNull(res),
                () -> assertEquals("Ya existe una categoría con el nombre TEST", res.getMessage())
        );

        // Verify
        verify(categoriasRepository, times(1)).findById(any(Long.class));
        verify(categoriasRepository, times(1)).findByNombreEqualsIgnoreCase(any(String.class));
        verify(categoriasRepository, times(0)).save(any(Categoria.class));
    }

    @Test
    public void testDeleteById() {
        // Arrange
        when(categoriasRepository.findById(any(Long.class))).thenReturn(Optional.of(categoria));
        when(categoriasRepository.existsProductoById(any(Long.class))).thenReturn(false);

        // Act
        categoriasService.deleteById(1L);

        // Assert
        assertAll("deleteById",
                () -> assertNotNull(categoria),
                () -> assertEquals("TEST", categoria.getNombre())
        );

        // Verify
        verify(categoriasRepository, times(1)).findById(any(Long.class));
        verify(categoriasRepository, times(1)).existsProductoById(any(Long.class));
        verify(categoriasRepository, times(1)).deleteById(any(Long.class));
    }
}
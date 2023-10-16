package dev.joseluisgs.tiendaapispringboot.categorias.repositories;

import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriasRepository extends JpaRepository<Categoria, Long> {
    // Encontrar por nombre
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);

    // Categorias activas
    Categoria findByNombreContainingIgnoreCaseAndIsDeletedFalse(String nombre);

    // Si está borrado
    List<Categoria> findByIsDeleted(Boolean isDeleted);

    // Actualizar el producto con isDeleted a true
    @Modifying // Para indicar que es una consulta de actualización
    @Query("UPDATE Categoria p SET p.isDeleted = true WHERE p.id = :id")
    // Consulta de actualización
    void updateIsDeletedToTrueById(Long id);

}
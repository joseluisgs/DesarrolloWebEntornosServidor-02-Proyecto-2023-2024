package dev.joseluisgs.tiendaapispringboot.categorias.repositories;

import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriasRepository extends JpaRepository<Categoria, Long> {
    // Encontrar por nombre exacto
    Optional<Categoria> findByNombreEqualsIgnoreCase(String nombre);

    // Encontrar por nombre exacto y no borrado
    Optional<Categoria> findByNombreEqualsIgnoreCaseAndIsDeletedFalse(String nombre);

    // Categorias por nombre
    List<Categoria> findAllByNombreContainingIgnoreCase(String nombre);

    // Categorias activas
    List<Categoria> findAllByNombreContainingIgnoreCaseAndIsDeletedFalse(String nombre);

    // Si están borradas
    List<Categoria> findByIsDeleted(Boolean isDeleted);

    // Actualizar el producto con isDeleted a true
    @Modifying // Para indicar que es una consulta de actualización
    @Query("UPDATE Categoria p SET p.isDeleted = true WHERE p.id = :id")
    // Consulta de actualización
    void updateIsDeletedToTrueById(Long id);

}
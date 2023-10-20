package dev.joseluisgs.tiendaapispringboot.categorias.repositories;

import dev.joseluisgs.tiendaapispringboot.categorias.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Creamos el repositorio extendéndolo de JPA, siguiendo DAO
 * Con ello ya tenemos las operaciones básicas de CRUD y Paginación
 * extiende de JpaSpecificationExecutor para tener las opciones de Specificación y busqueda con Criteria
 */
@Repository
public interface CategoriasRepository extends JpaRepository<Categoria, Long>, JpaSpecificationExecutor<Categoria> {
    // Encontrar por nombre exacto
    Optional<Categoria> findByNombreEqualsIgnoreCase(String nombre);

    // Categorias por nombre
    List<Categoria> findAllByNombreContainingIgnoreCase(String nombre);

    // Si están borradas
    List<Categoria> findByIsDeleted(Boolean isDeleted);

    // Actualizar el producto con isDeleted a true
    @Modifying // Para indicar que es una consulta de actualización
    @Query("UPDATE Categoria p SET p.isDeleted = true WHERE p.id = :id")
    // Consulta de actualización
    void updateIsDeletedToTrueById(Long id);

    // Obtienes si existe un producto con el id de la categoría
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Producto p WHERE p.categoria.id = :id")
    Boolean existsProductoById(Long id);

}
package dev.joseluisgs.tiendaapispringboot.productos.repositories;

import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductosRepository extends JpaRepository<Producto, Long> {
    // Otras consultas que voy a tener a parte de las básicas

    // Por categoría
    @Query("SELECT p FROM Producto p WHERE LOWER(p.categoria.nombre) LIKE %:categoria%")
    List<Producto> findByCategoriaContainsIgnoreCase(String categoria);

    // Paginadas por categoría
    @Query("SELECT p FROM Producto p WHERE LOWER(p.categoria.nombre) LIKE %:categoria%")
    Page<Producto> findByCategoriaContainsIgnoreCase(String categoria, Pageable pageable);

    // Por categoria y que deleted sea false
    @Query("SELECT p FROM Producto p WHERE LOWER(p.categoria.nombre) LIKE %:categoria% AND p.isDeleted = false")
    List<Producto> findByCategoriaContainsIgnoreCaseAndIsDeletedFalse(String categoria);

    // Paginadas por categoria y que deleted sea false
    @Query("SELECT p FROM Producto p WHERE LOWER(p.categoria.nombre) LIKE %:categoria% AND p.isDeleted = false")
    Page<Producto> findByCategoriaContainsIgnoreCaseAndIsDeletedFalse(String categoria, Pageable pageable);

    // Por marca
    List<Producto> findByMarcaContainsIgnoreCase(String marca);

    // Paginadas por marca
    Page<Producto> findByMarcaContainsIgnoreCase(String marca, Pageable pageable);

    // Por marca y que deleted sea false
    List<Producto> findByMarcaContainsIgnoreCaseAndIsDeletedFalse(String marca);

    // Paginadas por marca y que deleted sea false
    Page<Producto> findByMarcaContainsIgnoreCaseAndIsDeletedFalse(String marca, Pageable pageable);

    // Por marca y categoría
    @Query("SELECT p FROM Producto p WHERE LOWER(p.marca) LIKE %:marca% AND LOWER(p.categoria.nombre) LIKE %:categoria%")
    List<Producto> findByMarcaContainsIgnoreCaseAndCategoriaIgnoreCase(String marca, String categoria);

    // Paginadas por marca y categoría
    @Query("SELECT p FROM Producto p WHERE LOWER(p.marca) LIKE %:marca% AND LOWER(p.categoria.nombre) LIKE %:categoria%")
    Page<Producto> findByMarcaContainsIgnoreCaseAndCategoriaIgnoreCase(String marca, String categoria, Pageable pageable);

    // Por marca y categoría y deleted false
    @Query("SELECT p FROM Producto p WHERE LOWER(p.marca) LIKE %:marca% AND LOWER(p.categoria.nombre) LIKE %:categoria% AND p.isDeleted = false")
    List<Producto> findByMarcaContainsIgnoreCaseAndCategoriaIgnoreCaseAndIsDeletedFalse(String marca, String categoria);

    // Paginadas por marca y categoría y deleted false
    @Query("SELECT p FROM Producto p WHERE LOWER(p.marca) LIKE %:marca% AND LOWER(p.categoria.nombre) LIKE %:categoria% AND p.isDeleted = false")
    Page<Producto> findByMarcaContainsIgnoreCaseAndCategoriaIgnoreCaseAndIsDeletedFalse(String marca, String categoria, Pageable pageable);

    // Por UUID
    Optional<Producto> findByUuid(UUID uuid);

    // Si está borrado
    List<Producto> findByIsDeleted(Boolean isDeleted);

    // Actualizar el producto con isDeleted a true
    @Modifying // Para indicar que es una consulta de actualización
    @Query("UPDATE Producto p SET p.isDeleted = true WHERE p.id = :id")
    // Consulta de actualización
    void updateIsDeletedToTrueById(Long id);
}

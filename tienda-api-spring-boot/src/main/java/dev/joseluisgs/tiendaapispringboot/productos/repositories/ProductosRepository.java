package dev.joseluisgs.tiendaapispringboot.productos.repositories;

import dev.joseluisgs.tiendaapispringboot.productos.models.Producto;
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
    // List<Producto> findByCategoriaContainsIgnoreCase(String categoria);

    // Por categoria y que deleted sea false
    // List<Producto> findByCategoriaContainsIgnoreCaseAndIsDeletedFalse(String categoria);

    // Por marca
    List<Producto> findByMarcaContainsIgnoreCase(String marca);

    // Por marca y que deleted sea false
    List<Producto> findByMarcaContainsIgnoreCaseAndIsDeletedFalse(String marca);

    // Por marca y categoría
    // List<Producto> findByMarcaContainsIgnoreCaseAndAndCategoriaIgnoreCase(String marca, String categoria);

    // Por marca y categoría y que deleted sea false
    // List<Producto> findByMarcaContainsIgnoreCaseAndAndCategoriaIgnoreCaseAndIsDeletedFalse(String marca, String categoria);

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

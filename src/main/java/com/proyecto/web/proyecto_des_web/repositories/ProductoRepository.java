package com.proyecto.web.proyecto_des_web.repositories;

import com.proyecto.web.proyecto_des_web.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    List<Producto> findByActivoTrue();
    
    List<Producto> findByCategoriaId(Long categoriaId);
    
    @Query("SELECT p FROM Producto p WHERE p.categoria.giroNegocio.id = :giroId AND p.activo = true")
    List<Producto> findByGiroNegocioId(@Param("giroId") Long giroId);
    
    @Query("SELECT p FROM Producto p WHERE p.requiereReunion = :requiere AND p.activo = true")
    List<Producto> findByRequiereReunion(@Param("requiere") Boolean requiere);
    
    @Query("SELECT p FROM Producto p JOIN p.vendedores v WHERE v.id = :vendedorId AND p.activo = true")
    List<Producto> findByVendedorId(@Param("vendedorId") Long vendedorId);
    
    @Query("SELECT p FROM Producto p WHERE p.stock > 0 AND p.activo = true")
    List<Producto> findProductosConStock();
    
    @Query("SELECT p FROM Producto p WHERE p.stock <= :minimo AND p.activo = true")
    List<Producto> findProductosConStockBajo(@Param("minimo") Integer minimo);
}
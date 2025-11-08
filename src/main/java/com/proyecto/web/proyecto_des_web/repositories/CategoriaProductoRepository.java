package com.proyecto.web.proyecto_des_web.repositories;

import com.proyecto.web.proyecto_des_web.entities.CategoriaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, Long> {
    
    List<CategoriaProducto> findByGiroNegocioId(Long giroNegocioId);
    
    @Query("SELECT c FROM CategoriaProducto c WHERE c.giroNegocio.activo = true")
    List<CategoriaProducto> findByGiroNegocioActivo();
    
    boolean existsByNombreAndGiroNegocioId(String nombre, Long giroNegocioId);
}
package com.proyecto.web.proyecto_des_web.repositories;

import com.proyecto.web.proyecto_des_web.entities.GiroNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiroNegocioRepository extends JpaRepository<GiroNegocio, Long> {
    
    List<GiroNegocio> findByActivoTrue();
    
    boolean existsByNombre(String nombre);
}
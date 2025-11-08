package com.proyecto.web.proyecto_des_web.repositories;

import com.proyecto.web.proyecto_des_web.entities.Reunion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReunionRepository extends JpaRepository<Reunion, Long> {
    
    List<Reunion> findByClienteId(Long clienteId);
    
    List<Reunion> findByVendedorId(Long vendedorId);
    
    List<Reunion> findByEstado(Reunion.EstadoReunion estado);
    
    List<Reunion> findByProductoId(Long productoId);
    
    @Query("SELECT r FROM Reunion r WHERE r.fechaHora BETWEEN :fechaInicio AND :fechaFin")
    List<Reunion> findByFechaRange(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                   @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT r FROM Reunion r WHERE r.vendedor.id = :vendedorId AND r.estado = :estado")
    List<Reunion> findByVendedorIdAndEstado(@Param("vendedorId") Long vendedorId, 
                                            @Param("estado") Reunion.EstadoReunion estado);
    
    @Query("SELECT r FROM Reunion r WHERE r.fechaHora >= :fecha ORDER BY r.fechaHora ASC")
    List<Reunion> findReunionesProximas(@Param("fecha") LocalDateTime fecha);
}
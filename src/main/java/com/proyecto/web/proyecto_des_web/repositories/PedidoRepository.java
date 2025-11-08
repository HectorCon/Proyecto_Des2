package com.proyecto.web.proyecto_des_web.repositories;

import com.proyecto.web.proyecto_des_web.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    List<Pedido> findByClienteId(Long clienteId);
    
    List<Pedido> findByVendedorId(Long vendedorId);
    
    List<Pedido> findByEstado(Pedido.EstadoPedido estado);
    
    @Query("SELECT p FROM Pedido p WHERE p.fechaPedido BETWEEN :fechaInicio AND :fechaFin")
    List<Pedido> findByFechaRange(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                  @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId AND p.estado = :estado")
    List<Pedido> findByClienteIdAndEstado(@Param("clienteId") Long clienteId, 
                                          @Param("estado") Pedido.EstadoPedido estado);
    
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estado = :estado")
    Long countByEstado(@Param("estado") Pedido.EstadoPedido estado);
    
    @Query("SELECT SUM(p.total) FROM Pedido p WHERE p.estado = 'ENTREGADO' AND p.fechaPedido BETWEEN :fechaInicio AND :fechaFin")
    Double getTotalVentasEntregadas(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                   @Param("fechaFin") LocalDateTime fechaFin);
}
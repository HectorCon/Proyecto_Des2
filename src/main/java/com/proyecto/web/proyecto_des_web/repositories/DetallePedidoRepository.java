package com.proyecto.web.proyecto_des_web.repositories;

import com.proyecto.web.proyecto_des_web.entities.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    
    List<DetallePedido> findByPedidoId(Long pedidoId);
    
    List<DetallePedido> findByProductoId(Long productoId);
    
    @Query("SELECT dp FROM DetallePedido dp WHERE dp.pedido.cliente.id = :clienteId")
    List<DetallePedido> findByClienteId(@Param("clienteId") Long clienteId);
    
    @Query("SELECT SUM(dp.cantidad) FROM DetallePedido dp WHERE dp.producto.id = :productoId AND dp.pedido.estado = 'ENTREGADO'")
    Integer getTotalVendidoProducto(@Param("productoId") Long productoId);
}
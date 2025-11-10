package com.proyecto.web.proyecto_des_web.repositories;

import com.proyecto.web.proyecto_des_web.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    // Buscar por email
    Optional<Cliente> findByEmail(String email);
    
    // Verificar si existe email
    boolean existsByEmail(String email);
    
    // Buscar por nombre (insensible a mayúsculas)
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar por empresa (insensible a mayúsculas)
    List<Cliente> findByEmpresaContainingIgnoreCase(String empresa);
    
    // Buscar por tipo de cliente
    List<Cliente> findByTipoCliente(String tipoCliente);
    
    // Buscar clientes activos
    List<Cliente> findByActivoTrue();
    
    // Buscar clientes activos por tipo
    List<Cliente> findByActivoTrueAndTipoCliente(String tipoCliente);
    
    // Buscar clientes para selección en pedidos (solo activos)
    @Query("SELECT c FROM Cliente c WHERE c.activo = true ORDER BY c.nombre")
    List<Cliente> findClientesParaPedidos();
    
    // Buscar por teléfono
    Optional<Cliente> findByTelefono(String telefono);
    
    // Contar clientes por tipo
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.tipoCliente = :tipo AND c.activo = true")
    Long countByTipoClienteAndActivo(@Param("tipo") String tipo);
    
    // Buscar clientes con pedidos recientes (últimos 30 días)
    @Query("SELECT DISTINCT c FROM Cliente c JOIN c.pedidos p WHERE c.activo = true AND p.fechaPedido >= :fechaLimite")
    List<Cliente> findClientesConPedidosRecientes(@Param("fechaLimite") LocalDateTime fechaLimite);
    
    // Buscar clientes sin pedidos
    @Query("SELECT c FROM Cliente c WHERE c.activo = true AND SIZE(c.pedidos) = 0")
    List<Cliente> findClientesSinPedidos();
}
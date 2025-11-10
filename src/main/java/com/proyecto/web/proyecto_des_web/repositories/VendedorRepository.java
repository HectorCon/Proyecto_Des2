package com.proyecto.web.proyecto_des_web.repositories;

import com.proyecto.web.proyecto_des_web.entities.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
    
    // Buscar por email
    Optional<Vendedor> findByEmail(String email);
    
    // Verificar si existe email
    boolean existsByEmail(String email);
    
    // Buscar por código de vendedor
    Optional<Vendedor> findByCodigo(String codigo);
    
    // Verificar si existe código
    boolean existsByCodigo(String codigo);
    
    // Buscar por nombre (insensible a mayúsculas)
    List<Vendedor> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar por especialidad (insensible a mayúsculas)
    List<Vendedor> findByEspecialidadContainingIgnoreCase(String especialidad);
    
    // Buscar vendedores activos
    List<Vendedor> findByActivoTrue();
    
    // Buscar vendedores activos para asignación
    @Query("SELECT v FROM Vendedor v WHERE v.activo = true ORDER BY v.nombre")
    List<Vendedor> findVendedoresParaAsignacion();
    
    // Buscar vendedores por meta mensual mínima
    List<Vendedor> findByMetaMensualGreaterThanEqualAndActivoTrue(BigDecimal metaMinima);
    
    // Buscar vendedores por comisión mínima
    List<Vendedor> findByComisionPorcentajeGreaterThanEqualAndActivoTrue(BigDecimal comisionMinima);
    
    // Buscar vendedores con productos asignados
    @Query("SELECT DISTINCT v FROM Vendedor v JOIN v.productosAsignados p WHERE v.activo = true")
    List<Vendedor> findVendedoresConProductos();
    
    // Buscar vendedores sin productos asignados
    @Query("SELECT v FROM Vendedor v WHERE v.activo = true AND SIZE(v.productosAsignados) = 0")
    List<Vendedor> findVendedoresSinProductos();
    
    // Buscar vendedores con pedidos en el mes actual
    @Query("SELECT DISTINCT v FROM Vendedor v JOIN v.pedidos p WHERE v.activo = true AND p.fechaPedido >= :fechaInicioMes")
    List<Vendedor> findVendedoresConPedidosMesActual(@Param("fechaInicioMes") LocalDateTime fechaInicioMes);
    
    // Obtener estadísticas de ventas por vendedor
    @Query("SELECT v.id, v.nombre, COUNT(p.id) as totalPedidos, COALESCE(SUM(p.total), 0) as totalVentas FROM Vendedor v LEFT JOIN v.pedidos p WHERE v.activo = true GROUP BY v.id, v.nombre")
    List<Object[]> getEstadisticasVentas();
    
    // Buscar vendedores por teléfono
    Optional<Vendedor> findByTelefono(String telefono);
    
    // Contar vendedores activos
    @Query("SELECT COUNT(v) FROM Vendedor v WHERE v.activo = true")
    Long countVendedoresActivos();
    
    // Buscar top vendedores por número de pedidos
    @Query("SELECT v FROM Vendedor v LEFT JOIN v.pedidos p WHERE v.activo = true GROUP BY v ORDER BY COUNT(p) DESC")
    List<Vendedor> findTopVendedoresPorPedidos();
}
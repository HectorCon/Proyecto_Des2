package com.proyecto.web.proyecto_des_web.controllers;

import com.proyecto.web.proyecto_des_web.dto.PedidoDTO;
import com.proyecto.web.proyecto_des_web.dto.ProductoDTO;
import com.proyecto.web.proyecto_des_web.dto.ReunionDTO;
import com.proyecto.web.proyecto_des_web.entities.Pedido;
import com.proyecto.web.proyecto_des_web.entities.Reunion;
import com.proyecto.web.proyecto_des_web.services.PedidoService;
import com.proyecto.web.proyecto_des_web.services.ProductoService;
import com.proyecto.web.proyecto_des_web.services.ReunionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReportesController {

    private final PedidoService pedidoService;
    private final ProductoService productoService;
    private final ReunionService reunionService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardData> getDashboardData() {
        DashboardData dashboard = new DashboardData();
        
        // Estadísticas de pedidos
        dashboard.setPedidosPendientes(pedidoService.contarPorEstado("PENDIENTE"));
        dashboard.setPedidosEnProceso(pedidoService.contarPorEstado("EN_PROCESO"));
        dashboard.setPedidosEntregados(pedidoService.contarPorEstado("ENTREGADO"));
        dashboard.setPedidosCancelados(pedidoService.contarPorEstado("CANCELADO"));
        
        // Productos con stock bajo
        dashboard.setProductosStockBajo(productoService.findConStockBajo(10));
        
        // Reuniones próximas
        dashboard.setReunionesProximas(reunionService.findProximas());
        
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/ventas")
    public ResponseEntity<ReporteVentas> getReporteVentas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        
        ReporteVentas reporte = new ReporteVentas();
        reporte.setFechaInicio(inicio);
        reporte.setFechaFin(fin);
        
        // Pedidos en el rango de fechas
        List<PedidoDTO> pedidos = pedidoService.findByRangoFechas(inicio, fin);
        reporte.setPedidos(pedidos);
        
        // Total de ventas entregadas
        Double totalVentas = pedidoService.getTotalVentasEntregadas(inicio, fin);
        reporte.setTotalVentas(totalVentas != null ? totalVentas : 0.0);
        
        // Estadísticas por estado
        Map<String, Long> estadisticas = new HashMap<>();
        for (Pedido.EstadoPedido estado : Pedido.EstadoPedido.values()) {
            estadisticas.put(estado.name(), pedidoService.contarPorEstado(estado.name()));
        }
        reporte.setEstadisticasPorEstado(estadisticas);
        
        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/reuniones")
    public ResponseEntity<ReporteReuniones> getReporteReuniones(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        
        ReporteReuniones reporte = new ReporteReuniones();
        reporte.setFechaInicio(inicio);
        reporte.setFechaFin(fin);
        
        // Reuniones en el rango de fechas
        List<ReunionDTO> reuniones = reunionService.findByRangoFechas(inicio, fin);
        reporte.setReuniones(reuniones);
        
        // Estadísticas por estado
        Map<String, Long> estadisticas = new HashMap<>();
        for (Reunion.EstadoReunion estado : Reunion.EstadoReunion.values()) {
            long count = reuniones.stream()
                    .filter(r -> r.getEstado().equals(estado.name()))
                    .count();
            estadisticas.put(estado.name(), count);
        }
        reporte.setEstadisticasPorEstado(estadisticas);
        
        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/inventario")
    public ResponseEntity<ReporteInventario> getReporteInventario() {
        ReporteInventario reporte = new ReporteInventario();
        
        reporte.setTodosLosProductos(productoService.findAll());
        reporte.setProductosActivos(productoService.findActivos());
        reporte.setProductosConStock(productoService.findConStock());
        reporte.setProductosStockBajo(productoService.findConStockBajo(10));
        reporte.setProductosQueRequierenReunion(productoService.findQueRequierenReunion());
        
        return ResponseEntity.ok(reporte);
    }

    @Data
    public static class DashboardData {
        private Long pedidosPendientes;
        private Long pedidosEnProceso;
        private Long pedidosEntregados;
        private Long pedidosCancelados;
        private List<ProductoDTO> productosStockBajo;
        private List<ReunionDTO> reunionesProximas;
    }

    @Data
    public static class ReporteVentas {
        private LocalDateTime fechaInicio;
        private LocalDateTime fechaFin;
        private List<PedidoDTO> pedidos;
        private Double totalVentas;
        private Map<String, Long> estadisticasPorEstado;
    }

    @Data
    public static class ReporteReuniones {
        private LocalDateTime fechaInicio;
        private LocalDateTime fechaFin;
        private List<ReunionDTO> reuniones;
        private Map<String, Long> estadisticasPorEstado;
    }

    @Data
    public static class ReporteInventario {
        private List<ProductoDTO> todosLosProductos;
        private List<ProductoDTO> productosActivos;
        private List<ProductoDTO> productosConStock;
        private List<ProductoDTO> productosStockBajo;
        private List<ProductoDTO> productosQueRequierenReunion;
    }
}
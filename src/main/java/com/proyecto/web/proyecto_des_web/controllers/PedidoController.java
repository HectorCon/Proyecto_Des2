package com.proyecto.web.proyecto_des_web.controllers;

import com.proyecto.web.proyecto_des_web.dto.DetallePedidoDTO;
import com.proyecto.web.proyecto_des_web.dto.PedidoDTO;
import com.proyecto.web.proyecto_des_web.entities.Pedido;
import com.proyecto.web.proyecto_des_web.services.PedidoService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> getAllPedidos() {
        return ResponseEntity.ok(pedidoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Long id) {
        Optional<Pedido> pedido = pedidoService.findById(id);
        return pedido.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoDTO>> getPedidosByCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(pedidoService.findByCliente(clienteId));
    }

    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<List<PedidoDTO>> getPedidosByVendedor(@PathVariable Long vendedorId) {
        return ResponseEntity.ok(pedidoService.findByVendedor(vendedorId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PedidoDTO>> getPedidosByEstado(@PathVariable String estado) {
        try {
            return ResponseEntity.ok(pedidoService.findByEstado(estado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/rango-fechas")
    public ResponseEntity<List<PedidoDTO>> getPedidosByRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(pedidoService.findByRangoFechas(inicio, fin));
    }

    @PostMapping
    public ResponseEntity<?> createPedido(@RequestBody CrearPedidoRequest request) {
        try {
            // Validaciones básicas
            if (request.getClienteId() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "ID de cliente es requerido"));
            }
            if (request.getVendedorId() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "ID de vendedor es requerido"));
            }
            if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Los detalles del pedido son requeridos"));
            }

            Pedido nuevoPedido = pedidoService.crearPedido(
                request.getClienteId(),
                request.getVendedorId(),
                request.getDetalles(),
                request.getNotas()
            );
            return ResponseEntity.ok(nuevoPedido);
        } catch (RuntimeException e) {
            // Errores específicos del negocio
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // Errores inesperados
            return ResponseEntity.status(500).body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstadoPedido(@PathVariable Long id, @RequestParam String nuevoEstado) {
        try {
            Pedido pedido = pedidoService.actualizarEstado(id, nuevoEstado);
            return ResponseEntity.ok(pedido);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estadisticas/count/{estado}")
    public ResponseEntity<Long> contarPorEstado(@PathVariable String estado) {
        try {
            return ResponseEntity.ok(pedidoService.contarPorEstado(estado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/estadisticas/ventas")
    public ResponseEntity<Double> getTotalVentasEntregadas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(pedidoService.getTotalVentasEntregadas(inicio, fin));
    }

    @Data
    public static class CrearPedidoRequest {
        private Long clienteId;
        private Long vendedorId;
        private String notas;
        private List<DetallePedidoDTO> detalles;
    }
}
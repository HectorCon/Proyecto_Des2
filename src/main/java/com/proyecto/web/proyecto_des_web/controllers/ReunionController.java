package com.proyecto.web.proyecto_des_web.controllers;

import com.proyecto.web.proyecto_des_web.dto.ReunionDTO;
import com.proyecto.web.proyecto_des_web.entities.Reunion;
import com.proyecto.web.proyecto_des_web.services.ReunionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reuniones")
@RequiredArgsConstructor
public class ReunionController {

    private final ReunionService reunionService;

    @GetMapping
    public ResponseEntity<List<ReunionDTO>> getAllReuniones() {
        return ResponseEntity.ok(reunionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reunion> getReunionById(@PathVariable Long id) {
        Optional<Reunion> reunion = reunionService.findById(id);
        return reunion.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<ReunionDTO>> getReunionesByCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(reunionService.findByCliente(clienteId));
    }

    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<List<ReunionDTO>> getReunionesByVendedor(@PathVariable Long vendedorId) {
        return ResponseEntity.ok(reunionService.findByVendedor(vendedorId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReunionDTO>> getReunionesByEstado(@PathVariable String estado) {
        try {
            return ResponseEntity.ok(reunionService.findByEstado(estado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/proximas")
    public ResponseEntity<List<ReunionDTO>> getReunionesProximas() {
        return ResponseEntity.ok(reunionService.findProximas());
    }

    @GetMapping("/rango-fechas")
    public ResponseEntity<List<ReunionDTO>> getReunionesByRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(reunionService.findByRangoFechas(inicio, fin));
    }

    @PostMapping
    public ResponseEntity<Reunion> createReunion(@RequestBody CrearReunionRequest request) {
        try {
            Reunion nuevaReunion = reunionService.crearReunion(
                request.getClienteId(),
                request.getVendedorId(),
                request.getProductoId(),
                request.getFechaHora(),
                request.getDireccion(),
                request.getNotas()
            );
            return ResponseEntity.ok(nuevaReunion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Reunion> actualizarEstadoReunion(@PathVariable Long id, @RequestParam String nuevoEstado) {
        try {
            Reunion reunion = reunionService.actualizarEstado(id, nuevoEstado);
            return ResponseEntity.ok(reunion);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReunion(@PathVariable Long id) {
        try {
            if (!reunionService.findById(id).isPresent()) {
                return ResponseEntity.notFound().build();
            }
            reunionService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Data
    public static class CrearReunionRequest {
        private Long clienteId;
        private Long vendedorId;
        private Long productoId;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime fechaHora;
        private String direccion;
        private String notas;
    }
}
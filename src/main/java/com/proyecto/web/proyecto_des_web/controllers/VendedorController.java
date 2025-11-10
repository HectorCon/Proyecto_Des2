package com.proyecto.web.proyecto_des_web.controllers;

import com.proyecto.web.proyecto_des_web.dto.VendedorDTO;
import com.proyecto.web.proyecto_des_web.services.VendedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/vendedores")
@RequiredArgsConstructor
public class VendedorController {

    private final VendedorService vendedorService;

    @GetMapping
    public ResponseEntity<List<VendedorDTO>> getAllVendedores() {
        try {
            return ResponseEntity.ok(vendedorService.findAllVendedores());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<List<VendedorDTO>> getVendedoresActivos() {
        try {
            return ResponseEntity.ok(vendedorService.findVendedoresActivos());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendedorDTO> getVendedorById(@PathVariable Long id) {
        try {
            Optional<VendedorDTO> vendedor = vendedorService.findVendedorById(id);
            return vendedor.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<VendedorDTO> createVendedor(@RequestBody VendedorDTO vendedorDTO) {
        try {
            // Validaciones básicas
            if (vendedorDTO.getNombre() == null || vendedorDTO.getNombre().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (vendedorDTO.getEmail() == null || vendedorDTO.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            VendedorDTO nuevoVendedor = vendedorService.createVendedor(vendedorDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoVendedor);
        } catch (RuntimeException e) {
            // Errores de negocio (email duplicado, código duplicado, etc.)
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendedorDTO> updateVendedor(@PathVariable Long id, @RequestBody VendedorDTO vendedorDTO) {
        try {
            // Validaciones básicas
            if (vendedorDTO.getNombre() == null || vendedorDTO.getNombre().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (vendedorDTO.getEmail() == null || vendedorDTO.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            VendedorDTO vendedorActualizado = vendedorService.updateVendedor(id, vendedorDTO);
            return ResponseEntity.ok(vendedorActualizado);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/toggle-activo")
    public ResponseEntity<Map<String, Object>> toggleActivoVendedor(@PathVariable Long id) {
        try {
            VendedorDTO vendedor = vendedorService.toggleActivo(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", vendedor.getActivo() ? "Vendedor activado exitosamente" : "Vendedor desactivado exitosamente");
            response.put("vendedorId", vendedor.getId());
            response.put("activo", vendedor.getActivo());
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint específico para seleccionar vendedores en formularios
    @GetMapping("/para-asignacion")
    public ResponseEntity<List<VendedorDTO>> getVendedoresParaAsignacion() {
        try {
            // Solo vendedores activos con información básica
            List<VendedorDTO> vendedores = vendedorService.findVendedoresActivos();
            return ResponseEntity.ok(vendedores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
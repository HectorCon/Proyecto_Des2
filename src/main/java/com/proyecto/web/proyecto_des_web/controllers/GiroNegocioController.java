package com.proyecto.web.proyecto_des_web.controllers;

import com.proyecto.web.proyecto_des_web.entities.GiroNegocio;
import com.proyecto.web.proyecto_des_web.repositories.GiroNegocioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/giros-negocio")
@RequiredArgsConstructor
public class GiroNegocioController {

    private final GiroNegocioRepository giroNegocioRepository;

    @GetMapping
    public ResponseEntity<List<GiroNegocio>> getAllGirosNegocio() {
        return ResponseEntity.ok(giroNegocioRepository.findAll());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<GiroNegocio>> getGirosNegocioActivos() {
        return ResponseEntity.ok(giroNegocioRepository.findByActivoTrue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiroNegocio> getGiroNegocioById(@PathVariable Long id) {
        Optional<GiroNegocio> giro = giroNegocioRepository.findById(id);
        return giro.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GiroNegocio> createGiroNegocio(@RequestBody GiroNegocio giroNegocio) {
        try {
            if (giroNegocioRepository.existsByNombre(giroNegocio.getNombre())) {
                return ResponseEntity.badRequest().build();
            }
            GiroNegocio nuevoGiro = giroNegocioRepository.save(giroNegocio);
            return ResponseEntity.ok(nuevoGiro);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GiroNegocio> updateGiroNegocio(@PathVariable Long id, @RequestBody GiroNegocio giroNegocio) {
        try {
            if (!giroNegocioRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            giroNegocio.setId(id);
            GiroNegocio giroActualizado = giroNegocioRepository.save(giroNegocio);
            return ResponseEntity.ok(giroActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGiroNegocio(@PathVariable Long id) {
        try {
            if (!giroNegocioRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            giroNegocioRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
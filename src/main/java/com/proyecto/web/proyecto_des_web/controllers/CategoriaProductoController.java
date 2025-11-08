package com.proyecto.web.proyecto_des_web.controllers;

import com.proyecto.web.proyecto_des_web.entities.CategoriaProducto;
import com.proyecto.web.proyecto_des_web.repositories.CategoriaProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias-productos")
@RequiredArgsConstructor
public class CategoriaProductoController {

    private final CategoriaProductoRepository categoriaRepository;

    @GetMapping
    public ResponseEntity<List<CategoriaProducto>> getAllCategorias() {
        return ResponseEntity.ok(categoriaRepository.findAll());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<CategoriaProducto>> getCategoriasActivas() {
        return ResponseEntity.ok(categoriaRepository.findByGiroNegocioActivo());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaProducto> getCategoriaById(@PathVariable Long id) {
        Optional<CategoriaProducto> categoria = categoriaRepository.findById(id);
        return categoria.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/giro/{giroId}")
    public ResponseEntity<List<CategoriaProducto>> getCategoriasByGiro(@PathVariable Long giroId) {
        return ResponseEntity.ok(categoriaRepository.findByGiroNegocioId(giroId));
    }

    @PostMapping
    public ResponseEntity<CategoriaProducto> createCategoria(@RequestBody CategoriaProducto categoria) {
        try {
            if (categoriaRepository.existsByNombreAndGiroNegocioId(
                    categoria.getNombre(), 
                    categoria.getGiroNegocio().getId())) {
                return ResponseEntity.badRequest().build();
            }
            CategoriaProducto nuevaCategoria = categoriaRepository.save(categoria);
            return ResponseEntity.ok(nuevaCategoria);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaProducto> updateCategoria(@PathVariable Long id, @RequestBody CategoriaProducto categoria) {
        try {
            if (!categoriaRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            categoria.setId(id);
            CategoriaProducto categoriaActualizada = categoriaRepository.save(categoria);
            return ResponseEntity.ok(categoriaActualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        try {
            if (!categoriaRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            categoriaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
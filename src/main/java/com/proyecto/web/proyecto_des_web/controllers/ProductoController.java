package com.proyecto.web.proyecto_des_web.controllers;

import com.proyecto.web.proyecto_des_web.dto.BulkStockUpdateDTO;
import com.proyecto.web.proyecto_des_web.dto.CreateProductoDTO;
import com.proyecto.web.proyecto_des_web.dto.ProductoDTO;
import com.proyecto.web.proyecto_des_web.entities.Producto;
import com.proyecto.web.proyecto_des_web.services.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> getAllProductos() {
        return ResponseEntity.ok(productoService.findAll());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ProductoDTO>> getProductosActivos() {
        return ResponseEntity.ok(productoService.findActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        Optional<Producto> producto = productoService.findById(id);
        return producto.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoDTO>> getProductosByCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.findByCategoria(categoriaId));
    }

    @GetMapping("/giro/{giroId}")
    public ResponseEntity<List<ProductoDTO>> getProductosByGiro(@PathVariable Long giroId) {
        return ResponseEntity.ok(productoService.findByGiroNegocio(giroId));
    }

    @GetMapping("/requieren-reunion")
    public ResponseEntity<List<ProductoDTO>> getProductosQueRequierenReunion() {
        return ResponseEntity.ok(productoService.findQueRequierenReunion());
    }

    @GetMapping("/con-stock")
    public ResponseEntity<List<ProductoDTO>> getProductosConStock() {
        return ResponseEntity.ok(productoService.findConStock());
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<List<ProductoDTO>> getProductosConStockBajo(@RequestParam(defaultValue = "10") Integer minimo) {
        return ResponseEntity.ok(productoService.findConStockBajo(minimo));
    }

    @PostMapping
    public ResponseEntity<?> createProducto(@RequestBody CreateProductoDTO dto) {
        try {
            Producto nuevoProducto = productoService.createFromDTO(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al crear el producto");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            if (!productoService.findById(id).isPresent()) {
                return ResponseEntity.notFound().build();
            }
            producto.setId(id);
            Producto productoActualizado = productoService.save(producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Producto> actualizarStock(@PathVariable Long id, @RequestParam Integer nuevoStock) {
        try {
            Producto producto = productoService.actualizarStock(id, nuevoStock);
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/stock/incrementar")
    public ResponseEntity<Producto> incrementarStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        try {
            Producto producto = productoService.incrementarStock(id, cantidad);
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}/stock/decrementar")
    public ResponseEntity<Producto> decrementarStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        try {
            Producto producto = productoService.decrementarStock(id, cantidad);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/stock/masivo")
    public ResponseEntity<?> actualizarStockMasivo(@RequestBody BulkStockUpdateDTO dto) {
        try {
            List<Producto> productosActualizados = productoService.actualizarStockMasivo(dto.getOperaciones());
            return ResponseEntity.ok(productosActualizados);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        try {
            if (!productoService.findById(id).isPresent()) {
                return ResponseEntity.notFound().build();
            }
            productoService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
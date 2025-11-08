package com.proyecto.web.proyecto_des_web.controllers;

import com.proyecto.web.proyecto_des_web.entities.Usuario;
import com.proyecto.web.proyecto_des_web.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        return usuario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> getUsuarioByEmail(@PathVariable String email) {
        Optional<Usuario> usuario = usuarioService.findByEmail(email);
        return usuario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/vendedores")
    public ResponseEntity<List<Usuario>> getVendedores() {
        return ResponseEntity.ok(usuarioService.findVendedores());
    }

    @GetMapping("/clientes")
    public ResponseEntity<List<Usuario>> getClientes() {
        return ResponseEntity.ok(usuarioService.findClientes());
    }

    @GetMapping("/rol/{rolNombre}")
    public ResponseEntity<List<Usuario>> getUsuariosByRol(@PathVariable String rolNombre) {
        return ResponseEntity.ok(usuarioService.findByRol(rolNombre));
    }

    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
        try {
            if (usuarioService.existsByEmail(usuario.getEmail())) {
                return ResponseEntity.badRequest().build();
            }
            Usuario nuevoUsuario = usuarioService.save(usuario);
            return ResponseEntity.ok(nuevoUsuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            if (!usuarioService.findById(id).isPresent()) {
                return ResponseEntity.notFound().build();
            }
            usuario.setId(id);
            Usuario usuarioActualizado = usuarioService.save(usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/toggle-activo")
    public ResponseEntity<Usuario> toggleActivo(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.activarDesactivar(id);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        try {
            if (!usuarioService.findById(id).isPresent()) {
                return ResponseEntity.notFound().build();
            }
            usuarioService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
package com.proyecto.web.proyecto_des_web.controllers;

import com.proyecto.web.proyecto_des_web.dto.auth.LoginRequest;
import com.proyecto.web.proyecto_des_web.dto.auth.LoginResponse;
import com.proyecto.web.proyecto_des_web.dto.auth.RegisterRequest;
import com.proyecto.web.proyecto_des_web.entities.Role;
import com.proyecto.web.proyecto_des_web.repositories.RoleRepository;
import com.proyecto.web.proyecto_des_web.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RoleRepository roleRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            LoginResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(roleRepository.findAll());
    }

    // Endpoints para demostración - datos de prueba
    @GetMapping("/test-credentials")
    public ResponseEntity<?> getTestCredentials() {
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("admin", Map.of(
            "email", "admin@empresa.com",
            "password", "admin123",
            "rol", "ADMIN"
        ));
        credentials.put("vendedor", Map.of(
            "email", "juan.vendedor@empresa.com", 
            "password", "vendedor123",
            "rol", "VENDEDOR"
        ));
        credentials.put("cliente", Map.of(
            "email", "maria.cliente@gmail.com",
            "password", "cliente123", 
            "rol", "CLIENTE"
        ));
        
        return ResponseEntity.ok(credentials);
    }
}
package com.proyecto.web.proyecto_des_web.controllers;

import com.proyecto.web.proyecto_des_web.dto.ClienteDTO;
import com.proyecto.web.proyecto_des_web.services.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> getAllClientes() {
        return ResponseEntity.ok(clienteService.findAllClientes());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ClienteDTO>> getClientesActivos() {
        return ResponseEntity.ok(clienteService.findClientesActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) {
        Optional<ClienteDTO> cliente = clienteService.findClienteById(id);
        return cliente.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint específico para seleccionar clientes en formularios de pedidos
    @GetMapping("/para-pedidos")
    public ResponseEntity<List<ClienteDTO>> getClientesParaPedidos() {
        // Solo clientes activos con información básica
        List<ClienteDTO> clientes = clienteService.findClientesActivos();
        return ResponseEntity.ok(clientes);
    }

    @PostMapping
    public ResponseEntity<?> createCliente(@RequestBody ClienteDTO clienteDTO) {
        try {
            ClienteDTO nuevoCliente = clienteService.createCliente(clienteDTO);
            return ResponseEntity.status(201).body(nuevoCliente);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCliente(@PathVariable Long id, @RequestBody ClienteDTO clienteDTO) {
        try {
            ClienteDTO clienteActualizado = clienteService.updateCliente(id, clienteDTO);
            return ResponseEntity.ok(clienteActualizado);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/toggle-activo")
    public ResponseEntity<?> toggleActivoCliente(@PathVariable Long id) {
        try {
            clienteService.toggleActivo(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
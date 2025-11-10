package com.proyecto.web.proyecto_des_web.services;

import com.proyecto.web.proyecto_des_web.dto.ClienteDTO;
import com.proyecto.web.proyecto_des_web.entities.Cliente;
import com.proyecto.web.proyecto_des_web.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<ClienteDTO> findAllClientes() {
        return clienteRepository.findAll().stream()
                .map(this::convertToClienteDTO)
                .collect(Collectors.toList());
    }

    public List<ClienteDTO> findClientesActivos() {
        return clienteRepository.findByActivoTrue().stream()
                .map(this::convertToClienteDTO)
                .collect(Collectors.toList());
    }

    public Optional<ClienteDTO> findClienteById(Long id) {
        return clienteRepository.findById(id)
                .map(this::convertToClienteDTO);
    }

    @Transactional
    public ClienteDTO createCliente(ClienteDTO clienteDTO) {
        // Verificar si ya existe un cliente con el mismo email
        if (clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new RuntimeException("Ya existe un cliente con el email: " + clienteDTO.getEmail());
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(clienteDTO.getNombre());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setTelefono(clienteDTO.getTelefono());
        cliente.setDireccion(clienteDTO.getDireccion());
        cliente.setEmpresa(clienteDTO.getEmpresa());
        cliente.setTipoCliente(clienteDTO.getTipoCliente() != null ? clienteDTO.getTipoCliente() : "PERSONA");
        cliente.setNotas(clienteDTO.getNotas());
        cliente.setActivo(true);
        cliente.setFechaRegistro(LocalDateTime.now());

        Cliente clienteGuardado = clienteRepository.save(cliente);
        return convertToClienteDTO(clienteGuardado);
    }

    @Transactional
    public ClienteDTO updateCliente(Long id, ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));

        // Verificar email duplicado solo si está cambiando
        if (!cliente.getEmail().equals(clienteDTO.getEmail()) && 
            clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new RuntimeException("Ya existe un cliente con el email: " + clienteDTO.getEmail());
        }

        cliente.setNombre(clienteDTO.getNombre());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setTelefono(clienteDTO.getTelefono());
        cliente.setDireccion(clienteDTO.getDireccion());
        cliente.setEmpresa(clienteDTO.getEmpresa());
        cliente.setTipoCliente(clienteDTO.getTipoCliente());
        cliente.setNotas(clienteDTO.getNotas());

        Cliente clienteActualizado = clienteRepository.save(cliente);
        return convertToClienteDTO(clienteActualizado);
    }

    @Transactional
    public void toggleActivo(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        
        cliente.setActivo(!cliente.getActivo());
        clienteRepository.save(cliente);
    }

    public ClienteDTO convertToClienteDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setEmail(cliente.getEmail());
        dto.setTelefono(cliente.getTelefono());
        dto.setDireccion(cliente.getDireccion());
        dto.setEmpresa(cliente.getEmpresa());
        dto.setTipoCliente(cliente.getTipoCliente());
        dto.setActivo(cliente.getActivo());
        dto.setFechaRegistro(cliente.getFechaRegistro());
        dto.setNotas(cliente.getNotas());
        
        // Contar pedidos del cliente
        if (cliente.getPedidos() != null) {
            dto.setTotalPedidos(cliente.getPedidos().size());
            
            // Obtener fecha del último pedido
            if (!cliente.getPedidos().isEmpty()) {
                var ultimoPedido = cliente.getPedidos().stream()
                        .max((p1, p2) -> p1.getFechaPedido().compareTo(p2.getFechaPedido()));
                if (ultimoPedido.isPresent()) {
                    dto.setUltimoPedido(ultimoPedido.get().getFechaPedido().toString());
                }
            } else {
                dto.setUltimoPedido("Sin pedidos");
            }
        } else {
            dto.setTotalPedidos(0);
            dto.setUltimoPedido("Sin pedidos");
        }
        
        return dto;
    }
}
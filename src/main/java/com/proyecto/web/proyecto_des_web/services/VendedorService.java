package com.proyecto.web.proyecto_des_web.services;

import com.proyecto.web.proyecto_des_web.dto.VendedorDTO;
import com.proyecto.web.proyecto_des_web.entities.Vendedor;
import com.proyecto.web.proyecto_des_web.repositories.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VendedorService {

    private final VendedorRepository vendedorRepository;

    public List<VendedorDTO> findAllVendedores() {
        return vendedorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<VendedorDTO> findVendedoresActivos() {
        return vendedorRepository.findByActivoTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<VendedorDTO> findVendedorById(Long id) {
        return vendedorRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public VendedorDTO createVendedor(VendedorDTO vendedorDTO) {
        // Validaciones
        if (vendedorDTO.getEmail() != null && vendedorRepository.existsByEmail(vendedorDTO.getEmail())) {
            throw new RuntimeException("Ya existe un vendedor con el email: " + vendedorDTO.getEmail());
        }
        
        if (vendedorDTO.getCodigo() != null && vendedorRepository.existsByCodigo(vendedorDTO.getCodigo())) {
            throw new RuntimeException("Ya existe un vendedor con el código: " + vendedorDTO.getCodigo());
        }

        Vendedor vendedor = convertToEntity(vendedorDTO);
        vendedor.setId(null); // Asegurar que es un nuevo vendedor
        vendedor.setFechaIngreso(LocalDateTime.now());
        vendedor.setActivo(true);
        
        Vendedor savedVendedor = vendedorRepository.save(vendedor);
        return convertToDTO(savedVendedor);
    }

    @Transactional
    public VendedorDTO updateVendedor(Long id, VendedorDTO vendedorDTO) {
        Vendedor vendedor = vendedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado con ID: " + id));

        // Validar email único (excluyendo el vendedor actual)
        if (vendedorDTO.getEmail() != null && 
            !vendedor.getEmail().equals(vendedorDTO.getEmail()) && 
            vendedorRepository.existsByEmail(vendedorDTO.getEmail())) {
            throw new RuntimeException("Ya existe un vendedor con el email: " + vendedorDTO.getEmail());
        }

        // Validar código único (excluyendo el vendedor actual)
        if (vendedorDTO.getCodigo() != null && 
            !vendedor.getCodigo().equals(vendedorDTO.getCodigo()) && 
            vendedorRepository.existsByCodigo(vendedorDTO.getCodigo())) {
            throw new RuntimeException("Ya existe un vendedor with el código: " + vendedorDTO.getCodigo());
        }

        // Actualizar campos
        vendedor.setNombre(vendedorDTO.getNombre());
        vendedor.setEmail(vendedorDTO.getEmail());
        vendedor.setTelefono(vendedorDTO.getTelefono());
        vendedor.setCodigo(vendedorDTO.getCodigo());
        vendedor.setEspecialidad(vendedorDTO.getEspecialidad());
        vendedor.setMetaMensual(vendedorDTO.getMetaMensual());
        vendedor.setComisionPorcentaje(vendedorDTO.getComisionPorcentaje());
        vendedor.setNotas(vendedorDTO.getNotas());

        Vendedor savedVendedor = vendedorRepository.save(vendedor);
        return convertToDTO(savedVendedor);
    }

    @Transactional
    public VendedorDTO toggleActivo(Long id) {
        Vendedor vendedor = vendedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado con ID: " + id));
                
        vendedor.setActivo(!vendedor.getActivo());
        Vendedor savedVendedor = vendedorRepository.save(vendedor);
        return convertToDTO(savedVendedor);
    }

    private VendedorDTO convertToDTO(Vendedor vendedor) {
        VendedorDTO dto = new VendedorDTO();
        dto.setId(vendedor.getId());
        dto.setNombre(vendedor.getNombre());
        dto.setEmail(vendedor.getEmail());
        dto.setTelefono(vendedor.getTelefono());
        dto.setCodigo(vendedor.getCodigo());
        dto.setEspecialidad(vendedor.getEspecialidad());
        dto.setMetaMensual(vendedor.getMetaMensual());
        dto.setComisionPorcentaje(vendedor.getComisionPorcentaje());
        dto.setActivo(vendedor.getActivo());
        dto.setFechaIngreso(vendedor.getFechaIngreso());
        dto.setNotas(vendedor.getNotas());
        
        // Estadísticas adicionales
        int totalPedidos = vendedor.getPedidos() != null ? vendedor.getPedidos().size() : 0;
        dto.setTotalPedidos(totalPedidos);
        
        int totalReuniones = vendedor.getReuniones() != null ? vendedor.getReuniones().size() : 0;
        dto.setTotalReuniones(totalReuniones);
        
        // Fecha de última venta
        if (vendedor.getPedidos() != null && !vendedor.getPedidos().isEmpty()) {
            var ultimaVenta = vendedor.getPedidos().stream()
                    .max((p1, p2) -> p1.getFechaPedido().compareTo(p2.getFechaPedido()));
            if (ultimaVenta.isPresent()) {
                dto.setUltimaVenta(ultimaVenta.get().getFechaPedido().toLocalDate().toString());
            }
        } else {
            dto.setUltimaVenta("Sin ventas");
        }
        
        return dto;
    }

    private Vendedor convertToEntity(VendedorDTO dto) {
        Vendedor vendedor = new Vendedor();
        vendedor.setId(dto.getId());
        vendedor.setNombre(dto.getNombre());
        vendedor.setEmail(dto.getEmail());
        vendedor.setTelefono(dto.getTelefono());
        vendedor.setCodigo(dto.getCodigo());
        vendedor.setEspecialidad(dto.getEspecialidad());
        vendedor.setMetaMensual(dto.getMetaMensual());
        vendedor.setComisionPorcentaje(dto.getComisionPorcentaje());
        vendedor.setActivo(dto.getActivo());
        vendedor.setFechaIngreso(dto.getFechaIngreso());
        vendedor.setNotas(dto.getNotas());
        return vendedor;
    }
}
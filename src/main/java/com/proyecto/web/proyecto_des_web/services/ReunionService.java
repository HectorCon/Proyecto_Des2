package com.proyecto.web.proyecto_des_web.services;

import com.proyecto.web.proyecto_des_web.dto.ReunionDTO;
import com.proyecto.web.proyecto_des_web.entities.Producto;
import com.proyecto.web.proyecto_des_web.entities.Reunion;
import com.proyecto.web.proyecto_des_web.entities.Usuario;
import com.proyecto.web.proyecto_des_web.repositories.ProductoRepository;
import com.proyecto.web.proyecto_des_web.repositories.ReunionRepository;
import com.proyecto.web.proyecto_des_web.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReunionService {

    private final ReunionRepository reunionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public List<ReunionDTO> findAll() {
        return reunionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<Reunion> findById(Long id) {
        return reunionRepository.findById(id);
    }

    public List<ReunionDTO> findByCliente(Long clienteId) {
        return reunionRepository.findByClienteId(clienteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReunionDTO> findByVendedor(Long vendedorId) {
        return reunionRepository.findByVendedorId(vendedorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReunionDTO> findByEstado(String estado) {
        Reunion.EstadoReunion estadoEnum = Reunion.EstadoReunion.valueOf(estado);
        return reunionRepository.findByEstado(estadoEnum).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReunionDTO> findProximas() {
        return reunionRepository.findReunionesProximas(LocalDateTime.now()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReunionDTO> findByRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return reunionRepository.findByFechaRange(inicio, fin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Reunion crearReunion(Long clienteId, Long vendedorId, Long productoId, 
                               LocalDateTime fechaHora, String direccion, String notas) {
        Usuario cliente = usuarioRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Usuario vendedor = usuarioRepository.findById(vendedorId)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Reunion reunion = new Reunion();
        reunion.setCliente(cliente);
        reunion.setVendedor(vendedor);
        reunion.setProducto(producto);
        reunion.setFechaHora(fechaHora);
        reunion.setDireccion(direccion);
        reunion.setNotas(notas);
        reunion.setEstado(Reunion.EstadoReunion.PROGRAMADA);

        return reunionRepository.save(reunion);
    }

    public Reunion actualizarEstado(Long reunionId, String nuevoEstado) {
        Optional<Reunion> reunionOpt = reunionRepository.findById(reunionId);
        if (reunionOpt.isPresent()) {
            Reunion reunion = reunionOpt.get();
            reunion.setEstado(Reunion.EstadoReunion.valueOf(nuevoEstado));
            return reunionRepository.save(reunion);
        }
        throw new RuntimeException("Reunión no encontrada");
    }

    public void deleteById(Long id) {
        reunionRepository.deleteById(id);
    }

    private ReunionDTO convertToDTO(Reunion reunion) {
        ReunionDTO dto = new ReunionDTO();
        dto.setId(reunion.getId());
        dto.setFechaHora(reunion.getFechaHora());
        dto.setDireccion(reunion.getDireccion());
        dto.setEstado(reunion.getEstado().name());
        dto.setNotas(reunion.getNotas());
        dto.setClienteNombre(reunion.getCliente().getNombre());
        dto.setClienteEmail(reunion.getCliente().getEmail());
        dto.setVendedorNombre(reunion.getVendedor().getNombre());
        dto.setVendedorEmail(reunion.getVendedor().getEmail());
        dto.setProductoNombre(reunion.getProducto().getNombre());
        dto.setProductoId(reunion.getProducto().getId());
        return dto;
    }
}
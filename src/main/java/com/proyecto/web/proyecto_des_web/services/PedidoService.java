package com.proyecto.web.proyecto_des_web.services;

import com.proyecto.web.proyecto_des_web.dto.DetallePedidoDTO;
import com.proyecto.web.proyecto_des_web.dto.PedidoDTO;
import com.proyecto.web.proyecto_des_web.entities.DetallePedido;
import com.proyecto.web.proyecto_des_web.entities.Pedido;
import com.proyecto.web.proyecto_des_web.entities.Producto;
import com.proyecto.web.proyecto_des_web.entities.Usuario;
import com.proyecto.web.proyecto_des_web.repositories.DetallePedidoRepository;
import com.proyecto.web.proyecto_des_web.repositories.PedidoRepository;
import com.proyecto.web.proyecto_des_web.repositories.ProductoRepository;
import com.proyecto.web.proyecto_des_web.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public List<PedidoDTO> findAll() {
        return pedidoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<PedidoDTO> findByCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDTO> findByVendedor(Long vendedorId) {
        return pedidoRepository.findByVendedorId(vendedorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDTO> findByEstado(String estado) {
        Pedido.EstadoPedido estadoEnum = Pedido.EstadoPedido.valueOf(estado);
        return pedidoRepository.findByEstado(estadoEnum).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDTO> findByRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return pedidoRepository.findByFechaRange(inicio, fin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Pedido crearPedido(Long clienteId, Long vendedorId, List<DetallePedidoDTO> detallesDTO, String notas) {
        Usuario cliente = usuarioRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Usuario vendedor = usuarioRepository.findById(vendedorId)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setNumeroPedido(generarNumeroPedido());
        pedido.setCliente(cliente);
        pedido.setVendedor(vendedor);
        pedido.setNotas(notas);
        pedido.setEstado(Pedido.EstadoPedido.PENDIENTE);

        BigDecimal total = BigDecimal.ZERO;

        pedido = pedidoRepository.save(pedido);

        for (DetallePedidoDTO detalleDTO : detallesDTO) {
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // Verificar stock si no requiere reunión
            if (!producto.getRequiereReunion() && producto.getStock() < detalleDTO.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(detalleDTO.getCantidad())));

            detallePedidoRepository.save(detalle);

            total = total.add(detalle.getSubtotal());

            // Reducir stock si no requiere reunión
            if (!producto.getRequiereReunion()) {
                producto.setStock(producto.getStock() - detalleDTO.getCantidad());
                productoRepository.save(producto);
            }
        }

        pedido.setTotal(total);
        return pedidoRepository.save(pedido);
    }

    public Pedido actualizarEstado(Long pedidoId, String nuevoEstado) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(pedidoId);
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            pedido.setEstado(Pedido.EstadoPedido.valueOf(nuevoEstado));
            return pedidoRepository.save(pedido);
        }
        throw new RuntimeException("Pedido no encontrado");
    }

    public Long contarPorEstado(String estado) {
        return pedidoRepository.countByEstado(Pedido.EstadoPedido.valueOf(estado));
    }

    public Double getTotalVentasEntregadas(LocalDateTime inicio, LocalDateTime fin) {
        return pedidoRepository.getTotalVentasEntregadas(inicio, fin);
    }

    private String generarNumeroPedido() {
        LocalDateTime now = LocalDateTime.now();
        return String.format("PED-%d-%02d%02d-%03d", 
                now.getYear(), 
                now.getMonthValue(), 
                now.getDayOfMonth(),
                (int)(Math.random() * 1000));
    }

    private PedidoDTO convertToDTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setNumeroPedido(pedido.getNumeroPedido());
        dto.setFechaPedido(pedido.getFechaPedido());
        dto.setEstado(pedido.getEstado().name());
        dto.setTotal(pedido.getTotal());
        dto.setNotas(pedido.getNotas());
        dto.setClienteNombre(pedido.getCliente().getNombre());
        dto.setClienteEmail(pedido.getCliente().getEmail());
        dto.setVendedorNombre(pedido.getVendedor().getNombre());
        dto.setVendedorEmail(pedido.getVendedor().getEmail());

        List<DetallePedidoDTO> detallesDTO = detallePedidoRepository.findByPedidoId(pedido.getId()).stream()
                .map(detalle -> {
                    DetallePedidoDTO detalleDTO = new DetallePedidoDTO();
                    detalleDTO.setId(detalle.getId());
                    detalleDTO.setCantidad(detalle.getCantidad());
                    detalleDTO.setPrecioUnitario(detalle.getPrecioUnitario());
                    detalleDTO.setSubtotal(detalle.getSubtotal());
                    detalleDTO.setProductoNombre(detalle.getProducto().getNombre());
                    detalleDTO.setProductoId(detalle.getProducto().getId());
                    return detalleDTO;
                })
                .collect(Collectors.toList());

        dto.setDetalles(detallesDTO);
        return dto;
    }
}
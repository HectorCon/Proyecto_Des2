package com.proyecto.web.proyecto_des_web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Long id;
    private String numeroPedido;
    private LocalDateTime fechaPedido;
    private String estado;
    private BigDecimal total;
    private String notas;
    private String clienteNombre;
    private String clienteEmail;
    private String vendedorNombre;
    private String vendedorEmail;
    private List<DetallePedidoDTO> detalles;
}
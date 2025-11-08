package com.proyecto.web.proyecto_des_web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReunionDTO {
    private Long id;
    private LocalDateTime fechaHora;
    private String direccion;
    private String estado;
    private String notas;
    private String clienteNombre;
    private String clienteEmail;
    private String vendedorNombre;
    private String vendedorEmail;
    private String productoNombre;
    private Long productoId;
}
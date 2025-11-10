package com.proyecto.web.proyecto_des_web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendedorDTO {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String codigo;
    private String especialidad;
    private BigDecimal metaMensual;
    private Double comisionPorcentaje;
    private Boolean activo;
    private LocalDateTime fechaIngreso;
    private String notas;
    private Integer totalPedidos;
    private Integer totalReuniones;
    private String ultimaVenta;
}
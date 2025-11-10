package com.proyecto.web.proyecto_des_web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private String empresa;
    private String tipoCliente; // String para facilitar la serialización JSON
    private Boolean activo;
    private LocalDateTime fechaRegistro;
    private String notas;
    private Integer totalPedidos;
    private String ultimoPedido;
}
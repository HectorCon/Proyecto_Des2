package com.proyecto.web.proyecto_des_web.dto;

import lombok.Data;

@Data
public class StockOperationDTO {
    private Long productoId;
    private Integer cantidad;
    private String operacion; // "SET", "ADD", "SUBTRACT"
}
package com.proyecto.web.proyecto_des_web.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateProductoDTO {
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private Long categoriaId;
    private Boolean requiereReunion;
}

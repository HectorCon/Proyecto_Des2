package com.proyecto.web.proyecto_des_web.dto;

import lombok.Data;
import java.util.List;

@Data
public class BulkStockUpdateDTO {
    private List<StockOperationDTO> operaciones;
}
package com.proyecto.web.proyecto_des_web.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vendedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vendedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column
    private String telefono;
    
    @Column
    private String codigo; // Código único del vendedor
    
    @Column
    private String especialidad; // Área de especialización
    
    @Column(name = "meta_mensual", precision = 10, scale = 2)
    private BigDecimal metaMensual;
    
    @Column(name = "comision_porcentaje")
    private Double comisionPorcentaje;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    @Column(name = "fecha_ingreso")
    private LocalDateTime fechaIngreso;
    
    @Column(length = 1000)
    private String notas;
    
    // Relación con pedidos
    @OneToMany(mappedBy = "vendedor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Pedido> pedidos;
    
    // Relación con reuniones
    @OneToMany(mappedBy = "vendedor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Reunion> reuniones;
    
    // Relación con productos asignados
    @ManyToMany(mappedBy = "vendedores", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Producto> productosAsignados;
    
    @PrePersist
    public void prePersist() {
        if (fechaIngreso == null) {
            fechaIngreso = LocalDateTime.now();
        }
        if (comisionPorcentaje == null) {
            comisionPorcentaje = 5.0; // 5% por defecto
        }
    }
}
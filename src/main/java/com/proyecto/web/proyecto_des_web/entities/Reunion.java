package com.proyecto.web.proyecto_des_web.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reuniones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reunion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;
    
    @Column(nullable = false)
    private String direccion;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReunion estado;
    
    @Column(length = 2000)
    private String notas;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Vendedor vendedor;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @PrePersist
    public void prePersist() {
        if (estado == null) {
            estado = EstadoReunion.PROGRAMADA;
        }
    }
    
    public enum EstadoReunion {
        PROGRAMADA,
        EN_PROCESO,
        COMPLETADA,
        CANCELADA
    }
}
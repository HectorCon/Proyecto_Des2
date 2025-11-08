package com.proyecto.web.proyecto_des_web.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Table(name = "categorias_productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaProducto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(length = 1000)
    private String descripcion;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "giro_negocio_id", nullable = false)
    private GiroNegocio giroNegocio;
    
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Producto> productos;
}
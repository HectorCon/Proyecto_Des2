package com.proyecto.web.proyecto_des_web.entities;

public enum TipoCliente {
    PERSONA("Persona Natural"),
    EMPRESA("Empresa"),
    DISTRIBUIDOR("Distribuidor"),
    MAYORISTA("Mayorista"),
    MINORISTA("Minorista");
    
    private final String descripcion;
    
    TipoCliente(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}
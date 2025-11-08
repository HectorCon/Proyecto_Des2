package com.proyecto.web.proyecto_des_web.dto.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponse {
    private String mensaje = "Login exitoso";
    private Long usuarioId;
    private String nombre;
    private String email;
    private String rol;
    private boolean activo;

    public LoginResponse(String token, Long usuarioId, String nombre, String email, String rol, boolean activo) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.activo = activo;
    }
}
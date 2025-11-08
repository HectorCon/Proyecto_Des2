package com.proyecto.web.proyecto_des_web.services;

import com.proyecto.web.proyecto_des_web.dto.auth.LoginRequest;
import com.proyecto.web.proyecto_des_web.dto.auth.LoginResponse;
import com.proyecto.web.proyecto_des_web.dto.auth.RegisterRequest;
import com.proyecto.web.proyecto_des_web.entities.Role;
import com.proyecto.web.proyecto_des_web.entities.Usuario;
import com.proyecto.web.proyecto_des_web.repositories.RoleRepository;
import com.proyecto.web.proyecto_des_web.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;

    public LoginResponse login(LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(request.getEmail());
        
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        
        Usuario usuario = usuarioOpt.get();
        
        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }
        
        // Verificar contraseña (comparación directa por ahora)
        if (!usuario.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }
        
        // Retornar información del usuario sin token
        return new LoginResponse(
            null, // Sin token
            usuario.getId(),
            usuario.getNombre(),
            usuario.getEmail(),
            usuario.getRol().getNombre(),
            usuario.getActivo()
        );
    }

    public LoginResponse register(RegisterRequest request) {
        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        
        // Obtener rol
        Role rol = roleRepository.findById(request.getRolId())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        
        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(request.getPassword());
        usuario.setTelefono(request.getTelefono());
        usuario.setDireccion(request.getDireccion());
        usuario.setRol(rol);
        usuario.setActivo(true);
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        // Retornar información del usuario sin token
        return new LoginResponse(
            null, // Sin token
            usuarioGuardado.getId(),
            usuarioGuardado.getNombre(),
            usuarioGuardado.getEmail(),
            usuarioGuardado.getRol().getNombre(),
            usuarioGuardado.getActivo()
        );
    }
}
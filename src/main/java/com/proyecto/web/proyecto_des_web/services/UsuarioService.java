package com.proyecto.web.proyecto_des_web.services;

import com.proyecto.web.proyecto_des_web.entities.Usuario;
import com.proyecto.web.proyecto_des_web.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> findVendedores() {
        return usuarioRepository.findVendedores();
    }

    public List<Usuario> findClientes() {
        return usuarioRepository.findClientes();
    }

    public List<Usuario> findByRol(String rolNombre) {
        return usuarioRepository.findByRolNombreAndActivoTrue(rolNombre);
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario activarDesactivar(Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setActivo(!usuario.getActivo());
            return usuarioRepository.save(usuario);
        }
        throw new RuntimeException("Usuario no encontrado");
    }
}
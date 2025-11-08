package com.proyecto.web.proyecto_des_web.repositories;

import com.proyecto.web.proyecto_des_web.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<Usuario> findByActivoTrue();
    
    @Query("SELECT u FROM Usuario u WHERE u.rol.nombre = :rolNombre AND u.activo = true")
    List<Usuario> findByRolNombreAndActivoTrue(@Param("rolNombre") String rolNombre);
    
    @Query("SELECT u FROM Usuario u WHERE u.rol.nombre = 'CLIENTE' AND u.activo = true")
    List<Usuario> findClientes();
    
    @Query("SELECT u FROM Usuario u WHERE u.rol.nombre = 'VENDEDOR' AND u.activo = true")
    List<Usuario> findVendedores();
    
    @Query("SELECT u FROM Usuario u WHERE u.rol.nombre = 'ADMIN' AND u.activo = true")
    List<Usuario> findAdministradores();
}
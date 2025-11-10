package com.proyecto.web.proyecto_des_web.controllers;

import com.proyecto.web.proyecto_des_web.entities.*;
import com.proyecto.web.proyecto_des_web.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/datos-prueba")
@RequiredArgsConstructor
public class DatosPruebaController {

    private final ClienteRepository clienteRepository;
    private final VendedorRepository vendedorRepository;
    private final ProductoRepository productoRepository;
    private final GiroNegocioRepository giroNegocioRepository;
    private final CategoriaProductoRepository categoriaRepository;

    @PostMapping("/insertar")
    public ResponseEntity<Map<String, String>> insertarDatosPrueba() {
        try {
            // Crear giro de negocio
            GiroNegocio giro = new GiroNegocio();
            giro.setNombre("Electrónicos");
            giro.setDescripcion("Productos electrónicos y tecnológicos");
            giro.setActivo(true);
            giro = giroNegocioRepository.save(giro);

            // Crear categoría
            CategoriaProducto categoria = new CategoriaProducto();
            categoria.setNombre("Smartphones");
            categoria.setDescripcion("Teléfonos inteligentes");
            categoria.setGiroNegocio(giro);
            categoria = categoriaRepository.save(categoria);

            // Crear productos
            Producto producto1 = new Producto();
            producto1.setNombre("iPhone 15");
            producto1.setDescripcion("Smartphone Apple última generación");
            producto1.setPrecio(new BigDecimal("1299.99"));
            producto1.setStock(50);
            producto1.setRequiereReunion(false);
            producto1.setActivo(true);
            producto1.setCategoria(categoria);
            productoRepository.save(producto1);

            Producto producto2 = new Producto();
            producto2.setNombre("Samsung Galaxy S24");
            producto2.setDescripcion("Smartphone Samsung premium");
            producto2.setPrecio(new BigDecimal("1199.99"));
            producto2.setStock(30);
            producto2.setRequiereReunion(false);
            producto2.setActivo(true);
            producto2.setCategoria(categoria);
            productoRepository.save(producto2);

            // Crear clientes
            for (int i = 1; i <= 10; i++) {
                Cliente cliente = new Cliente();
                cliente.setNombre("Cliente " + i);
                cliente.setEmail("cliente" + i + "@test.com");
                cliente.setTelefono("555-000" + i);
                cliente.setDireccion("Dirección " + i);
                cliente.setEmpresa("Empresa " + i);
                cliente.setTipoCliente("EMPRESA");
                cliente.setActivo(true);
                cliente.setFechaRegistro(LocalDateTime.now());
                cliente.setNotas("Cliente de prueba número " + i);
                clienteRepository.save(cliente);
            }

            // Crear vendedores
            for (int i = 1; i <= 5; i++) {
                Vendedor vendedor = new Vendedor();
                vendedor.setNombre("Vendedor " + i);
                vendedor.setEmail("vendedor" + i + "@empresa.com");
                vendedor.setTelefono("555-200" + i);
                vendedor.setCodigo("VEND00" + i);
                vendedor.setEspecialidad("Tecnología y Electrónicos");
                vendedor.setMetaMensual(new BigDecimal("50000.00"));
                vendedor.setComisionPorcentaje(5.5);
                vendedor.setActivo(true);
                vendedor.setFechaIngreso(LocalDateTime.now());
                vendedor.setNotas("Vendedor de prueba número " + i);
                vendedorRepository.save(vendedor);
            }

            return ResponseEntity.ok(Map.of("mensaje", "Datos de prueba insertados exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al insertar datos: " + e.getMessage()));
        }
    }
}
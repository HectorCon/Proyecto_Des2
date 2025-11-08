package com.proyecto.web.proyecto_des_web.services;

import com.proyecto.web.proyecto_des_web.dto.ProductoDTO;
import com.proyecto.web.proyecto_des_web.entities.Producto;
import com.proyecto.web.proyecto_des_web.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public List<ProductoDTO> findAll() {
        return productoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> findActivos() {
        return productoRepository.findByActivoTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    public List<ProductoDTO> findByCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> findByGiroNegocio(Long giroId) {
        return productoRepository.findByGiroNegocioId(giroId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> findQueRequierenReunion() {
        return productoRepository.findByRequiereReunion(true).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> findConStock() {
        return productoRepository.findProductosConStock().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> findConStockBajo(Integer minimo) {
        return productoRepository.findProductosConStockBajo(minimo).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    public Producto actualizarStock(Long id, Integer nuevoStock) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setStock(nuevoStock);
            return productoRepository.save(producto);
        }
        throw new RuntimeException("Producto no encontrado");
    }

    private ProductoDTO convertToDTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setRequiereReunion(producto.getRequiereReunion());
        dto.setActivo(producto.getActivo());
        dto.setCategoriaNombre(producto.getCategoria().getNombre());
        dto.setGiroNegocio(producto.getCategoria().getGiroNegocio().getNombre());
        return dto;
    }
}
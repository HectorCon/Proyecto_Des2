package com.proyecto.web.proyecto_des_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProyectoDesWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoDesWebApplication.class, args);
		System.out.println("=== Aplicación iniciada exitosamente ===");
		System.out.println("API REST disponible en: http://localhost:8080");
		System.out.println("Documentación de endpoints:");
		System.out.println("- Usuarios: /api/usuarios");
		System.out.println("- Productos: /api/productos");
		System.out.println("- Pedidos: /api/pedidos");
		System.out.println("- Reuniones: /api/reuniones");
		System.out.println("- Giros de Negocio: /api/giros-negocio");
		System.out.println("- Categorías: /api/categorias-productos");
		System.out.println("- Reportes: /api/reportes");
		System.out.println("========================================");
	}

}

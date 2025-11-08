# Sistema de Gestión Empresarial

API REST desarrollada con Spring Boot para la gestión integral de negocios, incluyendo gestión de usuarios, productos, pedidos y reuniones.

## Funcionalidades Principales

### 1. Gestión de Usuarios
- Administradores, vendedores y clientes
- Autenticación y autorización por roles
- Activación/desactivación de usuarios

### 2. Elaborar Pedidos
- Creación de pedidos con múltiples productos
- Control de inventario automático
- Estados: PENDIENTE, EN_PROCESO, ENTREGADO, CANCELADO

### 3. Seguimiento de Pedidos
- Consultas por cliente, vendedor y estado
- Historial completo de pedidos
- Reportes de ventas por período

### 4. Administración de Inventarios
- Control de stock en tiempo real
- Alertas de stock bajo
- Categorización por giros de negocio

### 5. Gestión de Reuniones
- Programación de citas para productos que requieren demostración
- Asignación de vendedores específicos
- Estados: PROGRAMADA, EN_PROCESO, COMPLETADA, CANCELADA

## Estructura del Proyecto

### Entidades Principales
- **Role**: Roles del sistema (ADMIN, VENDEDOR, CLIENTE)
- **Usuario**: Usuarios del sistema con roles asignados
- **GiroNegocio**: Tipos de negocio (Electrónicos, Consultoría, Equipos)
- **CategoriaProducto**: Categorías de productos por giro
- **Producto**: Productos/servicios disponibles
- **Pedido**: Órdenes de compra
- **DetallePedido**: Detalles de cada pedido
- **Reunion**: Citas programadas con clientes

## Endpoints de la API

### Autenticación (`/api/auth`)
- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/register` - Registrar nuevo usuario
- `GET /api/auth/roles` - Listar roles disponibles
- `GET /api/auth/test-credentials` - Credenciales de prueba

### Usuarios (`/api/usuarios`)
- `GET /api/usuarios` - Listar todos los usuarios
- `GET /api/usuarios/{id}` - Obtener usuario por ID
- `GET /api/usuarios/vendedores` - Listar vendedores
- `GET /api/usuarios/clientes` - Listar clientes
- `POST /api/usuarios` - Crear nuevo usuario
- `PUT /api/usuarios/{id}` - Actualizar usuario
- `PUT /api/usuarios/{id}/toggle-activo` - Activar/desactivar usuario

### Productos (`/api/productos`)
- `GET /api/productos` - Listar todos los productos
- `GET /api/productos/activos` - Productos activos
- `GET /api/productos/con-stock` - Productos con stock
- `GET /api/productos/stock-bajo` - Productos con stock bajo
- `GET /api/productos/categoria/{id}` - Productos por categoría
- `POST /api/productos` - Crear producto
- `PUT /api/productos/{id}/stock` - Actualizar stock

### Pedidos (`/api/pedidos`)
- `GET /api/pedidos` - Listar todos los pedidos
- `GET /api/pedidos/cliente/{id}` - Pedidos por cliente
- `GET /api/pedidos/vendedor/{id}` - Pedidos por vendedor
- `GET /api/pedidos/estado/{estado}` - Pedidos por estado
- `POST /api/pedidos` - Crear nuevo pedido
- `PUT /api/pedidos/{id}/estado` - Actualizar estado del pedido

### Reuniones (`/api/reuniones`)
- `GET /api/reuniones` - Listar todas las reuniones
- `GET /api/reuniones/proximas` - Reuniones próximas
- `GET /api/reuniones/cliente/{id}` - Reuniones por cliente
- `GET /api/reuniones/vendedor/{id}` - Reuniones por vendedor
- `POST /api/reuniones` - Programar nueva reunión
- `PUT /api/reuniones/{id}/estado` - Actualizar estado

### Reportes (`/api/reportes`)
- `GET /api/reportes/dashboard` - Dashboard principal
- `GET /api/reportes/ventas` - Reporte de ventas por período
- `GET /api/reportes/inventario` - Reporte de inventario
- `GET /api/reportes/reuniones` - Reporte de reuniones

## Autenticación Básica

### Sistema Simple
La aplicación utiliza autenticación básica sin tokens:

- **Login**: Verificación de email y password
- **Respuesta**: Información del usuario autenticado
- **Sin tokens**: No requiere headers especiales para requests posteriores

### Credenciales de Prueba
```json
{
  "admin": {
    "email": "admin@empresa.com",
    "password": "admin123",
    "rol": "ADMIN"
  },
  "vendedor": {
    "email": "juan.vendedor@empresa.com", 
    "password": "vendedor123",
    "rol": "VENDEDOR"
  },
  "cliente": {
    "email": "maria.cliente@gmail.com",
    "password": "cliente123", 
    "rol": "CLIENTE"
  }
}
```

### Flujo de Autenticación
1. **Login**: `POST /api/auth/login` con email y password
2. **Respuesta**: Información del usuario si las credenciales son válidas
3. **Uso**: Todos los endpoints están abiertos (sin restricciones de autenticación)

## Configuración de Base de Datos

### Credenciales PostgreSQL
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/proyecto
spring.datasource.username=postgres
spring.datasource.password=manager1
```

### Comandos Docker
```bash
docker pull postgres:16
docker run -d --name pg16 -e POSTGRES_PASSWORD=manager1 -e POSTGRES_DB=proyecto -p 5432:5432 -v pgdata16:/var/lib/postgresql/data postgres:16
```

## Datos de Prueba

Al iniciar la aplicación se insertan automáticamente datos de ejemplo:

### Usuarios
- **Admin**: admin@empresa.com / admin123
- **Vendedor**: juan.vendedor@empresa.com / vendedor123
- **Clientes**: maria.cliente@gmail.com, pedro.cliente@gmail.com / cliente123

### Productos/Servicios
- Smartphones (iPhone 15, Samsung Galaxy)
- Laptops (MacBook Pro, Dell XPS)
- Servicios de consultoría
- Equipos industriales

### Pedidos y Reuniones
- Ejemplos de pedidos en diferentes estados
- Reuniones programadas para productos que requieren demostración

## Cómo Usar

### 1. Iniciar Base de Datos
```bash
docker run -d --name pg16 -e POSTGRES_PASSWORD=manager1 -e POSTGRES_DB=proyecto -p 5432:5432 postgres:16
```

### 2. Ejecutar la Aplicación
```bash
./mvnw spring-boot:run
```

### 3. Acceder a la API
- La aplicación estará disponible en `http://localhost:8080`
- Configurada para CORS con frontend en `http://localhost:5173`

## Ejemplos de Uso

### Iniciar Sesión
```json
POST /api/auth/login
{
  "email": "admin@empresa.com",
  "password": "admin123"
}

// Respuesta
{
  "mensaje": "Login exitoso",
  "usuarioId": 1,
  "nombre": "Admin Sistema",
  "email": "admin@empresa.com",
  "rol": "ADMIN",
  "activo": true
}
```

### Usar la API
```bash
# Todos los endpoints están disponibles sin autenticación
curl http://localhost:8080/api/usuarios
curl http://localhost:8080/api/productos
```

### Crear un Pedido
```json
POST /api/pedidos
{
  "clienteId": 3,
  "vendedorId": 2,
  "notas": "Pedido urgente",
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 2,
      "precioUnitario": 1299.99
    }
  ]
}
```

### Programar una Reunión
```json
POST /api/reuniones
{
  "clienteId": 3,
  "vendedorId": 2,
  "productoId": 6,
  "fechaHora": "2024-11-20T10:00:00",
  "direccion": "Oficina Cliente",
  "notas": "Demostración de consultoría digital"
}
```

## Características Técnicas

- **Framework**: Spring Boot 3.5.7
- **Base de Datos**: PostgreSQL 16
- **ORM**: JPA/Hibernate
- **Java**: 17
- **Documentación**: Lombok para reducir boilerplate
- **CORS**: Configurado para frontend en localhost:5173
- **Transacciones**: Manejo automático con @Transactional

## Arquitectura

- **Controller**: Manejo de endpoints REST
- **Service**: Lógica de negocio
- **Repository**: Acceso a datos con JPA
- **Entity**: Modelo de datos
- **DTO**: Objetos de transferencia de datos
- **Config**: Configuraciones (CORS, etc.)

Esta aplicación proporciona una base sólida para un sistema de gestión empresarial completo, con todas las funcionalidades solicitadas implementadas siguiendo buenas prácticas de desarrollo.
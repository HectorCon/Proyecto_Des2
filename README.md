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
- **Usuario**: Usuarios administrativos del sistema
- **Cliente**: Entidad especializada para gestión de clientes
  - Tipos: PERSONA, EMPRESA, DISTRIBUIDOR, MAYORISTA, MINORISTA
  - Campos específicos: empresa, tipoCliente, fechaRegistro, notas
- **Vendedor**: Entidad especializada para gestión de vendedores
  - Campos específicos: metaMensual, comisionPorcentaje, especialidad, codigo
  - Relación con productos asignados
- **GiroNegocio**: Tipos de negocio (Electrónicos, Consultoría, Equipos)
- **CategoriaProducto**: Categorías de productos por giro
- **Producto**: Productos/servicios disponibles (asignados a vendedores)
- **Pedido**: Órdenes de compra (Cliente → Vendedor)
- **DetallePedido**: Detalles de cada pedido
- **Reunion**: Citas programadas (Cliente ↔ Vendedor)

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

### Clientes (`/api/clientes`)
- `GET /api/clientes` - Listar todos los clientes con estadísticas
- `GET /api/clientes/activos` - Clientes activos
- `GET /api/clientes/{id}` - Obtener cliente por ID
- `GET /api/clientes/para-pedidos` - Clientes activos para formularios de pedidos
- `POST /api/clientes` - Crear nuevo cliente
- `PUT /api/clientes/{id}` - Actualizar cliente existente
- `PUT /api/clientes/{id}/toggle-activo` - Activar/desactivar cliente

### Vendedores (`/api/vendedores`)
- `GET /api/vendedores` - Listar todos los vendedores con estadísticas
- `GET /api/vendedores/activos` - Vendedores activos
- `GET /api/vendedores/{id}` - Obtener vendedor por ID
- `GET /api/vendedores/para-asignacion` - Vendedores activos para asignación

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
  }
}
```

**Nota**: Los clientes y vendedores ahora tienen entidades separadas y se gestionan a través de sus respectivos endpoints `/api/clientes` y `/api/vendedores`.
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

### Usuarios Administrativos
- **Admin**: admin@empresa.com / admin123

### Clientes
- **María González** (EMPRESA): maria.gonzalez@gmail.com - Comercial González SAS
- **Pedro Martínez** (PERSONA): pedro.martinez@gmail.com - Cliente individual
- **Industrias López** (MAYORISTA): compras@industriaslopez.com - Cliente mayorista
- **Tech Solutions** (DISTRIBUIDOR): contacto@techsolutions.com - Distribuidor tecnológico
- **Ana Rodríguez** (MINORISTA): ana.rodriguez@personal.com - Cliente minorista

### Vendedores
- **Juan Carlos Pérez** (VEND001): Especialista en Tecnología y Electrónicos
- **Laura Fernández** (VEND002): Especialista en Servicios de Consultoría  
- **Carlos Ramírez** (VEND003): Especialista en Equipos Industriales

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

# CLIENTES
# Obtener clientes para formulario de pedidos
curl http://localhost:8080/api/clientes/para-pedidos

# Listar todos los clientes
curl http://localhost:8080/api/clientes

# Obtener cliente específico
curl http://localhost:8080/api/clientes/1

# Crear nuevo cliente
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Carlos López",
    "email": "carlos.lopez@empresa.com",
    "telefono": "555-1111",
    "direccion": "Calle Nueva 123",
    "empresa": "López & Asociados",
    "tipoCliente": "EMPRESA",
    "notas": "Cliente corporativo"
  }'

# VENDEDORES
# Obtener vendedores activos
curl http://localhost:8080/api/vendedores/activos

# Listar todos los vendedores
curl http://localhost:8080/api/vendedores

# Crear nuevo vendedor
curl -X POST http://localhost:8080/api/vendedores \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Sofia Ruiz",
    "email": "sofia.ruiz@empresa.com",
    "telefono": "555-3001",
    "codigo": "VEND004",
    "especialidad": "Equipos Médicos",
    "metaMensual": 60000.00,
    "comisionPorcentaje": 7.0
  }'

# PRODUCTOS
# Listar productos activos
curl http://localhost:8080/api/productos/activos

# Productos con stock bajo
curl http://localhost:8080/api/productos/stock-bajo

# Crear producto
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Surface Laptop",
    "descripcion": "Laptop Microsoft Surface",
    "precio": 1899.99,
    "stock": 15,
    "categoriaId": 2,
    "requiereReunion": false
  }'

# PEDIDOS
# Crear pedido
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "vendedorId": 1,
    "notas": "Pedido empresarial urgente",
    "detalles": [
      {
        "productoId": 1,
        "cantidad": 3,
        "precioUnitario": 1299.99
      }
    ]
  }'

# Pedidos por cliente
curl http://localhost:8080/api/pedidos/cliente/1

# Cambiar estado de pedido
curl -X PUT http://localhost:8080/api/pedidos/1/estado \
  -H "Content-Type: application/json" \
  -d '{"estado": "EN_PROCESO"}'

# REUNIONES
# Programar reunión
curl -X POST http://localhost:8080/api/reuniones \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "vendedorId": 2,
    "productoId": 6,
    "fechaHora": "2025-11-25T14:00:00",
    "direccion": "Oficina Central",
    "notas": "Consultoría estratégica"
  }'

# Reuniones por vendedor
curl http://localhost:8080/api/reuniones/vendedor/1

# REPORTES
# Dashboard principal
curl http://localhost:8080/api/reportes/dashboard

# Reporte de ventas
curl "http://localhost:8080/api/reportes/ventas?fechaInicio=2025-11-01&fechaFin=2025-11-30"
```

## Endpoint para Crear Clientes

### `POST /api/clientes` - Crear Nuevo Cliente

**URL:** `http://localhost:8080/api/clientes`  
**Método:** `POST`  
**Content-Type:** `application/json`

#### Request Body (JSON)
```json
{
  "nombre": "Juan Pérez",
  "email": "juan.perez@gmail.com", 
  "telefono": "555-1234",
  "direccion": "Av. Libertador 456",
  "empresa": "Pérez y Asociados",
  "tipoCliente": "EMPRESA",
  "notas": "Nuevo cliente empresarial"
}
```

#### Campos del Request
| Campo | Tipo | Requerido | Descripción | Valores Válidos |
|-------|------|-----------|-------------|-----------------|
| `nombre` | String | ✅ Sí | Nombre completo del cliente | Cualquier texto |
| `email` | String | ✅ Sí | Email único del cliente | Formato email válido |
| `telefono` | String | ❌ No | Número de teléfono | Cualquier texto |
| `direccion` | String | ❌ No | Dirección física | Cualquier texto |
| `empresa` | String | ❌ No | Nombre de la empresa (si aplica) | Cualquier texto |
| `tipoCliente` | String | ❌ No | Tipo de cliente | `PERSONA`, `EMPRESA`, `DISTRIBUIDOR`, `MAYORISTA`, `MINORISTA` |
| `notas` | String | ❌ No | Observaciones adicionales | Máximo 1000 caracteres |

#### Response Exitoso (201 Created)
```json
{
  "id": 6,
  "nombre": "Juan Pérez",
  "email": "juan.perez@gmail.com",
  "telefono": "555-1234",
  "direccion": "Av. Libertador 456",
  "empresa": "Pérez y Asociados",
  "tipoCliente": "EMPRESA",
  "activo": true,
  "fechaRegistro": "2025-11-09T22:00:00",
  "notas": "Nuevo cliente empresarial"
}
```

#### Respuestas de Error

**400 Bad Request - Email duplicado:**
```json
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Ya existe un cliente con el email: juan.perez@gmail.com",
  "path": "/api/clientes"
}
```

**400 Bad Request - Datos inválidos:**
```json
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El nombre es requerido",
  "path": "/api/clientes"
}
```

#### Ejemplo con cURL
```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Carlos López",
    "email": "carlos.lopez@empresa.com",
    "telefono": "555-1111",
    "direccion": "Calle Nueva 123",
    "empresa": "López & Asociados",
    "tipoCliente": "EMPRESA",
    "notas": "Cliente corporativo"
  }'
```

#### Ejemplo con JavaScript/Fetch
```javascript
const nuevoCliente = {
  nombre: "Ana García",
  email: "ana.garcia@personal.com",
  telefono: "555-2222", 
  direccion: "Barrio Centro 789",
  tipoCliente: "PERSONA",
  notas: "Cliente individual"
};

fetch('http://localhost:8080/api/clientes', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify(nuevoCliente)
})
.then(response => response.json())
.then(data => console.log('Cliente creado:', data))
.catch(error => console.error('Error:', error));
```

#### Notas Importantes
- El campo `email` debe ser único en el sistema
- El campo `activo` se establece automáticamente como `true`
- La `fechaRegistro` se establece automáticamente con la fecha/hora actual
- Si no se especifica `tipoCliente`, se establece como "PERSONA" por defecto
- Los clientes creados estarán inmediatamente disponibles para crear pedidos y reuniones

### Obtener Clientes para Pedidos
```json
GET /api/clientes/para-pedidos

// Respuesta exitosa
[
  {
    "id": 1,
    "nombre": "María González",
    "email": "maria.gonzalez@gmail.com",
    "telefono": "555-1001",
    "empresa": "Comercial González SAS",
    "tipoCliente": "EMPRESA",
    "activo": true
  },
  {
    "id": 2,
    "nombre": "Pedro Martínez",
    "email": "pedro.martinez@gmail.com",
    "telefono": "555-1002",
    "tipoCliente": "PERSONA",
    "activo": true
  }
]

// Respuesta fallida
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Error al obtener clientes",
  "path": "/api/clientes/para-pedidos"
}
```

### Listar Todos los Clientes
```json
GET /api/clientes

// Respuesta exitosa
[
  {
    "id": 1,
    "nombre": "María González",
    "email": "maria.gonzalez@gmail.com",
    "telefono": "555-1001",
    "direccion": "Calle Principal 123",
    "empresa": "Comercial González SAS",
    "tipoCliente": "EMPRESA",
    "activo": true,
    "fechaRegistro": "2025-11-09T10:00:00",
    "notas": "Cliente frecuente - descuentos especiales"
  }
]

// Respuesta fallida
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 500,
  "error": "Internal Server Error", 
  "message": "Error al obtener lista de clientes",
  "path": "/api/clientes"
}
```

### Obtener Cliente por ID
```json
GET /api/clientes/1

// Respuesta exitosa
{
  "id": 1,
  "nombre": "María González",
  "email": "maria.gonzalez@gmail.com",
  "telefono": "555-1001",
  "direccion": "Calle Principal 123",
  "empresa": "Comercial González SAS",
  "tipoCliente": "EMPRESA",
  "activo": true,
  "fechaRegistro": "2025-11-09T10:00:00",
  "notas": "Cliente frecuente - descuentos especiales"
}

// Respuesta fallida - Cliente no encontrado
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Cliente no encontrado con ID: 999",
  "path": "/api/clientes/999"
}
```

### Crear Cliente
```json
POST /api/clientes
Content-Type: application/json

// Request
{
  "nombre": "Juan Pérez",
  "email": "juan.perez@gmail.com",
  "telefono": "555-1234",
  "direccion": "Av. Libertador 456",
  "empresa": "Pérez y Asociados",
  "tipoCliente": "EMPRESA",
  "notas": "Nuevo cliente empresarial"
}

// Respuesta exitosa
{
  "id": 6,
  "nombre": "Juan Pérez",
  "email": "juan.perez@gmail.com",
  "telefono": "555-1234",
  "direccion": "Av. Libertador 456",
  "empresa": "Pérez y Asociados",
  "tipoCliente": "EMPRESA",
  "activo": true,
  "fechaRegistro": "2025-11-09T22:00:00",
  "notas": "Nuevo cliente empresarial"
}

// Respuesta fallida - Email duplicado
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Ya existe un cliente con el email: juan.perez@gmail.com",
  "path": "/api/clientes"
}
```

### Actualizar Cliente
```json
PUT /api/clientes/1
Content-Type: application/json

// Request
{
  "nombre": "María González Actualizada",
  "email": "maria.gonzalez@gmail.com",
  "telefono": "555-1001",
  "direccion": "Nueva Dirección 789",
  "empresa": "Comercial González SAS",
  "tipoCliente": "EMPRESA",
  "notas": "Cliente VIP con descuentos especiales"
}

// Respuesta exitosa
{
  "id": 1,
  "nombre": "María González Actualizada",
  "email": "maria.gonzalez@gmail.com",
  "telefono": "555-1001",
  "direccion": "Nueva Dirección 789",
  "empresa": "Comercial González SAS",
  "tipoCliente": "EMPRESA",
  "activo": true,
  "fechaRegistro": "2025-11-09T10:00:00",
  "notas": "Cliente VIP con descuentos especiales"
}

// Respuesta fallida - Cliente no encontrado
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Cliente no encontrado con ID: 999",
  "path": "/api/clientes/999"
}
```

### Activar/Desactivar Cliente
```json
PUT /api/clientes/1/toggle-activo

// Respuesta exitosa
{
  "mensaje": "Cliente desactivado exitosamente",
  "clienteId": 1,
  "activo": false
}

// Respuesta fallida
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Cliente no encontrado con ID: 999",
  "path": "/api/clientes/999/toggle-activo"
}
```

### Listar Vendedores Activos
```json
GET /api/vendedores/activos

// Respuesta exitosa
[
  {
    "id": 1,
    "nombre": "Juan Carlos Pérez",
    "email": "juan.perez@empresa.com",
    "telefono": "555-2001",
    "codigo": "VEND001",
    "especialidad": "Tecnología y Electrónicos",
    "metaMensual": 50000.00,
    "comisionPorcentaje": 5.50,
    "activo": true,
    "fechaIngreso": "2025-11-09T10:00:00"
  }
]

// Respuesta fallida
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Error al obtener vendedores activos",
  "path": "/api/vendedores/activos"
}
```

### Crear Vendedor
```json
POST /api/vendedores
Content-Type: application/json

// Request
{
  "nombre": "Ana García",
  "email": "ana.garcia@empresa.com",
  "telefono": "555-2004",
  "codigo": "VEND004",
  "especialidad": "Productos Farmacéuticos",
  "metaMensual": 40000.00,
  "comisionPorcentaje": 6.0,
  "notas": "Nueva vendedora especializada en farmacia"
}

// Respuesta exitosa
{
  "id": 4,
  "nombre": "Ana García",
  "email": "ana.garcia@empresa.com",
  "telefono": "555-2004",
  "codigo": "VEND004",
  "especialidad": "Productos Farmacéuticos",
  "metaMensual": 40000.00,
  "comisionPorcentaje": 6.0,
  "activo": true,
  "fechaIngreso": "2025-11-09T22:00:00",
  "notas": "Nueva vendedora especializada en farmacia"
}

// Respuesta fallida - Código duplicado
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Ya existe un vendedor con el código: VEND004",
  "path": "/api/vendedores"
}
```

### Crear un Pedido
```json
POST /api/pedidos
Content-Type: application/json

// Request
{
  "clienteId": 1,
  "vendedorId": 1,
  "notas": "Pedido urgente para cliente empresarial",
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 2,
      "precioUnitario": 1299.99
    },
    {
      "productoId": 3,
      "cantidad": 1,
      "precioUnitario": 2499.99
    }
  ]
}

// Respuesta exitosa
{
  "id": 4,
  "numeroPedido": "PED-2025-004",
  "fechaPedido": "2025-11-09T22:00:00",
  "estado": "PENDIENTE",
  "total": 5099.97,
  "notas": "Pedido urgente para cliente empresarial",
  "cliente": {
    "id": 1,
    "nombre": "María González",
    "email": "maria.gonzalez@gmail.com"
  },
  "vendedor": {
    "id": 1,
    "nombre": "Juan Carlos Pérez",
    "codigo": "VEND001"
  },
  "detalles": [
    {
      "id": 7,
      "cantidad": 2,
      "precioUnitario": 1299.99,
      "subtotal": 2599.98,
      "producto": {
        "id": 1,
        "nombre": "iPhone 15"
      }
    },
    {
      "id": 8,
      "cantidad": 1,
      "precioUnitario": 2499.99,
      "subtotal": 2499.99,
      "producto": {
        "id": 3,
        "nombre": "MacBook Pro M3"
      }
    }
  ]
}

// Respuesta fallida - Cliente no encontrado
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Cliente no encontrado con ID: 999",
  "path": "/api/pedidos"
}

// Respuesta fallida - Stock insuficiente
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Stock insuficiente para el producto: iPhone 15. Stock disponible: 1, Cantidad solicitada: 5",
  "path": "/api/pedidos"
}
```

### Listar Pedidos
```json
GET /api/pedidos

// Respuesta exitosa
[
  {
    "id": 1,
    "numeroPedido": "PED-2025-001",
    "fechaPedido": "2025-11-01T10:00:00",
    "estado": "PENDIENTE",
    "total": 2599.98,
    "notas": "Pedido de smartphones para empresa",
    "cliente": {
      "id": 1,
      "nombre": "María González"
    },
    "vendedor": {
      "id": 1,
      "nombre": "Juan Carlos Pérez"
    }
  }
]

// Respuesta fallida
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Error al obtener pedidos",
  "path": "/api/pedidos"
}
```

### Actualizar Estado de Pedido
```json
PUT /api/pedidos/1/estado
Content-Type: application/json

// Request
{
  "estado": "EN_PROCESO"
}

// Respuesta exitosa
{
  "id": 1,
  "numeroPedido": "PED-2025-001",
  "fechaPedido": "2025-11-01T10:00:00",
  "estado": "EN_PROCESO",
  "total": 2599.98,
  "notas": "Pedido de smartphones para empresa",
  "cliente": {
    "id": 1,
    "nombre": "María González"
  },
  "vendedor": {
    "id": 1,
    "nombre": "Juan Carlos Pérez"
  }
}

// Respuesta fallida - Estado inválido
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Estado inválido: ESTADO_INEXISTENTE. Estados válidos: PENDIENTE, EN_PROCESO, ENTREGADO, CANCELADO",
  "path": "/api/pedidos/1/estado"
}
```

### Programar una Reunión
```json
POST /api/reuniones
Content-Type: application/json

// Request
{
  "clienteId": 1,
  "vendedorId": 2,
  "productoId": 6,
  "fechaHora": "2025-11-20T10:00:00",
  "direccion": "Torre Empresarial Piso 12",
  "notas": "Demostración de consultoría digital para transformación empresarial"
}

// Respuesta exitosa
{
  "id": 4,
  "fechaHora": "2025-11-20T10:00:00",
  "direccion": "Torre Empresarial Piso 12",
  "estado": "PROGRAMADA",
  "notas": "Demostración de consultoría digital para transformación empresarial",
  "cliente": {
    "id": 1,
    "nombre": "María González",
    "email": "maria.gonzalez@gmail.com"
  },
  "vendedor": {
    "id": 2,
    "nombre": "Laura Fernández",
    "especialidad": "Servicios de Consultoría"
  },
  "producto": {
    "id": 6,
    "nombre": "Consultoría Digital",
    "precio": 5000.00
  }
}

// Respuesta fallida - Fecha en el pasado
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "La fecha de la reunión no puede ser en el pasado",
  "path": "/api/reuniones"
}

// Respuesta fallida - Producto no requiere reunión
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El producto 'iPhone 15' no requiere reunión de demostración",
  "path": "/api/reuniones"
}
```

### Listar Reuniones
```json
GET /api/reuniones

// Respuesta exitosa
[
  {
    "id": 1,
    "fechaHora": "2025-11-15T10:00:00",
    "direccion": "Torre Empresarial Piso 12",
    "estado": "PROGRAMADA",
    "notas": "Reunión para demostración de consultoría digital",
    "cliente": {
      "id": 4,
      "nombre": "Tech Solutions"
    },
    "vendedor": {
      "id": 2,
      "nombre": "Laura Fernández"
    },
    "producto": {
      "id": 6,
      "nombre": "Consultoría Digital"
    }
  }
]

// Respuesta fallida
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Error al obtener reuniones",
  "path": "/api/reuniones"
}
```

### Actualizar Estado de Reunión
```json
PUT /api/reuniones/1/estado
Content-Type: application/json

// Request
{
  "estado": "COMPLETADA"
}

// Respuesta exitosa
{
  "id": 1,
  "fechaHora": "2025-11-15T10:00:00",
  "direccion": "Torre Empresarial Piso 12",
  "estado": "COMPLETADA",
  "notas": "Reunión para demostración de consultoría digital",
  "cliente": {
    "id": 4,
    "nombre": "Tech Solutions"
  },
  "vendedor": {
    "id": 2,
    "nombre": "Laura Fernández"
  },
  "producto": {
    "id": 6,
    "nombre": "Consultoría Digital"
  }
}

// Respuesta fallida - Estado inválido
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Estado inválido: ESTADO_INEXISTENTE. Estados válidos: PROGRAMADA, EN_PROCESO, COMPLETADA, CANCELADA",
  "path": "/api/reuniones/1/estado"
}
```

### Listar Productos
```json
GET /api/productos

// Respuesta exitosa
[
  {
    "id": 1,
    "nombre": "iPhone 15",
    "descripcion": "Smartphone Apple última generación",
    "precio": 1299.99,
    "stock": 48,
    "requiereReunion": false,
    "activo": true,
    "categoria": {
      "id": 1,
      "nombre": "Smartphones"
    }
  },
  {
    "id": 6,
    "nombre": "Consultoría Digital",
    "descripcion": "Transformación digital empresarial",
    "precio": 5000.00,
    "stock": 999,
    "requiereReunion": true,
    "activo": true,
    "categoria": {
      "id": 4,
      "nombre": "Consultoría IT"
    }
  }
]

// Respuesta fallida
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Error al obtener productos",
  "path": "/api/productos"
}
```

### Crear Producto
```json
POST /api/productos
Content-Type: application/json

// Request
{
  "nombre": "iPad Pro M4",
  "descripcion": "Tablet profesional Apple con chip M4",
  "precio": 1599.99,
  "stock": 25,
  "categoriaId": 1,
  "requiereReunion": false
}

// Respuesta exitosa
{
  "id": 10,
  "nombre": "iPad Pro M4",
  "descripcion": "Tablet profesional Apple con chip M4",
  "precio": 1599.99,
  "stock": 25,
  "requiereReunion": false,
  "activo": true,
  "categoria": {
    "id": 1,
    "nombre": "Smartphones"
  }
}

// Respuesta fallida - Categoría no encontrada
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Categoría no encontrada con ID: 999",
  "path": "/api/productos"
}
```

### Actualizar Stock de Producto
```json
PUT /api/productos/1/stock
Content-Type: application/json

// Request
{
  "nuevoStock": 75
}

// Respuesta exitosa
{
  "id": 1,
  "nombre": "iPhone 15",
  "descripcion": "Smartphone Apple última generación",
  "precio": 1299.99,
  "stock": 75,
  "requiereReunion": false,
  "activo": true,
  "categoria": {
    "id": 1,
    "nombre": "Smartphones"
  }
}

// Respuesta fallida - Stock negativo
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El stock no puede ser negativo",
  "path": "/api/productos/1/stock"
}
```

### Dashboard Principal
```json
GET /api/reportes/dashboard

// Respuesta exitosa
{
  "totalClientes": 5,
  "clientesActivos": 5,
  "totalVendedores": 3,
  "vendedoresActivos": 3,
  "totalProductos": 9,
  "productosActivos": 9,
  "pedidosHoy": 2,
  "pedidosMes": 15,
  "ventasMes": 125000.50,
  "reunionesPendientes": 3,
  "productosMasVendidos": [
    {
      "productoId": 1,
      "nombre": "iPhone 15",
      "cantidadVendida": 10,
      "ingresoTotal": 12999.90
    }
  ],
  "topVendedores": [
    {
      "vendedorId": 1,
      "nombre": "Juan Carlos Pérez",
      "totalVentas": 50000.00,
      "cantidadPedidos": 8
    }
  ]
}

// Respuesta fallida
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Error al generar dashboard",
  "path": "/api/reportes/dashboard"
}
```

## Códigos de Respuesta HTTP

### Respuestas Exitosas
- **200 OK**: Solicitud exitosa (GET, PUT)
- **201 Created**: Recurso creado exitosamente (POST)
- **204 No Content**: Operación exitosa sin contenido (DELETE)

### Respuestas de Error del Cliente
- **400 Bad Request**: 
  - Datos inválidos en el request
  - Stock insuficiente
  - Email/código duplicado
  - Fecha inválida
- **404 Not Found**:
  - Cliente no encontrado
  - Vendedor no encontrado
  - Producto no encontrado
  - Pedido no encontrado
  - Reunión no encontrada

### Respuestas de Error del Servidor
- **500 Internal Server Error**:
  - Error de base de datos
  - Error interno de la aplicación
  - Error de conectividad

### Estructura de Respuestas de Error
```json
{
  "timestamp": "2025-11-09T22:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Descripción específica del error",
  "path": "/api/endpoint"
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
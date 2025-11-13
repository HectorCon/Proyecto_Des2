# Docker Setup para Proyecto Des Web

Este documento explica cómo ejecutar el proyecto usando Docker Compose.

## Requisitos Previos

- Docker instalado en tu sistema
- Docker Compose instalado

## Archivos Creados

- `Dockerfile`: Configuración para construir la imagen de la aplicación Spring Boot
- `docker-compose.yml`: Configuración de servicios (app + PostgreSQL)
- `.dockerignore`: Archivos a ignorar durante la construcción

## Cómo Ejecutar

### 1. Construir y ejecutar todo el stack

```bash
docker-compose up --build
```

### 2. Ejecutar en segundo plano (detached mode)

```bash
docker-compose up -d --build
```

### 3. Ver los logs

```bash
# Ver logs de todos los servicios
docker-compose logs

# Ver logs de un servicio específico
docker-compose logs app
docker-compose logs postgres
```

### 4. Detener los servicios

```bash
docker-compose down
```

### 5. Detener y eliminar volúmenes (datos de la BD)

```bash
docker-compose down -v
```

## Servicios

### PostgreSQL
- **Puerto**: 5432
- **Base de datos**: proyecto
- **Usuario**: postgres
- **Contraseña**: manager1
- **Volumen persistente**: Los datos se guardan en `postgres_data`

### Aplicación Spring Boot
- **Puerto**: 8080
- **URL**: http://localhost:8080
- **Perfil**: Usa las variables de entorno definidas en docker-compose.yml

## Configuración de Red

Ambos servicios están conectados a través de una red bridge llamada `proyecto-network`, permitiendo que la aplicación se conecte a PostgreSQL usando el nombre del servicio `postgres` como hostname.

## Variables de Entorno

El archivo `docker-compose.yml` configura las siguientes variables de entorno para la aplicación:

- `SPRING_DATASOURCE_URL`: URL de conexión a PostgreSQL
- `SPRING_DATASOURCE_USERNAME`: Usuario de la base de datos
- `SPRING_DATASOURCE_PASSWORD`: Contraseña de la base de datos
- `SPRING_JPA_HIBERNATE_DDL_AUTO`: Configuración de DDL (create-drop)

## API Endpoints - Gestión de Stock/Cantidades de Productos

Una vez que la aplicación esté ejecutándose, puedes usar los siguientes endpoints para gestionar las cantidades de productos:

### 1. Actualizar Stock (Establecer valor específico)

**Endpoint:** `PUT /api/productos/{id}/stock?nuevoStock={cantidad}`

**Request:**
```bash
curl -X PUT "http://localhost:8080/api/productos/1/stock?nuevoStock=50"
```

**Response (200 OK):**
```json
{
  "id": 1,
  "nombre": "Producto Ejemplo",
  "descripcion": "Descripción del producto",
  "precio": 25.99,
  "stock": 50,
  "requiereReunion": false,
  "activo": true,
  "categoria": {
    "id": 1,
    "nombre": "Categoría Ejemplo"
  }
}
```

**Response (404 Not Found):**
```json
{
  "error": "Producto no encontrado"
}
```

---

### 2. Incrementar Stock (Agregar cantidad)

**Endpoint:** `PUT /api/productos/{id}/stock/incrementar?cantidad={cantidad}`

**Request:**
```bash
curl -X PUT "http://localhost:8080/api/productos/1/stock/incrementar?cantidad=10"
```

**Response (200 OK):**
```json
{
  "id": 1,
  "nombre": "Producto Ejemplo",
  "descripcion": "Descripción del producto",
  "precio": 25.99,
  "stock": 60,
  "requiereReunion": false,
  "activo": true,
  "categoria": {
    "id": 1,
    "nombre": "Categoría Ejemplo"
  }
}
```

**Response (404 Not Found):**
```json
{
  "error": "Producto no encontrado"
}
```

---

### 3. Decrementar Stock (Restar cantidad)

**Endpoint:** `PUT /api/productos/{id}/stock/decrementar?cantidad={cantidad}`

**Request:**
```bash
curl -X PUT "http://localhost:8080/api/productos/1/stock/decrementar?cantidad=5"
```

**Response (200 OK):**
```json
{
  "id": 1,
  "nombre": "Producto Ejemplo",
  "descripción": "Descripción del producto",
  "precio": 25.99,
  "stock": 55,
  "requiereReunion": false,
  "activo": true,
  "categoria": {
    "id": 1,
    "nombre": "Categoría Ejemplo"
  }
}
```

**Response (400 Bad Request - Stock insuficiente):**
```json
{
  "error": "Stock insuficiente. Stock actual: 3"
}
```

**Response (404 Not Found):**
```json
{
  "error": "Producto no encontrado"
}
```

---

### 4. Actualización Masiva de Stock

**Endpoint:** `PUT /api/productos/stock/masivo`

**Request:**
```bash
curl -X PUT "http://localhost:8080/api/productos/stock/masivo" \
  -H "Content-Type: application/json" \
  -d '{
    "operaciones": [
      {
        "productoId": 1,
        "cantidad": 100,
        "operacion": "SET"
      },
      {
        "productoId": 2,
        "cantidad": 20,
        "operacion": "ADD"
      },
      {
        "productoId": 3,
        "cantidad": 5,
        "operacion": "SUBTRACT"
      }
    ]
  }'
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "nombre": "Producto A",
    "descripcion": "Descripción A",
    "precio": 25.99,
    "stock": 100,
    "requiereReunion": false,
    "activo": true,
    "categoria": {
      "id": 1,
      "nombre": "Categoría A"
    }
  },
  {
    "id": 2,
    "nombre": "Producto B",
    "descripcion": "Descripción B",
    "precio": 15.50,
    "stock": 45,
    "requiereReunion": true,
    "activo": true,
    "categoria": {
      "id": 2,
      "nombre": "Categoría B"
    }
  },
  {
    "id": 3,
    "nombre": "Producto C",
    "descripcion": "Descripción C",
    "precio": 8.75,
    "stock": 15,
    "requiereReunion": false,
    "activo": true,
    "categoria": {
      "id": 1,
      "nombre": "Categoría A"
    }
  }
]
```

**Response (400 Bad Request):**
```json
{
  "error": "Stock insuficiente. Stock actual: 2"
}
```

### Operaciones disponibles para actualización masiva:
- **SET**: Establece el stock al valor especificado
- **ADD**: Incrementa el stock en la cantidad especificada
- **SUBTRACT**: Decrementa el stock en la cantidad especificada

---

### 5. Consultar productos con stock bajo

**Endpoint:** `GET /api/productos/stock-bajo?minimo={cantidad}`

**Request:**
```bash
curl "http://localhost:8080/api/productos/stock-bajo?minimo=10"
```

**Response (200 OK):**
```json
[
  {
    "id": 3,
    "nombre": "Producto C",
    "descripcion": "Descripción C",
    "precio": 8.75,
    "stock": 5,
    "requiereReunion": false,
    "activo": true,
    "categoriaNombre": "Categoría A",
    "giroNegocio": "Tecnología"
  },
  {
    "id": 4,
    "nombre": "Producto D",
    "descripcion": "Descripción D",
    "precio": 12.99,
    "stock": 3,
    "requiereReunion": true,
    "activo": true,
    "categoriaNombre": "Categoría B",
    "giroNegocio": "Servicios"
  }
]
```

---

### 6. Consultar productos con stock disponible

**Endpoint:** `GET /api/productos/con-stock`

**Request:**
```bash
curl "http://localhost:8080/api/productos/con-stock"
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "nombre": "Producto A",
    "descripcion": "Descripción A",
    "precio": 25.99,
    "stock": 100,
    "requiereReunion": false,
    "activo": true,
    "categoriaNombre": "Categoría A",
    "giroNegocio": "Tecnología"
  },
  {
    "id": 2,
    "nombre": "Producto B",
    "descripcion": "Descripción B",
    "precio": 15.50,
    "stock": 45,
    "requiereReunion": true,
    "activo": true,
    "categoriaNombre": "Categoría B",
    "giroNegocio": "Servicios"
  }
]
```

## Troubleshooting

### Si la aplicación no puede conectarse a la base de datos:

1. Verifica que PostgreSQL esté ejecutándose:
   ```bash
   docker-compose ps
   ```

2. Verifica los logs de PostgreSQL:
   ```bash
   docker-compose logs postgres
   ```

3. Verifica que la red esté funcionando:
   ```bash
   docker network ls
   ```

### Si necesitas acceder a la base de datos directamente:

```bash
docker-compose exec postgres psql -U postgres -d proyecto
```

### Reconstruir solo la aplicación:

```bash
docker-compose build app
docker-compose up app
```
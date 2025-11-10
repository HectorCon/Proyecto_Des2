-- Insertar roles
INSERT INTO roles (nombre, descripcion) VALUES
('ADMIN', 'Administrador del sistema'),
('VENDEDOR', 'Vendedor de la empresa'),
('CLIENTE', 'Cliente de la empresa');

-- Insertar usuarios administrativos
INSERT INTO usuarios (nombre, email, password, telefono, direccion, activo, rol_id) VALUES
('Admin Sistema', 'admin@empresa.com', 'admin123', '555-0001', 'Oficina Central', true, 1);

-- Insertar clientes
INSERT INTO clientes (nombre, email, telefono, direccion, empresa, tipo_cliente, activo, fecha_registro, notas) VALUES
('María González', 'maria.gonzalez@gmail.com', '555-1001', 'Calle Principal 123', 'Comercial González SAS', 'EMPRESA', true, CURRENT_DATE, 'Cliente frecuente - descuentos especiales'),
('Pedro Martínez', 'pedro.martinez@gmail.com', '555-1002', 'Avenida Central 456', NULL, 'PERSONA', true, CURRENT_DATE, 'Cliente individual'),
('Industrias López', 'compras@industriaslopez.com', '555-1003', 'Zona Industrial Km 15', 'Industrias López Ltda', 'MAYORISTA', true, CURRENT_DATE, 'Cliente mayorista - volúmenes altos'),
('Tech Solutions', 'contacto@techsolutions.com', '555-1004', 'Torre Empresarial Piso 12', 'Tech Solutions S.A.', 'DISTRIBUIDOR', true, CURRENT_DATE, 'Distribuidor tecnológico'),
('Ana Rodríguez', 'ana.rodriguez@personal.com', '555-1005', 'Barrio Los Pinos Casa 45', NULL, 'MINORISTA', true, CURRENT_DATE, 'Cliente minorista activo');

-- Insertar vendedores
INSERT INTO vendedores (nombre, email, telefono, activo, fecha_ingreso, meta_mensual, comision_porcentaje, especialidad, codigo) VALUES
('Juan Carlos Pérez', 'juan.perez@empresa.com', '555-2001', true, CURRENT_DATE, 50000.00, 5.50, 'Tecnología y Electrónicos', 'VEND001'),
('Laura Fernández', 'laura.fernandez@empresa.com', '555-2002', true, CURRENT_DATE, 45000.00, 6.00, 'Servicios de Consultoría', 'VEND002'),
('Carlos Ramírez', 'carlos.ramirez@empresa.com', '555-2003', true, CURRENT_DATE, 60000.00, 5.00, 'Equipos Industriales', 'VEND003');

-- Insertar giros de negocio
INSERT INTO giros_negocio (nombre, descripcion, activo) VALUES
('Productos Electrónicos', 'Venta de dispositivos electrónicos y accesorios', true),
('Servicios de Consultoría', 'Servicios profesionales de consultoría empresarial', true),
('Equipos Industriales', 'Venta y mantenimiento de equipos industriales', true);

-- Insertar categorías de productos
INSERT INTO categorias_productos (nombre, descripcion, giro_negocio_id) VALUES
('Smartphones', 'Teléfonos inteligentes', 1),
('Laptops', 'Computadoras portátiles', 1),
('Accesorios', 'Accesorios para dispositivos', 1),
('Consultoría IT', 'Servicios de tecnología', 2),
('Consultoría Financiera', 'Servicios financieros', 2),
('Maquinaria Pesada', 'Equipos de construcción', 3),
('Herramientas', 'Herramientas industriales', 3);

-- Insertar productos/servicios
INSERT INTO productos (nombre, descripcion, precio, stock, categoria_id, requiere_reunion, activo) VALUES
('iPhone 15', 'Smartphone Apple última generación', 1299.99, 50, 1, false, true),
('Samsung Galaxy S24', 'Smartphone Samsung premium', 1199.99, 30, 1, false, true),
('MacBook Pro M3', 'Laptop Apple con chip M3', 2499.99, 20, 2, false, true),
('Dell XPS 13', 'Laptop ultraportátil', 1799.99, 25, 2, false, true),
('Audífonos Bluetooth', 'Audífonos inalámbricos premium', 299.99, 100, 3, false, true),
('Consultoría Digital', 'Transformación digital empresarial', 5000.00, 999, 4, true, true),
('Auditoría Financiera', 'Servicios de auditoría completa', 3000.00, 999, 5, true, true),
('Excavadora CAT 320', 'Excavadora hidráulica', 250000.00, 5, 6, true, true),
('Taladro Industrial', 'Taladro para uso industrial', 899.99, 15, 7, false, true);

-- Insertar vendedores asignados a productos/servicios
INSERT INTO producto_vendedor (producto_id, vendedor_id) VALUES
(1, 1), (2, 1), (3, 1), (4, 1), (5, 1),
(6, 2), (7, 2), 
(8, 3), (9, 3);

-- Insertar algunos pedidos de ejemplo
INSERT INTO pedidos (numero_pedido, fecha_pedido, estado, total, notas, cliente_id, vendedor_id) VALUES
('PED-2024-001', '2024-11-01', 'PENDIENTE', 2599.98, 'Pedido de smartphones para empresa', 1, 1),
('PED-2024-002', '2024-11-03', 'EN_PROCESO', 5000.00, 'Consultoría digital - proyecto 6 meses', 4, 2),
('PED-2024-003', '2024-11-05', 'ENTREGADO', 899.99, 'Herramientas para taller', 3, 3);

-- Insertar detalles de pedidos
INSERT INTO detalles_pedido (cantidad, precio_unitario, subtotal, pedido_id, producto_id) VALUES
(2, 1299.99, 2599.98, 1, 1),
(1, 5000.00, 5000.00, 2, 6),
(1, 899.99, 899.99, 3, 9);

-- Insertar reuniones
INSERT INTO reuniones (fecha_hora, direccion, estado, notas, cliente_id, vendedor_id, producto_id) VALUES
('2024-11-15 10:00:00', 'Torre Empresarial Piso 12', 'PROGRAMADA', 'Reunión para demostración de consultoría digital', 4, 2, 6),
('2024-11-20 14:30:00', 'Zona Industrial Km 15', 'PROGRAMADA', 'Evaluación de necesidades de maquinaria', 3, 3, 8),
('2024-11-25 09:00:00', 'Oficina Central', 'PROGRAMADA', 'Presentación de nueva línea de productos', 1, 1, 1);
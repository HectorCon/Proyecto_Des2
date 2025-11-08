-- Insertar roles
INSERT INTO roles (nombre, descripcion) VALUES
('ADMIN', 'Administrador del sistema'),
('VENDEDOR', 'Vendedor de la empresa'),
('CLIENTE', 'Cliente de la empresa');

-- Insertar usuarios
INSERT INTO usuarios (nombre, email, password, telefono, direccion, activo, rol_id) VALUES
('Admin Sistema', 'admin@empresa.com', 'admin123', '555-0001', 'Oficina Central', true, 1),
('Juan Vendedor', 'juan.vendedor@empresa.com', 'vendedor123', '555-0002', 'Sucursal Norte', true, 2),
('María Cliente', 'maria.cliente@gmail.com', 'cliente123', '555-0003', 'Calle Principal 123', true, 3),
('Pedro Cliente', 'pedro.cliente@gmail.com', 'cliente123', '555-0004', 'Avenida Central 456', true, 3);

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
(1, 2), (2, 2), (3, 2), (4, 2), (5, 2),
(6, 2), (7, 2), (8, 2), (9, 2);

-- Insertar algunos pedidos de ejemplo
INSERT INTO pedidos (numero_pedido, fecha_pedido, estado, total, notas, cliente_id, vendedor_id) VALUES
('PED-2024-001', '2024-11-01', 'PENDIENTE', 2599.98, 'Pedido de smartphones para empresa', 3, 2),
('PED-2024-002', '2024-11-03', 'EN_PROCESO', 5000.00, 'Consultoría digital - proyecto 6 meses', 4, 2),
('PED-2024-003', '2024-11-05', 'ENTREGADO', 899.99, 'Herramientas para taller', 3, 2);

-- Insertar detalles de pedidos
INSERT INTO detalles_pedido (cantidad, precio_unitario, subtotal, pedido_id, producto_id) VALUES
(2, 1299.99, 2599.98, 1, 1),
(1, 5000.00, 5000.00, 2, 6),
(1, 899.99, 899.99, 3, 9);

-- Insertar reuniones
INSERT INTO reuniones (fecha_hora, direccion, estado, notas, cliente_id, vendedor_id, producto_id) VALUES
('2024-11-15 10:00:00', 'Oficina Cliente - Torre Empresarial', 'PROGRAMADA', 'Reunión para demostración de consultoría digital', 4, 2, 6),
('2024-11-20 14:30:00', 'Planta Industrial - Zona Norte', 'PROGRAMADA', 'Evaluación de necesidades de maquinaria', 3, 2, 8);
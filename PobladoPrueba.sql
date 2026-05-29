-- ====================================================================
-- 1. SERVICIO DE PACIENTES (cyber-patient-service)
-- IDs asignados automáticamente: V_Valerie (1), David_Martinez (2), Maine_Fury (3)
-- ====================================================================
INSERT INTO paciente (alias, nivel_cyberpsicosis, estado) VALUES 
('V_Valerie', 15, 'ESTABLE'),
('David_Martinez', 75, 'ESTABLE'),
('Maine_Fury', 95, 'ESTABLE');

-- ====================================================================
-- 2. SERVICIO DE CATÁLOGO DE CIBERWARE (cyber-ciberware-service)
-- IDs asignados automáticamente: Sandevistan (1), Garras de Mantis (2), Anestesia (3)
-- ====================================================================
INSERT INTO ciberware (nombre, costo_eddies, costo_humanidad, stock) VALUES 
('Sandevistan Mk.IV', 15000.00, 25, 5),
('Garras de Mantis', 9500.00, 15, 3),
('Sistema de Bombeo de Anestesia', 3200.00, 5, 10);

-- ====================================================================
-- 3. SERVICIO DE FINANZAS (cyber-finance-service) - Billeteras
-- IDs asignados automáticamente: Billetera de V (1), David (2), Maine (3)
-- Relación lógica por orden de inserción: paciente_id 1, 2 y 3 respectivamente.
-- ====================================================================
INSERT INTO billetera (paciente_id, saldo_eddies) VALUES 
(1, 50000.00), 
(2, 16000.00), 
(3, 450.00);   

-- ====================================================================
-- 4. SERVICIO DE VENTAS (cyber-sale-service)
-- IDs asignados automáticamente: Venta de V (1), Venta de David (2), Venta fallida de Maine (3)
-- ====================================================================
INSERT INTO venta (paciente_id, ciberware_id, precio_cobrado, estado) VALUES 
(1, 2, 9500.00, 'PAGADA'),      -- V (1) compró Garras de Mantis (2)
(2, 1, 15000.00, 'PAGADA'),     -- David (2) compró el Sandevistan (1)
(3, 1, 15000.00, 'RECHAZADA');  -- Maine (3) intentó comprar Sandevistan (1)

-- ====================================================================
-- 5. SERVICIO DE FINANZAS (cyber-finance-service) - Transacciones
-- IDs asignados automáticamente: Transacción de V (1), Transacción de David (2)
-- ====================================================================
INSERT INTO transaccion (billetera_id, venta_id, monto) VALUES 
(1, 1, 9500.00),  -- Usa Billetera (1) y Venta (1)
(2, 2, 15000.00); -- Usa Billetera (2) y Venta (2)

-- ====================================================================
-- 6. SERVICIO DE QUIRÓFANO / INSTALACIÓN (cyber-surgery-service)
-- IDs asignados automáticamente: Cirugía V (1), Cirugía David (2)
-- ====================================================================
INSERT INTO instalacion (venta_id, paciente_id, ciberware_id, estado_cirugia) VALUES 
(1, 1, 2, 'EXITOSA'),
(2, 2, 1, 'EXITOSA');

-- ====================================================================
-- 7. SERVICIO DE MONITOREO DE HUMANIDAD (cyber-humanity-service)
-- IDs asignados automáticamente: Historial V (1), Historial David (2)
-- ====================================================================
INSERT INTO humanidad (paciente_id, ciberware_id, impacto_humanidad) VALUES 
(1, 2, 15),
(2, 1, 25);

-- ====================================================================
-- 8. SERVICIO DE ALERTAS MAXTAC (cyber-maxtac-service)
-- ID asignado automáticamente: Alerta inicial (1)
-- ====================================================================
INSERT INTO alerta (paciente_id, nivel_amenaza, estado_despliegue) VALUES 
(3, 'CODIGO_NEGRO', 'COMPAÑIA_EN_CAMINO'); -- Alerta preventiva sobre Maine (3)

/*  Advertencia de Seguridad: Asegúrate de ejecutar este script con las tablas completamente 
    vacías (TRUNCATE TABLE) para garantizar que los contadores autoincrementales inicien desde 
    1 y las relaciones lógicas basadas en el orden no se rompan.
*/
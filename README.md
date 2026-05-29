# RIPPERDOC_CLINIC_PROYECTO

¡Bienvenido a Night City! Este proyecto es una simulación arquitectónica de un ecosistema backend para la gestión de una clínica clandestina de **Matasanos (Ripperdocs)**. 

El objetivo principal es demostrar el diseño, aislamiento y comunicación de datos entre **7 microservicios independientes** construidos con **Spring Boot**, utilizando una base de datos única (MySQL) pero respetando el principio de autonomía donde cada servicio administra exclusivamente sus propias tablas.

---

## El Flujo del Paciente (Lógica del Proyecto)

El ecosistema opera bajo una regla de negocio estricta: **transacciones individuales (un ítem por registro)**. El flujo de datos simula el recorrido real de un Edgerunner en la clínica:

1. **Registro:** El paciente entra a la clínica con un nivel de estabilidad mental.
2. **Selección:** Elige un único implante del catálogo disponible.
3. **Contrato:** Se genera una venta en estado *Pendiente*.
4. **Cobro:** El servicio de finanzas valida los fondos (Eddies) y procesa la transacción.
5. **Cirugía:** Si hay pago, el cirujano opera e instala físicamente la pieza en el paciente.
6. **Impacto Psicológico:** El servicio de humanidad calcula el desgaste mental acumulado por el metal implantado.
7. **Código Negro:** Si el paciente supera el límite de cordura, se dispara una alerta automática y MaxTac entra en acción.

---

## Los 7 Microservicios Core

Cada módulo funciona como una entidad aislada que emula restricciones de llaves foráneas (`Foreign Keys`) mediante validaciones lógicas por red a través de peticiones HTTP:

* **1. cyber-patient-service (Pacientes):** Administra la identidad de los Edgerunners, su estado de salud mental y su nivel de degradación en la clínica.
* **2. cyber-ciberware-service (Catálogo):** Gestiona el inventario de piezas de alta tecnología, controlando el stock disponible y sus costos (tanto en dinero como en cordura).
* **3. cyber-sale-service (Ventas):** El núcleo transaccional. Conecta al paciente con el implante mediante registros planos y gestiona el estado de la compraventa.
* **4. cyber-finance-service (Finanzas):** Controla las billeteras digitales de los pacientes y audita cada transacción financiera en eurodólares (Eddies).
* **5. cyber-surgery-service (Quirófano):** Registra el éxito o fallo de la implantación física en la camilla de operaciones tras confirmarse el pago.
* **6. cyber-humanity-service (Monitoreo de Humanidad):** El escáner neuronal que mide el historial de modificaciones del paciente y calcula el impacto psicológico post-cirugía.
* **7. cyber-maxtac-service (Alertas de Seguridad):** El protocolo de emergencia. Monitorea brotes psicóticos para desplegar las fuerzas de choque si un paciente se sale de control.

---

## Stack Tecnológico
* **Java 21** & **Spring Boot**
* **Spring Data JPA** (Persistencia aislada)
* **Spring Web** (Comunicación síncrona/APIs Rest)
* **MySQL** (Base de datos compartida con esquemas aislados)
* **Lombok** & **Jakarta Validation**

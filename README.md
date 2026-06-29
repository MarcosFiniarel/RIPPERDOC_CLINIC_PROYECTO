# RIPPERDOC_CLINIC_PROYECTO

¡Bienvenido a Night City! Este proyecto es una simulación arquitectónica de un ecosistema backend para la gestión de una clínica clandestina de **Matasanos (Ripperdocs)**. 

El objetivo principal es demostrar el diseño, aislamiento y comunicación de datos entre **7 microservicios independientes** construidos con **Spring Boot**, utilizando una base de datos única (MySQL) pero respetando el principio de autonomía donde cada servicio administra exclusivamente sus propias tablas.

---

## El Flujo del Paciente (Lógica del Proyecto)
El ecosistema opera bajo una regla de negocio estricta: transacciones individuales (un ítem por registro). El flujo de datos simula el recorrido real de un Edgerunner en la clínica:

1. Registro: El paciente entra a la clínica con un nivel de estabilidad mental.
2. Selección: Elige un único implante del catálogo disponible.
3. Contrato: Se genera una venta en estado Pendiente.
4. Cobro: El servicio de finanzas valida los fondos (Eddies) y procesa la transacción.
5. Evaluación de Compatibilidad: Antes de la intervención, el sistema cruza los datos biológicos del paciente (edad, peso, altura, densidades) contra las tolerancias de hardware de la pieza elegida usando la regla del "3 de 5".
6. Cirugía: Si el reporte de compatibilidad es favorable, el cirujano opera e instala físicamente la pieza en el paciente, dejando constancia del estado de la intervención (EXITOSA o FALLIDA).
7. Impacto Psicológico: Tras una cirugía exitosa, se actualiza el desgaste mental del paciente acumulado por el metal implantado en su cuerpo.
8. Código Negro: Si el paciente supera el límite de cordura, se dispara una alerta automática y MaxTac entra en acción.

## Los 7 Microservicios Core
Cada módulo funciona como una entidad aislada que emula restricciones de llaves foráneas (Foreign Keys) mediante validaciones lógicas por red a través de peticiones HTTP síncronas (WebClient):

1. cyber-patient-service (Pacientes): Administra la identidad de los Edgerunners, su estado físico-biológico, su salud mental y su nivel de degradación en la clínica.
2. cyber-ciberware-service (Catálogo): Gestiona el inventario de piezas de alta tecnología, controlando el stock disponible, sus costos en Eddies y los requerimientos biológicos estrictos de compatibilidad.
3. cyber-sale-service (Ventas): El núcleo transaccional. Conecta al paciente con el implante mediante registros planos y gestiona el estado de la compraventa.
4. cyber-finance-service (Finanzas): Controla las billeteras digitales de los pacientes y audita cada transacción financiera en eurodólares (Eddies).
5. cyber-compatibility-service (Compatibilidad): El motor algorítmico de la clínica. Ejecuta la validación síncrona "3 de 5" cruzando por red los datos de Pacientes y Catálogo para emitir un diagnóstico de viabilidad médica.
6. cyber-surgery-service (Quirófano): Orquesta la ejecución de la cirugía. Consulta al módulo de Compatibilidad y registra de forma permanente la bitácora médica del éxito o rechazo del implante en la camilla de operaciones.
7. cyber-maxtac-service (Alertas de Seguridad): El protocolo de emergencia. Monitorea brotes psicóticos para desplegar las fuerzas de choque si un paciente se sale de control tras una instalación de cromo.

---
## NOTA IMPORTANTE
Debido al peso de los archivos target de cada microservicio para porder obtener los snapshot y  asi ejecutarlos via docker, github no permite cargarlos por lo cual se deja el enlace a google drive donde estara un archivo comprimido descargable que contendrá los archivos target correspondientes a cada microservicio al igual que el archivo .mvn que omite github por si se desea obtener el snapshot al momento de ejecutar el codigo en lugar de utilizar los ya existentes que corresponden con esta version.

https://drive.google.com/file/d/1N-zyzOFZTazB28qzx-GyxPZBX-M9_f8d/view?usp=sharing

---

## Stack Tecnológico
* **Java 21** & **Spring Boot**
* **Spring Data JPA** (Persistencia aislada)
* **Spring Web** (Comunicación síncrona/APIs Rest)
* **MySQL** (Base de datos compartida con esquemas aislados)
* **Lombok** & **Jakarta Validation**

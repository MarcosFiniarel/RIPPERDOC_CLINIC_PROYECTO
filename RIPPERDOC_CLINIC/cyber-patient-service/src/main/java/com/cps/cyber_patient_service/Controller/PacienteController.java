package com.cps.cyber_patient_service.Controller;

import com.cps.cyber_patient_service.DTO.PacienteCompatibilidadDto;
import com.cps.cyber_patient_service.DTO.PacienteContratoDto;
import com.cps.cyber_patient_service.Model.Paciente;
import com.cps.cyber_patient_service.Service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Habilita el framework para hacer logs
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/paciente")
@RequiredArgsConstructor
@Slf4j // Inyecta automáticamente el objeto 'log' de Lombok para auditoría y trazas en consola
@Tag(name = "2. Registro de Pacientes", description = "Endpoints para el control biológico, seguimiento de cyberpsicosis y contratos de mercenarios")
public class PacienteController {
    private final PacienteService pacienteService;

    /**
     * 1. LISTA DE PACIENTES
     * Metodo HTTP: GET
     * Endpoint: http://localhost:8081/api/v1/paciente
     */
    @GetMapping
    @Operation(summary = "Listar todos los pacientes", description = "Obtiene el padrón completo de sujetos registrados en la base de datos de la clínica clandestina.")
    @ApiResponse(responseCode = "200", description = "Lista de pacientes enviada con éxito")
    public ResponseEntity<List<Paciente>> listarTodos() {
        log.info("=== Solicitud de listado de clientes recibida ===");
        List<Paciente> lista = pacienteService.obtenerTodos();

        // El símbolo {} es un comodín (placeholder) dinámico que reemplaza su valor por el tamaño de la lista
        log.info("Listado enviado con éxito. Total de pacientes: {}", lista.size());
        return ResponseEntity.ok(lista);
    }

    /**
     * 2. BUSCAR UN PACIENTE ESPECÍFICO POR ID
     * Metodo HTTP: GET
     * Endpoint: http://localhost:8081/api/v1/paciente/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar paciente por ID", description = "Localiza la ficha biológica completa de un paciente usando su clave única de red.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente localizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Sujeto no encontrado en los registros de la clínica", content = @Content)
    })
    public ResponseEntity<Paciente> buscarPorId(@PathVariable @Parameter(description = "ID del paciente a consultar", example = "1") Long id) {
        log.info("Buscando paciente con ID: {}", id);
        Paciente paciente = pacienteService.obtenerPorId(id);
        log.info("Paciente encontrado: {}", paciente.getAlias());
        return ResponseEntity.ok(paciente);
    }

    /**
     * 3. REGISTRAR UN NUEVO PACIENTE (CREAR)
     * Metodo HTTP: POST
     * Endpoint: http://localhost:8081/api/v1/paciente
     */
    @PostMapping
    @Operation(summary = "Registrar un nuevo paciente", description = "Crea un nuevo expediente médico en la base de datos evaluando sus métricas corporales iniciales.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "¡Paciente dado de alta en la matriz correctamente!"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los atributos del JSON enviado", content = @Content)
    })
    public ResponseEntity<Paciente> crearPaciente(@Valid @RequestBody Paciente paciente) {
        log.info("Registrando nuevo paciente: {}", paciente.getAlias());
        Paciente guardado = pacienteService.guardar(paciente);
        // ESPACIO EN EL QUE SE VA A MANDAR A CONECTAR CON FINANCE PARA QUE SE CREE AUTOMATICAMENTE LA BILLETERA UNA VEZ QUE ESTE LISTO ESE MICROSERVICIO
        log.info("¡PACIENTE REGISTRADO CON ÉXITO! Asignado ID: {} | Alias: {}", guardado.getId(), guardado.getAlias());

        // Retornamos un estado 201 Created por buenas prácticas de diseño arquitectónico REST
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    /**
     * 4. ELIMINAR UN PACIENTE DEL REGISTRO
     * Metodo HTTP: DELETE
     * Endpoint: http://localhost:8081/api/v1/paciente/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar paciente del registro", description = "Purga de forma permanente el expediente del paciente. Usado ante decesos en combate o alertas de MaxTac.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Expediente médico borrado del sistema sin dejar rastros"),
            @ApiResponse(responseCode = "404", description = "No se pudo eliminar: El ID proporcionado no existe", content = @Content)
    })
    public ResponseEntity<Void> eliminarPaciente(@PathVariable @Parameter(description = "ID del paciente a dar de baja", example = "1") Long id) {
        // Usamos log.warn para alertar en consola que se está ejecutando una acción destructiva
        log.warn("ALERTA: Solicitud de eliminación del paciente con ID: {}", id);
        pacienteService.eliminar(id);
        log.info("Paciente ID: {} eliminado permanentemente", id);

        // Retornamos un 204 No Content porque el recurso ya no existe y no hay cuerpo que devolver
        return ResponseEntity.noContent().build();
    }

    /**
     * 5. ACTUALIZAR NIVEL DE CYBERPSICOSIS
     * Metodo HTTP: PATCH (Usado porque modificamos de forma parcial un solo atributo: el nivel de cyberpsicosis)
     * Endpoint: http://localhost:8081/api/v1/paciente/{id}/psico?modificador=VALUE
     * Nota: Este endpoint está pensado para ser disparado internamente tras una instalacion y posterior registro de alteracion de humanidad.
     */
    @PatchMapping("/{id}/psico")
    @Operation(summary = "Modificar nivel de cyberpsicosis", description = "Actualiza de forma parcial el estado de degradación mental sumando el costo de un nuevo implante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Índice de estabilidad mental actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado", content = @Content)
    })
    public ResponseEntity<Paciente> NvlCyberpsico(
            @PathVariable @Parameter(description = "ID del paciente operado", example = "1") Long id,
            @RequestParam @Parameter(description = "Puntos de humanidad a descontar de la psique del paciente", example = "88") int modificador) {
        log.info("Iniciando actualizacion de humanidad de ID: {}", id);
        Paciente actualizado = pacienteService.actualizacionCyberpsicosis(id, modificador);
        log.info("Nivel de Cyberpsicosis actualizado con éxito. Nivel actual para '{}': {}", actualizado.getAlias(), actualizado.getNivelCyberpsicosis());
        return ResponseEntity.ok(actualizado);
    }

    // ENDPOINT DE INTEGRACIÓN: CONTRATO GLOBAL PARA EL FLUJO DE VENTAS
    /** VALIDAR QUE EL PACIENTE ESTA APTO PARA COMPRAR CIBERWARE
     * Metodo HTTP: GET
     * Endpoint: http://localhost:8081/api/v1/paciente/{id}/check
     * Retorna true si el paciente puede comprar y false si no
     */
    @GetMapping("/{id}/check")
    @Operation(summary = "Validar estado de aptitud del paciente", description = "Verifica si los niveles de cyberpsicosis del paciente le permiten seguir comprando más cromo de forma segura.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chequeo de aptitud realizado con éxito"),
            @ApiResponse(responseCode = "404", description = "ID de paciente no registrado", content = @Content)
    })
    public ResponseEntity<Boolean> checkPaciente(@PathVariable @Parameter(description = "ID del paciente a evaluar", example = "1") Long id) {
        log.info("Verificando paciente con ID: {}", id);
        boolean disponible = pacienteService.checkPaciente(id);
        log.info("Verificación completada. ¿Habilitado para comprar?: {}", disponible);
        return ResponseEntity.ok(disponible);
    }

    /**
     * EMITIR CONTRATO GLOBAL DE COMPRA
     * Metodo HTTP: GET
     * Endpoint: http://localhost:8081/api/v1/paciente/{id}/contrato
     * Retorna un DTO optimizado (ID, ALIAS).
     */
    @GetMapping("/{id}/contrato")
    @Operation(summary = "Emitir Contrato de Paciente (DTO)", description = "Genera un payload optimizado de red que expone de forma directa la identidad del comprador para el flujo de ventas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Firma del contrato del paciente generada con éxito"),
            @ApiResponse(responseCode = "404", description = "Paciente inexistente", content = @Content)
    })
    public ResponseEntity<PacienteContratoDto> obtenerContratoParaVenta(@PathVariable @Parameter(description = "ID del paciente involucrado en la transacción", example = "1") Long id) {
        log.info("Emitiendo Contrato Global para compra de ID: {}", id);
        Paciente paciente = pacienteService.obtenerPorId(id);

        // Se empaquetan los datos de la entidad de la BD hacia el DTO de transferencia de red
        PacienteContratoDto contrato = new PacienteContratoDto(
                paciente.getId(),
                paciente.getAlias()
        );

        log.info("Contrato Global de '{}' generado con éxito. Enviando a Ventas...", contrato.getAliasPaciente());
        return ResponseEntity.ok(contrato);
    }

    /**
     * EMITIR DATOS DE COMPATIBILIDAD
     * Metodo HTTP: GET
     * Endpoint: http://localhost:8081/api/v1/paciente/{id}/compatibilidad
     * Retorna un DTO optimizado (ALTURA, DENSIDAD OSEA, DENSIDAD MUSCULAR, PESO y EDAD).
     */
    @GetMapping("/{id}/compatibilidad")
    @Operation(summary = "Emitir Ficha de Compatibilidad del Paciente (DTO)", description = "Transmite de forma aislada las métricas físicas y óseas del sujeto para corroborar si resistirá un implante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ficha antropométrica enviada correctamente"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado", content = @Content)
    })
    public ResponseEntity<PacienteCompatibilidadDto> obtenerCompatibilidad(@PathVariable @Parameter(description = "ID del paciente a analizar biometricamente", example = "1") Long id) {
        log.info("Emitiendo datos de compatibilidad de ID: {}", id);
        Paciente paciente = pacienteService.obtenerPorId(id);

        // Se empaquetan los datos de la entidad de la BD hacia el DTO de transferencia de red
        PacienteCompatibilidadDto compatibilidad = new PacienteCompatibilidadDto(
                paciente.getAltura(),
                paciente.getDensidadOsea(),
                paciente.getDensidadMuscular(),
                paciente.getPeso(),
                paciente.getEdad()
        );

        log.info("Datos de '{}' obtenidos con éxito. Enviando a Compatibilidad...", paciente.getAlias());
        return ResponseEntity.ok(compatibilidad);
    }
}

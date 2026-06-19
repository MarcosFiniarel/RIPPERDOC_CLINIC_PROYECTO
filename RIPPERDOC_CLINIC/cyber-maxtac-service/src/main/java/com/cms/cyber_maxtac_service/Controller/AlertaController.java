package com.cms.cyber_maxtac_service.Controller;

import com.cms.cyber_maxtac_service.Model.Alerta;
import com.cms.cyber_maxtac_service.Service.AlertaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alertas")
@Tag(name = "8. Centro de Despliegue Táctico - MaxTac", description = "Endpoints de emergencia y monitoreo de amenazas críticas para el despliegue automático de contención militar")
public class AlertaController {

    private final AlertaService alertaService;

    /**
     * CREAR ALERTA (Trigger de emergencia por red)
     * POST http://localhost:8087/api/v1/alertas?pacienteId=X&alias=Y
     */
    @PostMapping
    @Operation(summary = "Gatillar alerta de emergencia (Trigger de red)", description = "Punto de integración asíncrona síncope. Disparado automáticamente por la red médica cuando un paciente supera los límites de cyberpsicosis permitidos durante o después de una cirugía.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "¡ALERTA EMITIDA! La unidad de choque de MaxTac ha despegado hacia la locación del objetivo"),
            @ApiResponse(responseCode = "400", description = "Parámetros de geolocalización o de ID malformados", content = @Content)
    })
    public ResponseEntity<Alerta> crearAlerta(
            @RequestParam @Parameter(description = "ID del paciente catalogado como amenaza", example = "1") Long pacienteId,
            @RequestParam @Parameter(description = "Alias callejero del sujeto a contener", example = "David Martínez") String alias) {
        Alerta nuevaAlerta = alertaService.crearAlerta(pacienteId, alias);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaAlerta);
    }

    /**
     * ACTUALIZAR ESTADO A NEUTRALIZADA
     * PATCH http://localhost:8087/api/v1/alertas/{id}/neutralizar
     */
    @PatchMapping("/{id}/neutralizar")
    @Operation(summary = "Actualizar estado a Neutralizado", description = "Modifica parcialmente el estatus del despliegue táctico una vez que las fuerzas de MaxTac han pacificado o abatido la amenaza en el perímetro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte táctico actualizado con éxito, zona declarada segura"),
            @ApiResponse(responseCode = "404", description = "ID de reporte de alerta no hallado en el centro de comando", content = @Content)
    })
    public ResponseEntity<Alerta> neutralizarAmenaza(@PathVariable @Parameter(description = "ID único de la alerta a actualizar", example = "901") Long id) {
        Alerta alertaActualizada = alertaService.neutralizarAmenaza(id);
        return ResponseEntity.ok(alertaActualizada);
    }

    /**
     * LISTAR TODAS LAS ALERTAS
     * GET http://localhost:8087/api/v1/alertas
     */
    @GetMapping
    @Operation(summary = "Listar bitácora histórica de alertas", description = "Obtiene el registro global de todas las llamadas de emergencia emitidas en el sector de la clínica clandestina.")
    @ApiResponse(responseCode = "200", description = "Bitácora histórica enviada con éxito")
    public ResponseEntity<List<Alerta>> listarTodas() {
        List<Alerta> alertas = alertaService.listarTodas();
        return ResponseEntity.ok(alertas);
    }

    /**
     * BUSCAR ALERTA POR ID DE REPORTE
     * GET http://localhost:8087/api/v1/alertas/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar reporte de alerta por ID", description = "Localiza los archivos de despliegue específicos de una intervención mediante su ID único corporativo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte localizado en los servidores tácticos"),
            @ApiResponse(responseCode = "404", description = "No se halló el registro de intervención solicitado", content = @Content)
    })
    public ResponseEntity<Alerta> obtenerPorId(@PathVariable @Parameter(description = "ID único del reporte de alerta", example = "901") Long id) {
        Alerta alerta = alertaService.obtenerPorId(id);
        return ResponseEntity.ok(alerta);
    }

    /**
     * BUSCAR ALERTAS POR ID DE PACIENTE
     * GET http://localhost:8087/api/v1/alertas/paciente/{pacienteId}
     */
    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Buscar alertas activas por ID de Paciente", description = "Filtra la bitácora de emergencias para verificar si un mercenario específico tiene órdenes de captura o contención vigentes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial táctico del paciente recuperado"),
            @ApiResponse(responseCode = "404", description = "El paciente no registra antecedentes de alertas", content = @Content)
    })
    public ResponseEntity<List<Alerta>> obtenerPorPacienteId(@PathVariable @Parameter(description = "ID del paciente a auditar", example = "1") Long pacienteId) {
        List<Alerta> alertas = alertaService.obtenerPorPacienteId(pacienteId);
        return ResponseEntity.ok(alertas);
    }

    /**
     * BUSCAR ALERTAS POR ALIAS
     * GET http://localhost:8087/api/v1/alertas/alias/{alias}
     */
    @GetMapping("/alias/{alias}")
    @Operation(summary = "Buscar alertas por Alias del sujeto", description = "Consulta las llamadas de emergencia cruzando el nombre callejero o alias táctico del mercenario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Incidentes asociados al alias recuperados con éxito"),
            @ApiResponse(responseCode = "404", description = "El alias no registra antecedentes de alertas", content = @Content)
    })
    public ResponseEntity<List<Alerta>> obtenerPorAlias(@PathVariable @Parameter(description = "Alias del sospechoso a consultar", example = "David Martínez") String alias) {
        List<Alerta> alertas = alertaService.obtenerPorAlias(alias);
        return ResponseEntity.ok(alertas);
    }

    /**
     * ELIMINAR REPORTE DE ALERTA
     * DELETE http://localhost:8087/api/v1/alertas/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reporte de la bitácora", description = "Purga de los servidores el expediente de una llamada de MaxTac. Usado únicamente bajo sobornos masivos de edis o para borrar huellas frente a la policía de Night City (NCPD).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evidencia táctica eliminada del sistema correctamente"),
            @ApiResponse(responseCode = "404", description = "No se localizó el reporte de alerta para purgar", content = @Content)
    })
    public ResponseEntity<Void> eliminarAlerta(@PathVariable @Parameter(description = "ID de la alerta a purgar", example = "901") Long id) {
        alertaService.eliminarAlerta(id);
        return ResponseEntity.noContent().build();
    }
}

package com.ccos.cyber_compatibility_service.Controller;

import com.ccos.cyber_compatibility_service.Model.Compatibilidad;
import com.ccos.cyber_compatibility_service.Service.CompatibilidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/compatibilidad")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "7. Central de Compatibilidad Biomédica", description = "Endpoints de integración y diagnóstico rápido para validar la tolerancia biológica del paciente antes de la fijación del cromo")
public class CompatibilidadController {

    private final CompatibilidadService compatibilidadService;

    /**
     * 1. LISTAR TODAS LAS COMPATIBILIDADES
     * Endpoint: GET http://localhost:8088/api/v1/compatibilidad
     */
    @GetMapping
    @Operation(summary = "Listar todo el histórico de compatibilidades", description = "Obtiene el registro completo de escaneos y veredictos biológicos ejecutados en la clínica.")
    @ApiResponse(responseCode = "200", description = "Historial biomédico extraído correctamente de la red")
    public ResponseEntity<List<Compatibilidad>> listarTodas() {
        log.info("=== Solicitud de historial completo de compatibilidad recibida ===");
        return ResponseEntity.ok(compatibilidadService.obtenerTodas());
    }

    /**
     * 2. FILTRAR POR ESTADO (Exitosas o Fallidas)
     * Endpoint: GET http://localhost:8088/api/v1/compatibilidad/filtrar?estado=true
     */
    @GetMapping("/filtrar")
    @Operation(summary = "Filtrar escaneos por estado", description = "Permite segmentar las evaluaciones médicas para analizar la tasa de rechazos biológicos (false) o aprobaciones exitosas (true).")
    @ApiResponse(responseCode = "200", description = "Registros filtrados enviados con éxito")
    public ResponseEntity<List<Compatibilidad>> listarPorEstado(@RequestParam @Parameter(description = "Filtro de aptitud (true = Aptos / false = Rechazados)", example = "true") boolean estado) {
        log.info("Filtrando registros de compatibilidad donde estado = {}", estado);
        return ResponseEntity.ok(compatibilidadService.obtenerPorEstado(estado));
    }

    /**
     * 3. ENDPOINT DE EVALUACIÓN ULTRA-SIMPLIFICADA
     * Disparado por red desde Cirugías pasando solo parámetros de consulta.
     * Cero DTOs de entrada, procesamiento inmediato en memoria y retorno del booleano.
     * * Endpoint: POST http://localhost:8088/api/v1/compatibilidad/evaluar?idCirugia=VALUE&idPaciente=VALUE&idCiberware=VALUE
     */
    @PostMapping("/evaluar")
    @Operation(summary = "Ejecutar escaneo biológico cruzado (Ultra-Simplificado)", description = "Endpoint crítico de integración síncrona. Recibe los IDs planos, consulta asincrónicamente el ciberware y el paciente, compara métricas musculares, óseas y de edad en memoria, y devuelve la respuesta instantánea.")
    @ApiResponse(responseCode = "200", description = "Procesamiento y escaneo biológico finalizado (Retorna true si pasa los requisitos corporales y false si hay rechazo clínico)")
    public ResponseEntity<Boolean> evaluarCompatibilidadCompleta(
            @RequestParam @Parameter(description = "ID de la cirugía asociada", example = "501") Long idCirugia,
            @RequestParam @Parameter(description = "ID del paciente en la camilla", example = "1") Long idPaciente,
            @RequestParam @Parameter(description = "ID del ciberware a atornillar", example = "1") Long idCiberware) {

        log.info("Petición de escaneo recibida. Cirugía: {} | Paciente: {} | Ciberware: {}",
                idCirugia, idPaciente, idCiberware);

        // Ejecutamos la lógica pasando los parámetros planos directo al service
        boolean esApto = compatibilidadService.evaluarYRegistrar(idCirugia, idPaciente, idCiberware);

        log.info("Evaluación completada para Cirugía ID: {}. ¿Habilitado?: {}", idCirugia, esApto);
        return ResponseEntity.ok(esApto);
    }

    /**
     * 4. ELIMINAR UN REGISTRO HISTÓRICO
     * Endpoint: DELETE http://localhost:8088/api/v1/compatibilidad/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar registro analítico", description = "Borra permanentemente el resultado de un escaneo biológico del registro histórico clínico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Registro purgado del sistema de diagnóstico sin dejar rastro"),
            @ApiResponse(responseCode = "404", description = "ID de diagnóstico no localizado", content = @Content)
    })
    public ResponseEntity<Void> eliminarRegistro(@PathVariable @Parameter(description = "ID del registro de compatibilidad a purgar", example = "701") Long id) {
        log.warn("Solicitud de eliminación del registro de compatibilidad ID: {}", id);
        compatibilidadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

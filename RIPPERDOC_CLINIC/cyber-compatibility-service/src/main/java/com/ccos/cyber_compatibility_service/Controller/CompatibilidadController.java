package com.ccos.cyber_compatibility_service.Controller;

import com.ccos.cyber_compatibility_service.Model.Compatibilidad;
import com.ccos.cyber_compatibility_service.Service.CompatibilidadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/compatibilidad")
@RequiredArgsConstructor
@Slf4j
public class CompatibilidadController {

    private final CompatibilidadService compatibilidadService;

    /**
     * 1. LISTAR TODAS LAS COMPATIBILIDADES
     * Endpoint: GET http://localhost:8088/api/v1/compatibilidad
     */
    @GetMapping
    public ResponseEntity<List<Compatibilidad>> listarTodas() {
        log.info("=== Solicitud de historial completo de compatibilidad recibida ===");
        return ResponseEntity.ok(compatibilidadService.obtenerTodas());
    }

    /**
     * 2. FILTRAR POR ESTADO (Exitosas o Fallidas)
     * Endpoint: GET http://localhost:8088/api/v1/compatibilidad/filtrar?estado=true
     */
    @GetMapping("/filtrar")
    public ResponseEntity<List<Compatibilidad>> listarPorEstado(@RequestParam boolean estado) {
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
    public ResponseEntity<Boolean> evaluarCompatibilidadCompleta(
            @RequestParam Long idCirugia,
            @RequestParam Long idPaciente,
            @RequestParam Long idCiberware) {

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
    public ResponseEntity<Void> eliminarRegistro(@PathVariable Long id) {
        log.warn("Solicitud de eliminación del registro de compatibilidad ID: {}", id);
        compatibilidadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

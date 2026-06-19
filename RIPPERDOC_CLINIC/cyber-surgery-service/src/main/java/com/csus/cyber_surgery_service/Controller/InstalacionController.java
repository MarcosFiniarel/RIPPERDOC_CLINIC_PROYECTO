package com.csus.cyber_surgery_service.Controller;

import com.csus.cyber_surgery_service.DTO.CirugiaRequestDto;
import com.csus.cyber_surgery_service.Model.Instalacion;
import com.csus.cyber_surgery_service.Service.InstalacionService;
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
@RequestMapping("/api/v1/cirugias")
@RequiredArgsConstructor
@Tag(name = "6. Quirófano y Cirugías Críticas", description = "Endpoints automáticos y manuales para la programación, consulta y ejecución de instalaciones de cromo biológico")
public class InstalacionController {

    private final InstalacionService instalacionService;

    /**
     * 1. CREAR UNA CIRUGÍA (Gatillada automáticamente por el microservicio de Ventas)
     * Endpoint: POST http://localhost:8085/api/v1/cirugias
     */
    @PostMapping
    @Operation(summary = "Registrar y programar cirugía", description = "Endpoint de integración automática gatillado por el microservicio de Ventas tras un pago exitoso para iniciar la operación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "¡Cirugía agendada y registrada en el sistema del quirófano!"),
            @ApiResponse(responseCode = "400", description = "Payload de integración malformado o inválido", content = @Content)
    })
    public ResponseEntity<Instalacion> registrarCirugia(@RequestBody @Parameter(description = "DTO de integración con los datos de la orden de venta") CirugiaRequestDto request) {
        Instalacion operacionRealizada = instalacionService.crearCirugia(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(operacionRealizada);
    }

    /**
     * 2. LISTAR TODAS LAS CIRUGÍAS
     * Endpoint: GET http://localhost:8085/api/v1/cirugias
     */
    @GetMapping
    @Operation(summary = "Listar todas las cirugías", description = "Recupera el historial completo de procedimientos quirúrgicos realizados en la clínica.")
    @ApiResponse(responseCode = "200", description = "Historial quirúrgico obtenido con éxito")
    public ResponseEntity<List<Instalacion>> listarTodas() {
        return ResponseEntity.ok(instalacionService.obtenerTodas());
    }

    /**
     * 3. LISTAR CIRUGÍAS POR PACIENTE
     * Endpoint: GET http://localhost:8085/api/v1/cirugias/paciente/{pacienteId}
     */
    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Filtrar cirugías por Paciente", description = "Obtiene la lista de todas las intervenciones médicas que ha sufrido un mercenario específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial del paciente recuperado correctamente"),
            @ApiResponse(responseCode = "404", description = "ID de paciente no registrado", content = @Content)
    })
    public ResponseEntity<List<Instalacion>> listarPorPaciente(@PathVariable @Parameter(description = "ID del paciente a consultar", example = "1") Long pacienteId) {
        return ResponseEntity.ok(instalacionService.obtenerPorPaciente(pacienteId));
    }

    /**
     * 4. LISTAR CIRUGÍAS POR CIBERWARE
     * Endpoint: GET http://localhost:8085/api/v1/cirugias/ciberware/{ciberwareId}
     */
    @GetMapping("/ciberware/{ciberwareId}")
    @Operation(summary = "Filtrar cirugías por Ciberware", description = "Muestra cuántas veces se ha instalado con éxito un modelo específico de implante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Métricas de instalación del ciberware obtenidas"),
            @ApiResponse(responseCode = "404", description = "ID de ciberware no existente", content = @Content)
    })
    public ResponseEntity<List<Instalacion>> listarPorCiberware(@PathVariable @Parameter(description = "ID del implante en catálogo", example = "1") Long ciberwareId) {
        return ResponseEntity.ok(instalacionService.obtenerPorCiberware(ciberwareId));
    }

    /**
     * 5. BUSCAR POR ID DE CIRUGÍA
     * Endpoint: GET http://localhost:8085/api/v1/cirugias/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar reporte quirúrgico por ID", description = "Localiza una ficha de cirugía específica usando su identificador único de reporte.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte de cirugía localizado"),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún reporte con el ID proporcionado", content = @Content)
    })
    public ResponseEntity<Instalacion> buscarPorId(@PathVariable @Parameter(description = "ID único del reporte de cirugía", example = "501") Long id) {
        return ResponseEntity.ok(instalacionService.obtenerPorId(id));
    }

    /**
     * 7. ELIMINAR CIRUGÍA
     * Endpoint: DELETE http://localhost:8085/api/v1/cirugias/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar registro de cirugía", description = "Purga de los servidores el historial de una intervención quirúrgica específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reporte médico borrado correctamente del sistema"),
            @ApiResponse(responseCode = "404", description = "No se localizó el reporte para su eliminación", content = @Content)
    })
    public ResponseEntity<Void> eliminarCirugia(@PathVariable @Parameter(description = "ID del reporte a eliminar", example = "501") Long id) {
        instalacionService.eliminarCirugia(id);
        return ResponseEntity.noContent().build();
    }
}

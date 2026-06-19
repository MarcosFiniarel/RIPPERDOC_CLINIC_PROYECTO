package com.css.cyber_sale_service.Controller;

import com.css.cyber_sale_service.DTO.RequestDto;
import com.css.cyber_sale_service.Model.Venta;
import com.css.cyber_sale_service.Service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
@RequiredArgsConstructor
@Tag(name = "5. Gestión de Ventas y Facturación", description = "Endpoints de orquestación central para procesar compras, verificar fondos y disparar órdenes de cirugías")
public class VentaController {

    private final VentaService ventaService;

    /**
     * 1. LISTAR TODAS LAS VENTAS
     * Endpoint: GET http://localhost:8083/api/v1/ventas
     */
    @GetMapping
    @Operation(summary = "Listar todas las órdenes de venta", description = "Recupera el historial de transacciones, presupuestos y compras gestionadas por la clínica.")
    @ApiResponse(responseCode = "200", description = "Historial de ventas obtenido de la red")
    public ResponseEntity<List<Venta>> listarTodas() {
        List<Venta> ventas = ventaService.obtenerTodas();
        return ResponseEntity.ok(ventas);
    }

    /**
     * 2. LISTAR VENTAS POR CLIENTE (PACIENTE)
     * Endpoint: GET http://localhost:8083/api/v1/ventas/paciente/{pacienteId}
     */
    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Filtrar ventas por Paciente", description = "Muestra la lista de compras y presupuestos asignados a la cuenta de un mercenario específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ventas del paciente recuperadas"),
            @ApiResponse(responseCode = "404", description = "ID de paciente no registrado", content = @Content)
    })
    public ResponseEntity<List<Venta>> listarPorPaciente(@PathVariable @Parameter(description = "ID del paciente a consultar", example = "1") Long pacienteId) {
        List<Venta> ventas = ventaService.obtenerPorPaciente(pacienteId);
        return ResponseEntity.ok(ventas);
    }

    /**
     * 3. LISTAR VENTAS POR CIBERWARE
     * Endpoint: GET http://localhost:8083/api/v1/ventas/ciberware/{ciberwareId}
     */
    @GetMapping("/ciberware/{ciberwareId}")
    @Operation(summary = "Filtrar ventas por Ciberware", description = "Muestra el volumen de compras de un modelo de implante específico del catálogo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Métricas de venta del ciberware obtenidas"),
            @ApiResponse(responseCode = "404", description = "ID de ciberware inexistente", content = @Content)
    })
    public ResponseEntity<List<Venta>> listarPorCiberware(@PathVariable @Parameter(description = "ID del implante en catálogo", example = "1") Long ciberwareId) {
        List<Venta> ventas = ventaService.obtenerPorCiberware(ciberwareId);
        return ResponseEntity.ok(ventas);
    }

    /**
     * 4. BUSCAR VENTA POR ID
     * Endpoint: GET http://localhost:8083/api/v1/ventas/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar orden de venta por ID", description = "Obtiene los detalles de facturación de una compra específica usando su código de auditoría.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden de venta localizada"),
            @ApiResponse(responseCode = "404", description = "Código de orden de venta no hallado", content = @Content)
    })
    public ResponseEntity<Venta> buscarPorId(@PathVariable @Parameter(description = "ID único de la orden", example = "105") Long id) {
        Venta venta = ventaService.obtenerPorId(id);
        return ResponseEntity.ok(venta);
    }

    /**
     * 5. CREAR LA VENTA (PROCESAR COMPRA INMEDIATA)
     * Endpoint: POST http://localhost:8083/api/v1/ventas
     * Nota: Recibe la solicitud minimalista de Postman y gatilla la orquestación distribuida.
     */
    @PostMapping
    @Operation(summary = "Procesar compra inmediata (Orquestación)", description = "Punto de entrada principal. Recibe la solicitud del cliente, evalúa compatibilidad biológica cruzada, stock físico en vitrina, fondos de Eddies y, si pasa los filtros, genera el cargo y emite la orden de cirugía.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "¡Orden de compra procesada con éxito y enviada al quirófano!"),
            @ApiResponse(responseCode = "400", description = "Operación denegada (Error de validación, stock agotado, incompatibilidad o fondos insuficientes)", content = @Content)
    })
    public ResponseEntity<Venta> registrarVenta(@Valid @RequestBody RequestDto request) {
        Venta ordenProcesada = ventaService.crearVenta(request);
        // Retornamos 201 Created por buenas prácticas de APIs RESTful
        return ResponseEntity.status(HttpStatus.CREATED).body(ordenProcesada);
    }

    /**
     * 7. ELIMINAR UNA VENTA DEL HISTÓRICO
     * Endpoint: DELETE http://localhost:8083/api/v1/ventas/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar venta del histórico", description = "Purga permanentemente el registro de una orden de venta. Usado únicamente ante auditorías severas o anulaciones completas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Registro de orden purgado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se localizó la orden para su eliminación", content = @Content)
    })
    public ResponseEntity<Void> eliminarVenta(@PathVariable @Parameter(description = "ID de la orden a purgar", example = "105") Long id) {
        ventaService.eliminarVenta(id);
        // Retornamos 204 No Content porque el recurso se purgó con éxito y no hay cuerpo de respuesta
        return ResponseEntity.noContent().build();
    }
}

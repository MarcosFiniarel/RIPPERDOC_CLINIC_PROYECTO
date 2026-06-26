package com.cfs.cyber_finance_service.Controller;

import com.cfs.cyber_finance_service.Model.Transaccion;
import com.cfs.cyber_finance_service.Services.TransaccionService;
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
@RequestMapping("/api/v1/transacciones")
@Tag(name = "4. Finanzas - Auditoría de Transacciones", description = "Endpoints de control y trazabilidad de pagos automáticos del flujo de ventas")
public class TransaccionController {
    private final TransaccionService transaccionService;

    // CREAR TRANSACCION
    @PostMapping
    @Operation(summary = "Procesar pago automático de venta", description = "Endpoint de integración síncrona que evalúa el saldo, debita el dinero y genera el comprobante de auditoría de la venta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Procesamiento de pago finalizado (Retorna true si fue aprobada, false si fue rechazada)")
    })
    public ResponseEntity<Boolean> crear(
            @RequestParam @Parameter(description = "ID de la orden de venta", example = "105") Long idVenta,
            @RequestParam @Parameter(description = "ID del paciente comprador", example = "1") Long idPaciente,
            @RequestParam @Parameter(description = "Costo de la operación en Eddies", example = "120000.0") Double monto){

        boolean resultado = transaccionService.crear(idVenta, idPaciente, monto);
        // Aunque devuelva un booleano, al ser un POST que da de alta un registro/auditoría de transacción,
        // se usa HttpStatus.CREATED (201) por buenas prácticas RESTful.
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    // OBTENER TODAS LAS TRANSACCIONES
    @GetMapping
    @Operation(summary = "Listar historial global de transacciones", description = "Permite a los analistas de la clínica ver todos los pagos autorizados y rechazados de la red.")
    @ApiResponse(responseCode = "200", description = "Historial global enviado con éxito")
    public ResponseEntity<List<Transaccion>> obtenerTodas(){
        List<Transaccion> transacciones = transaccionService.obtenerTodas();
        return ResponseEntity.ok(transacciones);
    }

    // OBTENER TRANSACCIONES POR BILLETERA
    @GetMapping("/billetera/{billeteraId}")
    @Operation(summary = "Listar transacciones por Billetera", description = "Filtra el historial de movimientos de una sola cuenta de banco específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial de la cuenta extraído correctamente"),
            @ApiResponse(responseCode = "404", description = "Billetera no encontrada", content = @Content)
    })
    public ResponseEntity<List<Transaccion>> obtenerPorBilletera(
            @PathVariable @Parameter(description = "ID de la billetera a auditar", example = "1") Long billeteraId){
        List<Transaccion> historial = transaccionService.obtenerPorBilletera(billeteraId);
        return ResponseEntity.ok(historial);
    }
}

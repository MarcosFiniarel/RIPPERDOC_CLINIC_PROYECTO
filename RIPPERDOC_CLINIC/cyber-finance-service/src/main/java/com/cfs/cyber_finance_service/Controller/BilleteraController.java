package com.cfs.cyber_finance_service.Controller;

import com.cfs.cyber_finance_service.Model.Billetera;
import com.cfs.cyber_finance_service.Services.BilleteraService;
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
@RequestMapping("/api/v1/billeteras")
@RequiredArgsConstructor
@Tag(name = "3. Finanzas - Billeteras", description = "Endpoints para la gestión de fondos, saldos y cuentas de Eurodólares de los mercenarios")
public class BilleteraController {

    private final BilleteraService billeteraService;

    // CREAR BILLETERA
    @PostMapping
    @Operation(summary = "Inicializar billetera de paciente", description = "Crea automáticamente una cuenta financiera en 0.0 Eddies vinculada a un paciente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Billetera creada con éxito"),
            @ApiResponse(responseCode = "400", description = "El paciente ya cuenta con una billetera activa", content = @Content)
    })
    public ResponseEntity<Billetera> crear(@RequestParam @Parameter(description = "ID del paciente a asociar", example = "1") Long id) {
        Billetera nuevaBilletera = billeteraService.crear(id);
        // Cambiado a HttpStatus.CREATED (201) por buenas prácticas de diseño en creación de recursos
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaBilletera);
    }

    // OBTENER TODAS LAS BILLETERAS
    @GetMapping
    @Operation(summary = "Obtener todas las billeteras", description = "Muestra el registro completo de cuentas de la clínica.")
    @ApiResponse(responseCode = "200", description = "Listado de cuentas enviado")
    public ResponseEntity<List<Billetera>> obtenerTodas() {
        List<Billetera> lista = billeteraService.obtenerTodas();
        return ResponseEntity.ok(lista);
    }

    // OBTENER BILLETERA POR ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar billetera por ID", description = "Recupera una cuenta usando el ID único de la billetera.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Billetera localizada"),
            @ApiResponse(responseCode = "404", description = "Billetera no registrada", content = @Content)
    })
    public ResponseEntity<Billetera> obtenerPorId(@PathVariable("id") @Parameter(description = "ID de la billetera", example = "1") Long id) {
        Billetera billetera = billeteraService.obtenerPorId(id);
        return ResponseEntity.ok(billetera);
    }

    // OBTENER BILLETERA POR ID DE PACIENTE
    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Buscar billetera por ID de Paciente", description = "Obtiene los fondos del mercenario realizando la búsqueda cruzada con su ID de paciente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Billetera del paciente localizada"),
            @ApiResponse(responseCode = "404", description = "El paciente no posee una cuenta registrada", content = @Content)
    })
    public ResponseEntity<Billetera> obtenerPorPacienteId(@PathVariable("pacienteId") @Parameter(description = "ID del paciente", example = "1") Long pacienteId) {
        Billetera billetera = billeteraService.obtenerPorPacienteId(pacienteId);
        return ResponseEntity.ok(billetera);
    }

    // SUMAR SALDO A BILLETERA (EN EDDIES)
    @PutMapping("/{id}/sumar")
    @Operation(summary = "Abonar fondos (Cargar Eddies)", description = "Suma una cantidad de dinero a la billetera especificada por ID (Depósito manual).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Abono procesado, saldo actualizado"),
            @ApiResponse(responseCode = "404", description = "Billetera inexistente", content = @Content)
    })
    public ResponseEntity<Billetera> sumarSaldo(
            @PathVariable @Parameter(description = "ID de la billetera a abonar", example = "1") Long id,
            @RequestParam @Parameter(description = "Monto en Eddies a depositar", example = "50000.0") Double monto) {
        Billetera billeteraActualizada = billeteraService.sumarSaldo(id, monto);
        return ResponseEntity.ok(billeteraActualizada);
    }

    // RESTAR SALDO A BILLETERA (EN EDDIES)
    @PutMapping("/{id}/restar")
    @Operation(summary = "Cobrar fondos (Deducir Eddies)", description = "Resta dinero de la cuenta tras una compra. Retorna true si la operación es exitosa y false si no hay fondos suficientes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evaluación de transacción completada"),
            @ApiResponse(responseCode = "404", description = "Billetera no encontrada", content = @Content)
    })
    public ResponseEntity<Boolean> restarSaldo(
            @PathVariable @Parameter(description = "ID de la billetera a cobrar", example = "1") Long id,
            @RequestParam @Parameter(description = "Monto en Eddies a debitar", example = "120000.0") Double monto) {
        Boolean resultadoTransaccion = billeteraService.restarSaldo(id, monto);
        return ResponseEntity.ok(resultadoTransaccion);
    }

    // ELIMINAR BILLETERA
    @DeleteMapping("/{id}")
    @Operation(summary = "Cerrar cuenta financiera", description = "Elimina permanentemente el registro de la billetera del sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Billetera eliminada correctamente del sistema sin dejar rastro"),
            @ApiResponse(responseCode = "404", description = "Cuenta no existente", content = @Content)
    })
    public ResponseEntity<Void> eliminar(@PathVariable @Parameter(description = "ID de la billetera a dar de baja", example = "1") Long id) {
        billeteraService.eliminar(id);
        // Modificado a 204 No Content para mantener la estricta consistencia con el diseño de tus otros microservicios
        return ResponseEntity.noContent().build();
    }
}
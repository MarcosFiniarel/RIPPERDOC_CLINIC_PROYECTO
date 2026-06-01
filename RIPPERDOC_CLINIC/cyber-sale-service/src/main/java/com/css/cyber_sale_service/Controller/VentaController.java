package com.css.cyber_sale_service.Controller;

import com.css.cyber_sale_service.DTO.RequestDto;
import com.css.cyber_sale_service.Model.Venta;
import com.css.cyber_sale_service.Service.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    /**
     * 1. LISTAR TODAS LAS VENTAS
     * Endpoint: GET http://localhost:8083/api/v1/ventas
     */
    @GetMapping
    public ResponseEntity<List<Venta>> listarTodas() {
        List<Venta> ventas = ventaService.obtenerTodas();
        return ResponseEntity.ok(ventas);
    }

    /**
     * 2. LISTAR VENTAS POR CLIENTE (PACIENTE)
     * Endpoint: GET http://localhost:8083/api/v1/ventas/paciente/{pacienteId}
     */
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Venta>> listarPorPaciente(@PathVariable Long pacienteId) {
        List<Venta> ventas = ventaService.obtenerPorPaciente(pacienteId);
        return ResponseEntity.ok(ventas);
    }

    /**
     * 3. LISTAR VENTAS POR CIBERWARE
     * Endpoint: GET http://localhost:8083/api/v1/ventas/ciberware/{ciberwareId}
     */
    @GetMapping("/ciberware/{ciberwareId}")
    public ResponseEntity<List<Venta>> listarPorCiberware(@PathVariable Long ciberwareId) {
        List<Venta> ventas = ventaService.obtenerPorCiberware(ciberwareId);
        return ResponseEntity.ok(ventas);
    }

    /**
     * 4. BUSCAR VENTA POR ID
     * Endpoint: GET http://localhost:8083/api/v1/ventas/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Venta> buscarPorId(@PathVariable Long id) {
        Venta venta = ventaService.obtenerPorId(id);
        return ResponseEntity.ok(venta);
    }

    /**
     * 5. CREAR LA VENTA (PROCESAR COMPRA INMEDIATA)
     * Endpoint: POST http://localhost:8083/api/v1/ventas
     * Nota: Recibe la solicitud minimalista de Postman y gatilla la orquestación distribuida.
     */
    @PostMapping
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
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long id) {
        ventaService.eliminarVenta(id);
        // Retornamos 204 No Content porque el recurso se purgó con éxito y no hay cuerpo de respuesta
        return ResponseEntity.noContent().build();
    }
}

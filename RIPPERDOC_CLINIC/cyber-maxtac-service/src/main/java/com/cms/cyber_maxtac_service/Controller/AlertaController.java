package com.cms.cyber_maxtac_service.Controller;

import com.cms.cyber_maxtac_service.Model.Alerta;
import com.cms.cyber_maxtac_service.Service.AlertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alertas")
public class AlertaController {

    private final AlertaService alertaService;

    /**
     * CREAR ALERTA (Trigger de emergencia por red)
     * POST http://localhost:8087/api/v1/alertas?pacienteId=X&alias=Y
     */
    @PostMapping
    public ResponseEntity<Alerta> crearAlerta(@RequestParam Long pacienteId, @RequestParam String alias) {
        Alerta nuevaAlerta = alertaService.crearAlerta(pacienteId, alias);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaAlerta);
    }

    /**
     * ACTUALIZAR ESTADO A NEUTRALIZADA
     * PATCH http://localhost:8087/api/v1/alertas/{id}/neutralizar
     */
    @PatchMapping("/{id}/neutralizar")
    public ResponseEntity<Alerta> neutralizarAmenaza(@PathVariable Long id) {
        Alerta alertaActualizada = alertaService.neutralizarAmenaza(id);
        return ResponseEntity.ok(alertaActualizada);
    }

    /**
     * LISTAR TODAS LAS ALERTAS
     * GET http://localhost:8087/api/v1/alertas
     */
    @GetMapping
    public ResponseEntity<List<Alerta>> listarTodas() {
        List<Alerta> alertas = alertaService.listarTodas();
        return ResponseEntity.ok(alertas);
    }

    /**
     * BUSCAR ALERTA POR ID DE REPORTE
     * GET http://localhost:8087/api/v1/alertas/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Alerta> obtenerPorId(@PathVariable Long id) {
        Alerta alerta = alertaService.obtenerPorId(id);
        return ResponseEntity.ok(alerta);
    }

    /**
     * BUSCAR ALERTAS POR ID DE PACIENTE
     * GET http://localhost:8087/api/v1/alertas/paciente/{pacienteId}
     */
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Alerta>> obtenerPorPacienteId(@PathVariable Long pacienteId) {
        List<Alerta> alertas = alertaService.obtenerPorPacienteId(pacienteId);
        return ResponseEntity.ok(alertas);
    }

    /**
     * BUSCAR ALERTAS POR ALIAS
     * GET http://localhost:8087/api/v1/alertas/alias/{alias}
     */
    @GetMapping("/alias/{alias}")
    public ResponseEntity<List<Alerta>> obtenerPorAlias(@PathVariable String alias) {
        List<Alerta> alertas = alertaService.obtenerPorAlias(alias);
        return ResponseEntity.ok(alertas);
    }

    /**
     * ELIMINAR REPORTE DE ALERTA
     * DELETE http://localhost:8087/api/v1/alertas/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlerta(@PathVariable Long id) {
        alertaService.eliminarAlerta(id);
        return ResponseEntity.noContent().build();
    }
}

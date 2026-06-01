package com.csus.cyber_surgery_service.Controller;

import com.csus.cyber_surgery_service.DTO.CirugiaRequestDto;
import com.csus.cyber_surgery_service.Model.Instalacion;
import com.csus.cyber_surgery_service.Service.InstalacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cirugias")
@RequiredArgsConstructor
public class InstalacionController {

    private final InstalacionService instalacionService;

    /**
     * 1. CREAR UNA CIRUGÍA (Gatillada automáticamente por el microservicio de Ventas)
     * Endpoint: POST http://localhost:8085/api/v1/cirugias
     */
    @PostMapping
    public ResponseEntity<Instalacion> registrarCirugia(@RequestBody CirugiaRequestDto request) {
        Instalacion operacionRealizada = instalacionService.crearCirugia(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(operacionRealizada);
    }

    /**
     * 2. LISTAR TODAS LAS CIRUGÍAS
     * Endpoint: GET http://localhost:8085/api/v1/cirugias
     */
    @GetMapping
    public ResponseEntity<List<Instalacion>> listarTodas() {
        return ResponseEntity.ok(instalacionService.obtenerTodas());
    }

    /**
     * 3. LISTAR CIRUGÍAS POR PACIENTE
     * Endpoint: GET http://localhost:8085/api/v1/cirugias/paciente/{pacienteId}
     */
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Instalacion>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(instalacionService.obtenerPorPaciente(pacienteId));
    }

    /**
     * 4. LISTAR CIRUGÍAS POR CIBERWARE
     * Endpoint: GET http://localhost:8085/api/v1/cirugias/ciberware/{ciberwareId}
     */
    @GetMapping("/ciberware/{ciberwareId}")
    public ResponseEntity<List<Instalacion>> listarPorCiberware(@PathVariable Long ciberwareId) {
        return ResponseEntity.ok(instalacionService.obtenerPorCiberware(ciberwareId));
    }

    /**
     * 5. BUSCAR POR ID DE CIRUGÍA
     * Endpoint: GET http://localhost:8085/api/v1/cirugias/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Instalacion> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(instalacionService.obtenerPorId(id));
    }

    /**
     * 7. ELIMINAR CIRUGÍA
     * Endpoint: DELETE http://localhost:8085/api/v1/cirugias/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCirugia(@PathVariable Long id) {
        instalacionService.eliminarCirugia(id);
        return ResponseEntity.noContent().build();
    }
}

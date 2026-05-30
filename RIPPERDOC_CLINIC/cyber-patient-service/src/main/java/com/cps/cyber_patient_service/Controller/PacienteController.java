package com.cps.cyber_patient_service.Controller;

import com.cps.cyber_patient_service.DTO.PacienteContratoDto;
import com.cps.cyber_patient_service.Model.Paciente;
import com.cps.cyber_patient_service.Service.PacienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Habilita el framework para hacer logs
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/paciente")
@RequiredArgsConstructor
@Slf4j // Inyecta automáticamente el objeto 'log' de Lombok para auditoría y trazas en consola
public class PacienteController {
    private final PacienteService pacienteService;

    /**
     * 1. LISTA DE PACIENTES
     * Metodo HTTP: GET
     * Endpoint: http://localhost:8081/api/v1/paciente
     */
    @GetMapping
    public ResponseEntity<List<Paciente>> listarTodos() {
        log.info("=== Solicitud de listado de clientes recibida ===");
        List<Paciente> lista = pacienteService.obtenerTodos();

        // El símbolo {} es un comodín (placeholder) dinámico que reemplaza su valor por el tamaño de la lista
        log.info("Listado enviado con éxito. Total de pacientes: {}", lista.size());
        return ResponseEntity.ok(lista);
    }

    /**
     * 2. BUSCAR UN PACIENTE ESPECÍFICO POR ID
     * Metodo HTTP: GET
     * Endpoint: http://localhost:8081/api/v1/paciente/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscarPorId(@PathVariable Long id) {
        log.info("Buscando paciente con ID: {}", id);
        Paciente paciente = pacienteService.obtenerPorId(id);
        log.info("Paciente encontrado: {}", paciente.getAlias());
        return ResponseEntity.ok(paciente);
    }

    /**
     * 3. REGISTRAR UN NUEVO PACIENTE (CREAR)
     * Metodo HTTP: POST
     * Endpoint: http://localhost:8081/api/v1/paciente
     */
    @PostMapping
    public ResponseEntity<Paciente> crearPaciente(@Valid @RequestBody Paciente paciente) {
        log.info("Registrando nuevo paciente: {}", paciente.getAlias());
        Paciente guardado = pacienteService.guardar(paciente);
        // ESPACIO EN EL QUE SE VA A MANDAR A CONECTAR CON FINANCE PARA QUE SE CREE AUTOMATICAMENTE LA BILLETERA UNA VEZ QUE ESTE LISTO ESE MICROSERVICIO
        log.info("¡PACIENTE REGISTRADO CON ÉXITO! Asignado ID: {} | Alias: {}", guardado.getId(), guardado.getAlias());

        // Retornamos un estado 201 Created por buenas prácticas de diseño arquitectónico REST
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    /**
     * 4. ELIMINAR UN PACIENTE DEL REGISTRO
     * Metodo HTTP: DELETE
     * Endpoint: http://localhost:8081/api/v1/paciente/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id) {
        // Usamos log.warn para alertar en consola que se está ejecutando una acción destructiva
        log.warn("ALERTA: Solicitud de eliminación del paciente con ID: {}", id);
        pacienteService.eliminar(id);
        log.info("Paciente ID: {} eliminado permanentemente", id);

        // Retornamos un 204 No Content porque el recurso ya no existe y no hay cuerpo que devolver
        return ResponseEntity.noContent().build();
    }

    /**
     * 5. ACTUALIZAR NIVEL DE CYBERPSICOSIS
     * Metodo HTTP: PATCH (Usado porque modificamos de forma parcial un solo atributo: el nivel de cyberpsicosis)
     * Endpoint: http://localhost:8081/api/v1/paciente/{id}/psico?modificador=VALUE
     * Nota: Este endpoint está pensado para ser disparado internamente tras una instalacion y posterior registro de alteracion de humanidad.
     */
    @PatchMapping("/{id}/psico")
    public ResponseEntity<Paciente> NvlCyberpsico(@PathVariable Long id, @RequestParam int modificador) {
        log.info("Iniciando actualizacion de humanidad de ID: {}", id);
        Paciente actualizado = pacienteService.actualizacionCyberpsicosis(id, modificador);
        log.info("Nivel de Cyberpsicosis actualizado con éxito. Nivel actual para '{}': {}", actualizado.getAlias(), actualizado.getNivelCyberpsicosis());
        return ResponseEntity.ok(actualizado);
    }

    // ENDPOINT DE INTEGRACIÓN: CONTRATO GLOBAL PARA EL FLUJO DE VENTAS
    /**
     * 8. EMITIR CONTRATO GLOBAL DE COMPRA
     * Metodo HTTP: GET
     * Endpoint: http://localhost:8081/api/v1/paciente/{id}/contrato
     * Retorna un DTO optimizado (ID, ALIAS).
     */
    @GetMapping("/{id}/contrato")
    public ResponseEntity<PacienteContratoDto> obtenerContratoParaVenta(@PathVariable Long id) {
        log.info("Emitiendo Contrato Global para compra de ID: {}", id);
        Paciente paciente = pacienteService.obtenerPorId(id);

        // Se empaquetan los datos de la entidad de la BD hacia el DTO de transferencia de red
        PacienteContratoDto contrato = new PacienteContratoDto(
                paciente.getId(),
                paciente.getAlias()
        );

        log.info("Contrato Global de '{}' generado con éxito. Enviando a Ventas...", contrato.getAliasPaciente());
        return ResponseEntity.ok(contrato);
    }
}

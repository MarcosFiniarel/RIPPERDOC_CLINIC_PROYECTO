package com.csus.cyber_surgery_service.Service;

import com.csus.cyber_surgery_service.DTO.CirugiaRequestDto;
import com.csus.cyber_surgery_service.Model.Instalacion;
import com.csus.cyber_surgery_service.Repository.InstalacionRepository;
import com.csus.cyber_surgery_service.WebClient.PacienteClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstalacionService {

    private final InstalacionRepository instalacionRepository;
    private final PacienteClient  pacienteClient;

    /**
     * 1. CREAR UNA CIRUGÍA (ORQUESTADOR DE QUIRÓFANO)
     * Recibe el DTO enviado por Ventas, ensambla la entidad local en "EN PROCESO",
     * asegura el registro en la BD y gatilla la evaluación médica inmediata.
     */
    @Transactional
    public Instalacion crearCirugia(CirugiaRequestDto dto) {
        log.info("=== Quirófano: Desempaquetando DTO de Ventas para Paciente ID: {} ===", dto.getPacienteId());

        // Transcribimos los datos del DTO
        Instalacion nuevaCirugia = new Instalacion();
        nuevaCirugia.setVentaId(dto.getVentaId());
        nuevaCirugia.setPacienteId(dto.getPacienteId());
        nuevaCirugia.setCiberwareId(dto.getCiberwareId());
        nuevaCirugia.setImpactoHumanidad(dto.getImpactoHumanidad());

        // El estado nace como "EN PROCESO" (Valor por defecto establecido en la DTO)
        nuevaCirugia.setEstado(dto.getEstado());

        // Salvamos la orden inicial en la base de datos de Cirugías
        Instalacion cirugiaRegistrada = instalacionRepository.save(nuevaCirugia);
        log.info("Orden de quirófano en pre-salva: ID {} | Estado: {}",
                cirugiaRegistrada.getId(), cirugiaRegistrada.getEstado());

        // PASO 6 SIMULADO: Evaluamos y mutamos el estado de manera síncrona e inmediata
        boolean estadoIntervencion = true;

        return procesarResultadoCirugia(cirugiaRegistrada, estadoIntervencion);
    }

    // 2. LISTAR TODAS LAS CIRUGÍAS
    @Transactional(readOnly = true)
    public List<Instalacion> obtenerTodas() {
        log.info("Buscando historial completo de cirugías en la base de datos.");
        return instalacionRepository.findAll();
    }

    // 3. LISTAR CIRUGÍAS POR PACIENTE
    @Transactional(readOnly = true)
    public List<Instalacion> obtenerPorPaciente(Long pacienteId) {
        log.info("Filtrando bitácora de cirugías para el Paciente ID: {}", pacienteId);
        return instalacionRepository.findByPacienteId(pacienteId);
    }

    // 4. LISTAR CIRUGÍAS POR CIBERWARE
    @Transactional(readOnly = true)
    public List<Instalacion> obtenerPorCiberware(Long ciberwareId) {
        log.info("Filtrando bitácora de cirugías para el Ciberware ID: {}", ciberwareId);
        return instalacionRepository.findByCiberwareId(ciberwareId);
    }

    // 5. BUSCAR POR ID DE CIRUGÍA
    @Transactional(readOnly = true)
    public Instalacion obtenerPorId(Long id) {
        log.info("Buscando reporte médico de cirugía ID: {}", id);
        return instalacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "El reporte de cirugía Nro " + id + " no existe."));
    }

    /**
     * 6. ACTUALIZAR ESTADO DE LA CIRUGÍA (EVALUADOR INTERNO DE SALUD)
     * Modifica el estado basándose en el resultado de la intervención.
     * Mantiene la buena práctica: save y return ocurren únicamente dentro del bloque exitoso.
     */
    private Instalacion procesarResultadoCirugia(Instalacion cirugia, boolean estadoOperacion) {
        if (estadoOperacion) {
            log.info("¡Operación completada con éxito! Implante acoplado al sistema nervioso. Estado: EXITOSA");
            cirugia.setEstado("EXITOSA");
            // Forzamos el guardado del nuevo estado previendo fallos en la red al momento de conectar con el cambio de humanidad
            Instalacion cirugiaActualizada = instalacionRepository.saveAndFlush(cirugia);

            // --- [FLAG DE INTEGRACIÓN FUTURA: IMPACTO PSICOLÓGICO EN EL PACIENTE] ---
            log.info("--- [FLAG PACIENTE] Enviando actualización de ciberpsicosis (+{}%) al Paciente ID: {} ---",
                    cirugia.getImpactoHumanidad(), cirugia.getPacienteId());

            pacienteClient.actualizarNivelCyberpsicosis(
                    cirugiaActualizada.getPacienteId(),
                    cirugiaActualizada.getImpactoHumanidad()
            );

            // Persistimos y retornamos el objeto final modificado
            return cirugiaActualizada;
        }

        // Caso de Fallo Quirúrgico (Bases listas para el alcance futuro del proyecto)
        log.warn("Fallo crítico en el acoplamiento del cromo. Abortando instalación quirúrgica.");
        cirugia.setEstado("FALLIDA");

        // Se fuerza el guardado inmediato en el disco (flush) antes de interrumpir el hilo transaccional
        instalacionRepository.saveAndFlush(cirugia);

        throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Rechazo agudo del implante. Cirugía abortada por el Matasanos.");
    }

    // 7. ELIMINAR CIRUGÍA
    @Transactional
    public void eliminarCirugia(Long id) {
        log.warn("ALERTA: Solicitud de eliminación permanente para el registro de cirugía ID: {}", id);
        Instalacion cirugia = obtenerPorId(id);
        instalacionRepository.delete(cirugia);
        log.info("Registro de cirugía ID: {} removido del sistema exitosamente.", id);
    }
}

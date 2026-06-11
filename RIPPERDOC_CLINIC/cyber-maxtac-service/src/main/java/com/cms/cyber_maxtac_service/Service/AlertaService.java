package com.cms.cyber_maxtac_service.Service;

import com.cms.cyber_maxtac_service.Model.Alerta;
import com.cms.cyber_maxtac_service.Repository.AlertaRepository;
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
public class AlertaService {

    private final AlertaRepository alertaRepository;

    /**
     * CREAR ALERTA DE MAXTAC (Despliegue Inmediato)
     * Parámetros inyectados desde el flujo síncrono de Ventas/Pacientes.
     */
    @Transactional
    public Alerta crearAlerta(Long pacienteId, String alias) {
        log.error("██████████████████████████████████████████████████████████");
        log.error("[ALERTA MAXTAC] BROTE PSICÓTICO DETECTADO EN LA MATRIZ");
        log.error("OBJETIVO: {} (Paciente ID: {})", alias, pacienteId);
        log.error("ESTADO: COMPAÑIA_EN_CAMINO | AMENAZA: CODIGO_NEGRO");
        log.error("██████████████████████████████████████████████████████████");

        Alerta nuevaAlerta = new Alerta();
        nuevaAlerta.setPacienteId(pacienteId);
        nuevaAlerta.setAlias(alias);

        // Forzamos los estados por defecto requeridos por las directrices de seguridad
        nuevaAlerta.setNivelAmenaza("CODIGO_NEGRO");
        nuevaAlerta.setEstadoDespliegue("COMPAÑIA_EN_CAMINO");

        return alertaRepository.save(nuevaAlerta);
    }

    /**
     * ACTUALIZAR ESTADO A AMENAZA_NEUTRALIZADA
     */
    @Transactional
    public Alerta neutralizarAmenaza(Long idAlerta) {
        Alerta alerta = obtenerPorId(idAlerta);

        alerta.setEstadoDespliegue("AMENAZA_NEUTRALIZADA");
        log.info("[MAXTAC] Orden de operaciones cerrada. Estado actualizado: AMENAZA_NEUTRALIZADA para la Alerta ID: {}", idAlerta);

        return alertaRepository.save(alerta);
    }

    /**
     * LISTAR TODAS LAS ALERTAS REGISTRADAS
     */
    @Transactional(readOnly = true)
    public List<Alerta> listarTodas() {
        log.info("[MAXTAC] Consultando bitácora completa de alertas tácticas");
        return alertaRepository.findAll();
    }

    /**
     * BUSCAR ALERTA POR ID DE ALERTA (Lanza 404 si no existe)
     */
    @Transactional(readOnly = true)
    public Alerta obtenerPorId(Long idAlerta) {
        log.info("[MAXTAC] Buscando reporte de alerta por ID: {}", idAlerta);
        return alertaRepository.findById(idAlerta)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "El registro de Alerta de MaxTac no existe en el sistema corporativo."));
    }

    /**
     * BUSCAR ALERTAS POR ID DE PACIENTE
     */
    @Transactional(readOnly = true)
    public List<Alerta> obtenerPorPacienteId(Long pacienteId) {
        log.info("[MAXTAC] Rastreando historial de alertas para el Paciente ID: {}", pacienteId);
        return alertaRepository.findByPacienteId(pacienteId);
    }

    /**
     * BUSCAR ALERTAS POR ALIAS DEL PACIENTE
     */
    @Transactional(readOnly = true)
    public List<Alerta> obtenerPorAlias(String alias) {
        log.info("[MAXTAC] Buscando interceptos de red para el alias: {}", alias);
        return alertaRepository.findByAlias(alias);
    }

    /**
     * ELIMINAR UNA ALERTA (Purgar registro del sistema)
     */
    @Transactional
    public void eliminarAlerta(Long idAlerta) {
        Alerta alerta = obtenerPorId(idAlerta);
        alertaRepository.delete(alerta);
        log.warn("[MAXTAC] Registro de alerta ID: {} eliminado de los servidores centrales.", idAlerta);
    }
}

package com.ccos.cyber_compatibility_service.Service;

import com.ccos.cyber_compatibility_service.DTO.CiberwareCompatibilidadDto;
import com.ccos.cyber_compatibility_service.DTO.PacienteCompatibilidadDto;
import com.ccos.cyber_compatibility_service.Model.Compatibilidad;
import com.ccos.cyber_compatibility_service.Repository.CompatibilidadRepository;
import com.ccos.cyber_compatibility_service.WebClient.CiberwareClient;
import com.ccos.cyber_compatibility_service.WebClient.PacienteClient;
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
public class CompatibilidadService {

    private final CompatibilidadRepository compatibilidadRepository;
    private final PacienteClient pacienteClient;
    private final CiberwareClient ciberwareClient;

    // 1. LISTAR
    @Transactional(readOnly = true)
    public List<Compatibilidad> obtenerTodas() {
        return compatibilidadRepository.findAll();
    }

    // 2. LISTAR POR ESTADO
    @Transactional(readOnly = true)
    public List<Compatibilidad> obtenerPorEstado(boolean estado) {
        return compatibilidadRepository.findByEstado(estado);
    }

    // 4. ELIMINAR
    @Transactional
    public void eliminar(Long id) {
        Compatibilidad registro = compatibilidadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Registro de compatibilidad no encontrado."));
        compatibilidadRepository.delete(registro);
    }

    /**
     * 3. EVALUAR Y REGISTRAR (Flujo Óptimo por Parámetros)
     * Recibe los IDs planos, consulta los DTOs biológicos en memoria y persiste la tabla limpia.
     */
    @Transactional
    public boolean evaluarYRegistrar(Long idCirugia, Long idPaciente, Long idCiberware) {
        log.info("=== Iniciando escaneo biológico para Cirugía ID: {} ===", idCirugia);

        // Paso A: Las sondas de red extraen los DTOs en memoria pura (RAM)
        PacienteCompatibilidadDto paciente = pacienteClient.obtenerDatosCompatibilidad(idPaciente);
        CiberwareCompatibilidadDto ciberware = ciberwareClient.obtenerRequisitosHardware(idCiberware);

        // Paso B: Procesamos el algoritmo matemático del "3 de 5"
        boolean resultadoCompatibilidad = evaluarCompatibilidad(paciente, ciberware);

        // Paso C: Persistencia estricta y limpia en la Base de Datos
        Compatibilidad compatibilidad = new Compatibilidad();
        compatibilidad.setIdCirugia(idCirugia);
        compatibilidad.setEstado(resultadoCompatibilidad);

        compatibilidadRepository.saveAndFlush(compatibilidad);
        log.info("Veredicto final inmutable guardado en BD para Cirugía Nro: {}. ¿Resultado?: {}", idCirugia, resultadoCompatibilidad);

        // Retornamos el booleano puro que espera el Quirófano
        return resultadoCompatibilidad;
    }

    /**
     * MOTOR DE REGLAS INTERNO: Criterio de 3 de 5 campos coincidentes.
     */
    private boolean evaluarCompatibilidad(PacienteCompatibilidadDto p, CiberwareCompatibilidadDto c) {
        int aciertos = 0;

        // Regla 1: Altura (ALTO, MEDIO, BAJO)
        if (p.getAltura() != null && p.getAltura().equalsIgnoreCase(c.getAltura())) {
            aciertos++;
            log.info("-> [OK] Altura estructural coincidente.");
        }

        // Regla 2: Densidad Ósea (ALTA, MEDIA, BAJA)
        if (p.getDensidadOsea() != null && p.getDensidadOsea().equalsIgnoreCase(c.getDensidadOsea())) {
            aciertos++;
            log.info("-> [OK] Tolerancia de Densidad Ósea coincidente.");
        }

        // Regla 3: Densidad Muscular (ALTA, MEDIA, BAJA)
        if (p.getDensidadMuscular() != null && p.getDensidadMuscular().equalsIgnoreCase(c.getDensidadMuscular())) {
            aciertos++;
            log.info("-> [OK] Capacidad de anclaje de Densidad Muscular coincidente.");
        }

        // Regla 4: Rango de Peso
        if (p.getPeso() != null && p.getPeso() >= c.getPesoMinimo() && p.getPeso() <= c.getPesoMaximo()) {
            aciertos++;
            log.info("-> [OK] Peso corporal dentro del rango de tolerancia.");
        }

        // Regla 5: Rango de Edad
        if (p.getEdad() != null && p.getEdad() >= c.getEdadMinima() && p.getEdad() <= c.getEdadMaxima()) {
            aciertos++;
            log.info("-> [OK] Edad apta para asimilación de neuro-conectores.");
        }

        log.info("Escaneo finalizado: {} de 5 aciertos biológicos.", aciertos);
        return aciertos >= 3;
    }
}

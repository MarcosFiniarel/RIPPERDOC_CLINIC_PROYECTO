package com.cps.cyber_patient_service.Service;

import com.cps.cyber_patient_service.Model.Paciente;
import com.cps.cyber_patient_service.Repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {
    private final PacienteRepository pacienteRepository;

    // 1. OBTENER TODOS los pacientes
    @Transactional(readOnly = true)
    public List<Paciente> obtenerTodos() {
        return pacienteRepository.findAll();
    }

    // 2. OBTENER por ID
    @Transactional(readOnly = true)
    public Paciente obtenerPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "El paciente con ID " + id + " no esta registrado."));
    }

    // 3. GUARDAR nuevo registro base
    @Transactional
    public Paciente guardar(Paciente paciente) {
        // Escudo anti-duplicados: interceptamos antes de tocar la base de datos
        if (pacienteRepository.existsByAlias(paciente.getAlias())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    paciente.getAlias() + " ya está registrado.");
        }
        return pacienteRepository.save(paciente);
    }

    // 4. ELIMINAR un registro base
    @Transactional
    public void eliminar(Long id) {
        Paciente paciente = obtenerPorId(id);
        pacienteRepository.delete(paciente);
    }

    // ACTUALIZACION DE CYBERPSICOSIS (Metodo pensado para ser usado por servicio de cirugia)
    @Transactional
    public Paciente actualizacionCyberpsicosis(Long id, int modificador) {
        Paciente paciente = obtenerPorId(id);

        // Calculamos la suma al nivel de psicosis
        int NvlCyberpsico = paciente.getNivelCyberpsicosis() + modificador;
        // Aplicamos la actualizacion de nivel
        paciente.setNivelCyberpsicosis(NvlCyberpsico);
        Paciente pacienteActualizado = pacienteRepository.saveAndFlush(paciente);

        // Verificacion del nivel de Cyberpsicosis
        if(NvlCyberpsico >= 100){
            // Aqui se llama a maxtac (Comunicacion con su microservicio)
        }

        return pacienteActualizado;
    }
}

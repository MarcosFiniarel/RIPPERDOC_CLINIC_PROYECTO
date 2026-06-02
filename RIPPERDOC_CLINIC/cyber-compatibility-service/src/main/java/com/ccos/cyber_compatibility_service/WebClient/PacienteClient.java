package com.ccos.cyber_compatibility_service.WebClient;

import com.ccos.cyber_compatibility_service.DTO.PacienteCompatibilidadDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class PacienteClient {

    private final WebClient webClient;

    // Inyección de la propiedad cyber-patient-service.url definida en application.properties
    public PacienteClient(@Value("${cyber-patient-service.url}") String pacienteUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(pacienteUrl)
                .build();
    }

    /**
     * Extrae los datos biométricos puros del Edgerunner.
     * Sin flujos extras de verificación, va directo por el DTO.
     */
    public PacienteCompatibilidadDto obtenerDatosCompatibilidad(Long id) {
        log.info("[RED -> PACIENTES] Extrayendo perfil biométrico del ID: {}", id);

        return this.webClient.get()
                .uri("/" + id + "/compatibilidad") // Ruta que responde el DTO de compatibilidad en pacientes
                .retrieve()
                // Atajamos errores de red o caídas del servicio central
                .onStatus(HttpStatus.NOT_FOUND::equals, response ->
                        Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Error de escaneo: El paciente ID " + id + " no está registrado.")))
                .bodyToMono(PacienteCompatibilidadDto.class)
                .onErrorResume(error -> {
                    if (error instanceof ResponseStatusException) {
                        return Mono.error(error);
                    }
                    return Mono.error(new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Fallo crítico de comunicación con el microservicio de Pacientes."));
                })
                .block(); // Síncrono para retener el flujo matemático en el Service
    }
}

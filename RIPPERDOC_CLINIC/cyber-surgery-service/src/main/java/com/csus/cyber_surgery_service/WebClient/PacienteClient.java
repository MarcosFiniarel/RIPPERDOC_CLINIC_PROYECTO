package com.csus.cyber_surgery_service.WebClient;

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

    public PacienteClient(@Value("${cyber-patient-service.url}") String pacienteUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(pacienteUrl)
                .build();
    }

    /**
     * Envía la alteración psicológica del ciberware al perfil del paciente.
     * Golpea el endpoint: PATCH /{id}/psico?modificador=VALUE
     */
    public void actualizarNivelCyberpsicosis(Long pacienteId, int modificador) {
        log.info("[RED -> PACIENTES] Enviando impacto neurológico de cromo (+{}) al Paciente ID: {}", modificador, pacienteId);

        this.webClient.patch()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}/psico")
                        .queryParam("modificador", modificador)
                        .build(pacienteId))
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, response ->
                        Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "No se pudo actualizar la salud mental. El paciente ID " + pacienteId + " no existe.")))
                .bodyToMono(Void.class) // Solo nos importa que retorne un estado 2xx exitoso
                .onErrorResume(error -> {
                    if (error instanceof ResponseStatusException) {
                        return Mono.error(error);
                    }
                    return Mono.error(new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Error crítico de red: El servidor de Pacientes no responde al reporte de psicosis."));
                })
                .block(); // Síncrono para garantizar consistencia transaccional

        log.info("[PACIENTES -> RED] Perfil psicológico actualizado correctamente en los servidores centrales.");
    }
}

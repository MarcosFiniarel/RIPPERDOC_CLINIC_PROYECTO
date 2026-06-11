package com.cps.cyber_patient_service.WebClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class MaxtacClient {

    private final WebClient webClient;

    public MaxtacClient(@Value("${cyber-maxtac-service.url}") String maxtacUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(maxtacUrl) // Carga: http://localhost:8087/api/v1/alertas
                .build();
    }

    /**
     * Dispara una alerta de emergencia asíncrona hacia la central de MaxTac.
     * Golpea: POST http://localhost:8087/api/v1/alertas?pacienteId=X&alias=Y
     */
    public void dispararAlertaCyberpsicosis(Long pacienteId, String alias) {
        log.warn("[RED -> MAXTAC] ¡Gatillando protocolo de contención urgente para el alias: {}!", alias);

        this.webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("pacienteId", pacienteId)
                        .queryParam("alias", alias)
                        .build())
                .retrieve()
                // Si MaxTac responde con códigos de error corporativos
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), response ->
                        Mono.error(new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Error en los servidores de MaxTac: No se pudo despachar la unidad táctica.")))
                // Mapeamos a Void.class ya que el service de pacientes solo necesita disparar la alerta, no procesar el objeto completo
                .bodyToMono(Void.class)
                // Escudo protector en caso de caída total del microservicio de MaxTac (8087 offline)
                .onErrorResume(error -> {
                    if (error instanceof ResponseStatusException) {
                        return Mono.error(error);
                    }
                    return Mono.error(new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Error crítico de red: El canal de comunicación con MaxTac está caído."));
                })
                .block(); // Bloqueo síncrono para asegurar la transmisión de la alerta de forma inmediata

        log.info("[RED -> MAXTAC] Alerta transmitida con éxito a las unidades en camino.");
    }
}

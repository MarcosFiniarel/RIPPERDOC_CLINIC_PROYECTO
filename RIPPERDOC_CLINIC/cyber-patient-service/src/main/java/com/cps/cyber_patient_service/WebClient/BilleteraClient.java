package com.cps.cyber_patient_service.WebClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class BilleteraClient {
    private final WebClient webClient;

    public BilleteraClient(@Value("${cyber-finance-service.url}") String pacienteUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(pacienteUrl)
                .build();
    }

    /**
     * Gatilla la creación síncrona de una billetera digital en el microservicio de Finanzas.
     */
    public void crearBilleteraDigital(Long pacienteId) {
        log.info("=== Red: Solicitando apertura de Billetera Digital en Finanzas para Paciente ID: {} ===", pacienteId);

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        // Dejamos el path vacío porque ya viene en la Base URL, solo sumamos el query parameter
                        .queryParam("id", pacienteId)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("Error devuelto por el servicio de Finanzas: {}", errorBody);
                                    return Mono.error(new ResponseStatusException(
                                            HttpStatus.BAD_REQUEST,
                                            "El sistema financiero rechazó la apertura de cuenta para el ID: " + pacienteId));
                                }))
                .bodyToMono(Void.class)
                .block();

        log.info("Billetera digital sincronizada correctamente para Paciente ID: {}", pacienteId);
    }
}

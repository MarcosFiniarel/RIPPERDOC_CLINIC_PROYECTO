package com.css.cyber_sale_service.WebClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class TransaccionClient {

    private final WebClient webClient;

    public TransaccionClient(@Value("${cyber-finance-service.url}") String financeUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(financeUrl) // Carga directo: http://localhost:8084/api/v1/transacciones
                .build();
    }

    /**
     * Solicita de forma síncrona el cobro de la venta al servicio financiero.
     * Golpea: POST http://localhost:8084/api/v1/transacciones?idVenta=X&idPaciente=Y&monto=Z
     */
    public boolean procesarCobroEdgerunner(Long idVenta, Long idPaciente, Double monto) {
        log.info("[RED -> FINANZAS] Solicitando procesamiento de cobro para Venta ID: {}, Monto: {} Eddies", idVenta, monto);

        Boolean transaccionExitosa = this.webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("idVenta", idVenta)
                        .queryParam("idPaciente", idPaciente)
                        .queryParam("monto", monto)
                        .build())
                .retrieve()
                // Si el servidor financiero responde con un error de estatus
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), response ->
                        Mono.error(new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Error en la matriz financiera: No se pudo procesar la solicitud de cobro.")))
                .bodyToMono(Boolean.class)
                // Escudo protector por si el microservicio de Finanzas está completamente caído
                .onErrorResume(error -> {
                    if (error instanceof ResponseStatusException) {
                        return Mono.error(error);
                    }
                    return Mono.error(new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Error crítico de red: El microservicio de Finanzas no responde. Red caída."));
                })
                .block(); // Bloqueo síncrono para esperar la validación antes de soltar el cromo

        return transaccionExitosa != null && transaccionExitosa;
    }
}

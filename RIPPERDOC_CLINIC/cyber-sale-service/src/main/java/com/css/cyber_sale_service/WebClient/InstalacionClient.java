package com.css.cyber_sale_service.WebClient;

import com.css.cyber_sale_service.DTO.CirugiaRequestDto;
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
public class InstalacionClient {

    private final WebClient webClient;

    // Inyección directa en el constructor mediante la propiedad de tu application.properties
    public InstalacionClient(@Value("${cyber-surgery-service.url}") String surgeryUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(surgeryUrl)
                .build();
    }

    /**
     * Dispara la orden de creación de cirugía en el microservicio quirúrgico.
     * Envía tu CirugiaRequestDto tradicional y espera el OK del quirófano.
     */
    public void solicitarCirugiaInmediata(CirugiaRequestDto solicitud) {
        log.info("[RED -> CIRUGÍAS] Solicitando apertura de quirófano para la Venta ID: {}", solicitud.getVentaId());

        this.webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(solicitud)
                .retrieve()
                // Interceptamos si el quirófano tira un fallo interno (por ejemplo, el 500 del rechazo agudo)
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, response ->
                        Mono.error(new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "El Matasanos reportó un fallo crítico en el quirófano. Instalación abortada.")))
                .bodyToMono(Void.class) // Solo nos interesa que responda un estado 2xx
                // Si el microservicio de cirugía está completamente apagado o inaccesible
                .onErrorResume(error -> {
                    if (error instanceof ResponseStatusException) {
                        return Mono.error(error);
                    }
                    return Mono.error(new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Error crítico de red: El servidor de quirófano (cyber-surgery-service) no responde."));
                })
                .block(); // Sincrónico para asegurar que la venta no termine si el quirófano falla

        log.info("[CIRUGÍAS -> RED] Quirófano reporta: ¡Instalación completada con éxito absoluto!");
    }
}
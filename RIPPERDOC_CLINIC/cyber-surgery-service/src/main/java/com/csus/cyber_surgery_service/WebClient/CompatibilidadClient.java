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
public class CompatibilidadClient {

    private final WebClient webClient;

    public CompatibilidadClient(@Value("${cyber-compatibility-service.url}") String compatibilityUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(compatibilityUrl)
                .build();
    }

    /**
     * Envía la orden de escaneo biológico al microservicio de compatibilidad.
     * Golpea: POST /evaluar?idCirugia=X&idPaciente=Y&idCiberware=Z
     */
    public boolean verificarCompatibilidadBiologica(Long idCirugia, Long idPaciente, Long idCiberware) {
        log.info("[RED -> COMPATIBILIDAD] Solicitando peritaje de cromo para Cirugía ID: {}", idCirugia);

        Boolean esCompatible = this.webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/evaluar")
                        .queryParam("idCirugia", idCirugia)
                        .queryParam("idPaciente", idPaciente)
                        .queryParam("idCiberware", idCiberware)
                        .build())
                .retrieve()
                // Si ocurre un error de negocio o no se encuentra algún dato en los otros microservicios
                .onStatus(HttpStatus.NOT_FOUND::equals, response ->
                        Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "El escaneo no se pudo procesar. Uno de los recursos no existe en el sistema.")))
                .bodyToMono(Boolean.class)
                // Escudo protector si el servidor de compatibilidad se cae por completo
                .onErrorResume(error -> {
                    if (error instanceof ResponseStatusException) {
                        return Mono.error(error);
                    }
                    return Mono.error(new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Error crítico de red: El servicio de Compatibilidad Biológica no responde."));
                })
                .block(); // Sincrónico para congelar el quirófano hasta tener el veredicto

        return esCompatible != null && esCompatible;
    }
}

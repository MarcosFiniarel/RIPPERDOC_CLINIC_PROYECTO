package com.ccos.cyber_compatibility_service.WebClient;

import com.ccos.cyber_compatibility_service.DTO.CiberwareCompatibilidadDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CiberwareClient {

    private final WebClient webClient;

    // Inyección de la propiedad cyber-ciberware-service.url definida en application.properties
    public CiberwareClient(@Value("${cyber-ciberware-service.url}") String ciberwareUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(ciberwareUrl)
                .build();
    }

    /**
     * Extrae las tolerancias físicas del ciberware desde el catálogo.
     */
    public CiberwareCompatibilidadDto obtenerRequisitosHardware(Long id) {
        log.info("[RED -> CIBERWARE] Extrayendo requerimientos de hardware del ID: {}", id);

        return this.webClient.get()
                .uri("/" + id + "/compatibilidad") // Ruta que responde el DTO de compatibilidad en ciberware
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, response ->
                        Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Error de escaneo: El ciberware ID " + id + " no existe en el catálogo.")))
                .bodyToMono(CiberwareCompatibilidadDto.class)
                .onErrorResume(error -> {
                    if (error instanceof ResponseStatusException) {
                        return Mono.error(error);
                    }
                    return Mono.error(new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Fallo crítico de comunicación con el microservicio de Ciberware."));
                })
                .block(); // Síncrono para el procesamiento secuencial
    }
}

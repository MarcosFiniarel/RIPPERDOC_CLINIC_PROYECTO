package com.css.cyber_sale_service.WebClient;

import com.css.cyber_sale_service.DTO.PacienteContratoDto;
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
     * Verifica la existencia del Edgerunner y obtiene su DTO en un solo viaje de red.
     * Si no existe, dispara un error que atajará el GlobalExceptionHandler.
     */
    public PacienteContratoDto verificarYObtenerPaciente(Long id) {
        log.info("[RED -> PACIENTES] Validando existencia y solicitando contrato del ID: {}", id);

        return this.webClient.get()
                .uri("/" + id)
                .retrieve()
                // interceptamos el 404 del microservicio de Pacientes para dar un mensaje personalizado
                .onStatus(HttpStatus.NOT_FOUND::equals, response ->
                        Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "El Edgerunner con ID " + id + " no existe en los registros de la clínica. Venta abortada.")))
                .bodyToMono(PacienteContratoDto.class)
                // Si el microservicio de pacientes está totalmente caído (500, Connection Refused, etc.)
                .onErrorResume(error -> {
                    if (error instanceof ResponseStatusException) {
                        return Mono.error(error); // Pasamos el 404 que ya manejamos arriba
                    }
                    return Mono.error(new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Error crítico en la red al conectar con el servicio de Pacientes."));
                })
                .block(); // Sincrónico para congelar la transacción de venta hasta estar seguros
    }
}

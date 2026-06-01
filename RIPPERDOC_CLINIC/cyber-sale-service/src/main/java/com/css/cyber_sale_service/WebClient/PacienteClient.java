package com.css.cyber_sale_service.WebClient;

import com.css.cyber_sale_service.DTO.CiberwareContratoDto;
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

    // Chequea disponibilidad técnica de stock
    private boolean verificarDisponibilidad(Long id) {
        log.info("[RED -> PACIENTES] Verificando habilitacion para ID: {}", id);

        Boolean disponible = this.webClient.get()
                .uri("/" + id + "/check")
                .retrieve()
                // Si el catálogo responde 404 (No existe), manejamos el error devolviendo false en el flujo
                .onStatus(HttpStatus.NOT_FOUND::equals, response -> Mono.empty())
                .bodyToMono(Boolean.class)
                // Si la red se cae por completo, devolvemos false por seguridad
                .onErrorReturn(false)
                .block();

        return disponible != null && disponible;
    }

    // Orquesta la verificación y la extracción del DTO
    public PacienteContratoDto obtenerContratoSiEstaDisponible(Long id) {
        // 1. Ejecutamos la verificación anidada
        boolean aptoParaCompra = verificarDisponibilidad(id);

        if (!aptoParaCompra) {
            log.warn("[RED -> PACIENTES] El paciente ID: {} NO está habilitado (Sobrepasa su capacidad de asimilacion)", id);
            // Lanzamos una excepción de Spring.
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El paciente no esta habilitado para comprar. Verifica el su nivel de humanidad.");
        }

        // 2. Si dio true, procedemos a traer el DTO completo
        log.info("[RED -> PACIENTE] Disponibilidad confirmada. Extrayendo contrato para ID: {}", id);

        return this.webClient.get()
                .uri("/" + id + "/contrato")
                .retrieve()
                .bodyToMono(PacienteContratoDto.class)
                // Si por alguna razón falla aquí, lanzamos un error controlado
                .onErrorResume(error -> Mono.error(new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Error crítico al recuperar el contrato del ciberware.")))
                .block();
    }
}

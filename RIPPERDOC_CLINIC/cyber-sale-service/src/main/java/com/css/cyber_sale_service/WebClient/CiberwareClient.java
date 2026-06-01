package com.css.cyber_sale_service.WebClient;

import com.css.cyber_sale_service.DTO.CiberwareContratoDto;
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

    public CiberwareClient(@Value("${cyber-ciberware-service.url}") String ciberwareUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(ciberwareUrl)
                .build();
    }


    // Chequea disponibilidad técnica de stock
    private boolean verificarDisponibilidad(Long id) {
        log.info("[RED -> CATÁLOGO] Verificando disponibilidad técnica para ID: {}", id);

        Boolean disponible = this.webClient.get()
                .uri("/" + id + "/disponible")
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
    public CiberwareContratoDto obtenerContratoSiEstaDisponible(Long id) {
        // 1. Ejecutamos la verificación anidada
        boolean aptoParaVenta = verificarDisponibilidad(id);

        if (!aptoParaVenta) {
            log.warn("[RED -> CATÁLOGO] El ciberware ID: {} NO está disponible (Sin stock o inexistente)", id);
            // Lanzamos una excepción de Spring.
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El ciberware solicitado no está disponible para la venta. Verifica el stock o el ID.");
        }

        // 2. Si dio true, procedemos a traer el DTO completo
        log.info("[RED -> CATÁLOGO] Disponibilidad confirmada. Extrayendo contrato para ID: {}", id);

        return this.webClient.get()
                .uri("/" + id + "/contrato")
                .retrieve()
                .bodyToMono(CiberwareContratoDto.class)
                // Si por alguna razón falla aquí, lanzamos un error controlado
                .onErrorResume(error -> Mono.error(new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Error crítico al recuperar el contrato del ciberware.")))
                .block();
    }

    // Una vez confirmada la venta deduce el stock de ciberware
    public void deducirStockCiberware(Long id) {
        log.info("[RED -> CATÁLOGO] Disparando deducción automática de stock para ID: {}", id);
        this.webClient.patch()
                .uri("/" + id + "/deducir")
                .retrieve()
                .toBodilessEntity() // No nos interesa el cuerpo de respuesta, solo que ejecute la acción
                .block();
        log.info("[CATÁLOGO -> RED] Stock deducido correctamente.");
    }
}

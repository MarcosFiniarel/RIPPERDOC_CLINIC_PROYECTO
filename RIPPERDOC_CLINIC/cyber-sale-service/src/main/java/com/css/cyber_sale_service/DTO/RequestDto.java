package com.css.cyber_sale_service.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Payload minimalista requerido para iniciar el flujo automatizado de venta de cromo")
public class RequestDto {
    @NotNull(message = "El ID del paciente no puede estar vacio")
    @Schema(description = "ID del mercenario que solicita la compra", example = "1")
    private Long idPaciente;

    @NotNull(message = "El ID del ciberware no puede ser nulo")
    @Schema(description = "ID de la pieza del catálogo a adquirir", example = "1")
    private Long idCiberware;
}

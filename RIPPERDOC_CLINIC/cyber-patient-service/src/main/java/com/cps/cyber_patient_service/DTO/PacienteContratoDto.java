package com.cps.cyber_patient_service.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta que expone la firma digital básica de identidad para el flujo del punto de venta")
public class PacienteContratoDto {
    @Schema(description = "ID del paciente localizado en los servidores de la red", example = "1")
    private Long idPaciente;
    @Schema(description = "Alias o nombre callejero del comprador", example = "David Martínez")
    private String AliasPaciente;
}

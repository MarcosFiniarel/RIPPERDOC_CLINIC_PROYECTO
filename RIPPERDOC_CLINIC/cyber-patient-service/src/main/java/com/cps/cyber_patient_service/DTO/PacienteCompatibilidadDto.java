package com.cps.cyber_patient_service.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO que transmite de forma aislada las métricas biológicas actuales del sujeto para contrastar con el cromo")
public class PacienteCompatibilidadDto {
    @Schema(description = "Estatura registrada en la ficha biomédica", example = "ALTO", allowableValues = {"ALTO", "MEDIO", "BAJO"})
    private String altura; // ALTO, MEDIO, BAJO
    @Schema(description = "Calidad actual de la estructura ósea del mercenario", example = "ALTA", allowableValues = {"ALTA", "MEDIA", "BAJA"})
    private String densidadOsea; // ALTA, MEDIA, BAJA
    @Schema(description = "Nivel de desarrollo de los tejidos musculares", example = "ALTA", allowableValues = {"ALTA", "MEDIA", "BAJA"})
    private String densidadMuscular; // ALTA, MEDIA, BAJA
    @Schema(description = "Peso actual en kilogramos medido en la clínica", example = "85.5")
    private Double peso;
    @Schema(description = "Edad cronológica del paciente", example = "18")
    private Integer edad;
}

package com.ccs.cyber_ciberware_service.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta interna con las restricciones corporales mínimas y máximas requeridas por el implante")
public class CiberwareCompatibilidadDto {
    @Schema(description = "Rango de estatura biológica obligatoria", example = "ALTO", allowableValues = {"ALTO", "MEDIO", "BAJO"})
    private String altura; // ALTO, MEDIO, BAJO
    @Schema(description = "Estructura ósea necesaria para el anclaje", example = "ALTA", allowableValues = {"ALTA", "MEDIA", "BAJA"})
    private String densidadOsea; // ALTA, MEDIA, BAJA
    @Schema(description = "Densidad muscular requerida para los impulsos", example = "ALTA", allowableValues = {"ALTA", "MEDIA", "BAJA"})
    private String densidadMuscular; // ALTA, MEDIA, BAJA
    @Schema(description = "Masa corporal mínima permitida en kilogramos", example = "80.0")
    private Double pesoMinimo;
    @Schema(description = "Masa corporal máxima tolerada por la pieza", example = "110.0")
    private Double pesoMaximo;
    @Schema(description = "Edad mínima para que el sistema inmunológico no rechace el cromo", example = "17")
    private Integer edadMinima;
    @Schema(description = "Edad máxima estimada para una asimilación neuronal óptima", example = "25")
    private Integer edadMaxima;
}

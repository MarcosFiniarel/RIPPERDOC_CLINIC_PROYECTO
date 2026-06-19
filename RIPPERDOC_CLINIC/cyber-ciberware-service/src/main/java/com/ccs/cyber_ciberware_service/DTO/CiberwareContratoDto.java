package com.ccs.cyber_ciberware_service.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO optimizado con los datos financieros y psicológicos estrictos para el contrato de cobro")
public class CiberwareContratoDto {
    @Schema(description = "ID único de la pieza en vitrina", example = "1")
    private Long id;
    @Schema(description = "Nombre oficial del implante militar/civil", example = "Arasaka Sandevistan MK.V")
    private String nombre;
    @Schema(description = "Precio final de venta en Eurodólares", example = "120000.0")
    private Double costoEddies;
    @Schema(description = "Costo neuronal de humanidad que se inyectará en la psique del paciente", example = "88")
    private Integer costoHumanidad;
}

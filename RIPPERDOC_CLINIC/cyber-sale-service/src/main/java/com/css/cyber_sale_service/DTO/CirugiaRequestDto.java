package com.css.cyber_sale_service.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de integración enviado internamente para inicializar la programación de la cirugía post-pago")
public class CirugiaRequestDto {
    @Schema(description = "ID de la orden de venta confirmada", example = "105")
    private Long ventaId;

    @Schema(description = "ID del paciente a operar", example = "1")
    private Long pacienteId;

    @Schema(description = "ID del implante a instalar", example = "1")
    private Long ciberwareId;

    @Schema(description = "Costo de estabilidad mental que recibirá el paciente", example = "88")
    private Integer impactoHumanidad;

    @Schema(description = "Estado operativo inicial de la orden quirúrgica", example = "EN PROCESO")
    private String estado = "EN PROCESO";

    public CirugiaRequestDto(Long ventaId, Long pacienteId, Long ciberwareId, Integer impactoHumanidad) {
        this.ventaId = ventaId;
        this.pacienteId = pacienteId;
        this.ciberwareId = ciberwareId;
        this.impactoHumanidad = impactoHumanidad;
    }
}

package com.css.cyber_sale_service.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CirugiaRequestDto {
    private Long ventaId;
    private Long pacienteId;
    private Long ciberwareId;
    private Integer impactoHumanidad;
    private String estado = "EN PROCESO";
    public CirugiaRequestDto(Long ventaId, Long pacienteId, Long ciberwareId, Integer impactoHumanidad) {
        this.ventaId = ventaId;
        this.pacienteId = pacienteId;
        this.ciberwareId = ciberwareId;
        this.impactoHumanidad = impactoHumanidad;
    }
}

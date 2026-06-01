package com.csus.cyber_surgery_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CirugiaRequestDto {
    private Long ventaId;
    private Long pacienteId;
    private Long ciberwareId;
    private Integer impactoHumanidad;
    private String estado = "EN PROCESO";
}

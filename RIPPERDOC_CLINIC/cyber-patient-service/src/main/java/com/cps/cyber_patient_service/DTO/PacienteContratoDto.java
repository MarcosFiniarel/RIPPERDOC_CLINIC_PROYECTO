package com.cps.cyber_patient_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteContratoDto {
    private Long idPaciente;
    private String AliasPaciente;
}

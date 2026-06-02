package com.cps.cyber_patient_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteCompatibilidadDto {
    private String altura; // ALTO, MEDIO, BAJO
    private String densidadOsea; // ALTA, MEDIA, BAJA
    private String densidadMuscular; // ALTA, MEDIA, BAJA
    private Double peso;
    private Integer edad;
}

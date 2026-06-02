package com.ccs.cyber_ciberware_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CiberwareCompatibilidadDto {
    private String altura; // ALTO, MEDIO, BAJO
    private String densidadOsea; // ALTA, MEDIA, BAJA
    private String densidadMuscular; // ALTA, MEDIA, BAJA
    private Double pesoMinimo;
    private Double pesoMaximo;
    private Integer edadMinima;
    private Integer edadMaxima;
}

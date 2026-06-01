package com.css.cyber_sale_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CiberwareContratoDto {
    private Long id;
    private String nombre;
    private Double costoEddies;
    private Integer costoHumanidad;
}

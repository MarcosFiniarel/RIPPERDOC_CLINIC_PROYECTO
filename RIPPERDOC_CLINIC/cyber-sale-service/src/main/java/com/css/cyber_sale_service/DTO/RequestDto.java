package com.css.cyber_sale_service.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    @NotNull(message = "El ID del paciente no puede esar vacio")
    private Long idPaciente;
    @NotNull(message = "El ID del ciberware no puede ser nulo")
    private Long idCiberware;
}

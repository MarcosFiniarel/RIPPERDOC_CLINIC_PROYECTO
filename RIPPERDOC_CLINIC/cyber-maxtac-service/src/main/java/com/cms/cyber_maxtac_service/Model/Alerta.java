package com.cms.cyber_maxtac_service.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "alerta")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Alerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del paciente objetivo es obligatorio")
    private Long pacienteId; // Referencia lógica al cyber-patient-service

    @NotBlank(message = "El nivel de amenaza es obligatorio")
    private String nivelAmenaza = "CODIGO_NEGRO";

    @NotBlank(message = "El estado de despliegue es obligatorio")
    private String estadoDespliegue = "COMPAÑIA_EN_CAMINO"; // "COMPAÑIA_EN_CAMINO", "NEUTRALIZADO"
}

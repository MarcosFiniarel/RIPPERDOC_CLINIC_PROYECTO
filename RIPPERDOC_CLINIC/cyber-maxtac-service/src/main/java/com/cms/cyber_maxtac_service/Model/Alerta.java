package com.cms.cyber_maxtac_service.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "alerta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo conceptual que representa un reporte de emergencia e intervención militar de MaxTac por cyberpsicosis")
public class Alerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único incremental del reporte de alerta táctica", example = "901")
    private Long id;

    @NotNull(message = "El ID del paciente objetivo es obligatorio")
    @Schema(description = "Referencia lógica al ID del paciente que sufrió la degradación mental severa", example = "1")
    private Long pacienteId;

    @NotBlank(message = "El alias del cyberpsico no puede estar en blanco")
    @Schema(description = "Nombre callejero del mercenario catalogado ahora como cyberpsicópata", example = "David Martínez")
    private String alias;

    @NotBlank(message = "El nivel de amenaza es obligatorio")
    @Schema(description = "Protocolo de peligro asignado por la central de red", example = "CODIGO_NEGRO")
    private String nivelAmenaza = "CODIGO_NEGRO";

    @NotBlank(message = "El estado de despliegue es obligatorio")
    @Schema(description = "Estado operativo actual de las fuerzas tácticas helitransportadas", allowableValues = {"COMPAÑIA_EN_CAMINO", "NEUTRALIZADO"}, example = "COMPAÑIA_EN_CAMINO")
    private String estadoDespliegue = "COMPAÑIA_EN_CAMINO";
}

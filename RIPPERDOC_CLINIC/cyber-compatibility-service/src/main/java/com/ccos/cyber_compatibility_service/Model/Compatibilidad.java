package com.ccos.cyber_compatibility_service.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "compatibilidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo conceptual que representa el veredicto del escaneo biomédico de compatibilidad entre un paciente y un ciberware")
public class Compatibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único incremental del registro de compatibilidad", example = "701")
    private Long id;

    @NotNull(message = "El id de la cirugia no puede ser nulo")
    @Schema(description = "Referencia lógica al ID de la cirugía evaluada", example = "501")
    private Long idCirugia;

    @NotNull(message = "El estado de compatibilidad no puede estar vacio")
    @Schema(description = "Veredicto final del análisis biológico (true = APTO, COMPATIBLE / false = RECHAZO BIOLÓGICO INMEDIATO)", example = "true")
    private boolean estado;
}

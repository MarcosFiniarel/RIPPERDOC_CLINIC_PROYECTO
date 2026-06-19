package com.cps.cyber_patient_service.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "paciente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo conceptual que representa a un paciente ciber-mejorado dentro de la red clínica")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único incremental de la red", example = "1")
    private Long id;

    @NotBlank(message = "El alias del paciente es obligatorio")
    @Size(max = 50, message = "El alias no puede superar los 50 caracteres")
    @Column(unique = true)
    @Schema(description = "Nombre callejero o alias del mercenario", example = "David Martínez")
    private String alias;

    @NotNull(message = "El nivel de cyberpsicosis no puede ser nulo")
    @Min(value = 0, message = "El nivel mínimo es 0%")
    @Schema(description = "Porcentaje actual de degradación neurológica por exceso de cromo", example = "88")
    private Integer nivelCyberpsicosis = 0;

    // ATRIBUTOS PARA COMPATIBILIDAD

    @NotBlank(message = "La altura no puede estar en blanco.")
    @Schema(description = "Clasificación de la estatura corporal", allowableValues = {"ALTO", "MEDIO", "BAJO"}, example = "ALTO")
    private String altura; // ALTO, MEDIO, BAJO

    @NotBlank(message = "La densidad osea no puede estar en blanco.")
    @Schema(description = "Calidad de la estructura ósea para soportar hardware pesado", allowableValues = {"ALTA", "MEDIA", "BAJA"}, example = "ALTA")
    private String densidadOsea; // ALTA, MEDIA, BAJA

    @NotBlank(message = "La densidad muscular no puede estar en blanco.")
    @Schema(description = "Nivel de fibra muscular para la asimilación nerviosa del cromo", allowableValues = {"ALTA", "MEDIA", "BAJA"}, example = "ALTA")
    private String densidadMuscular; // ALTA, MEDIA, BAJA

    @NotNull(message = "La peso no puede estar en blanco.")
    @Schema(description = "Masa corporal actual del paciente medida en kilogramos", example = "85.5")
    private Double peso;

    @NotNull(message = "La edad no puede estar en blanco.")
    @Schema(description = "Edad cronológica del sujeto", example = "18")
    private Integer edad;
}
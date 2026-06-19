package com.csus.cyber_surgery_service.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "instalacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo conceptual que representa el registro médico de una operación de instalación de implantes")
public class Instalacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único incremental del reporte quirúrgico", example = "501")
    private Long id;

    @NotNull(message = "El ID de la venta es obligatorio")
    @Schema(description = "Referencia lógica a la orden de compra que originó la cirugía", example = "105")
    private Long ventaId;

    @NotNull(message = "El ID del paciente es obligatorio")
    @Schema(description = "Referencia lógica al paciente que está en la camilla de operaciones", example = "1")
    private Long pacienteId;

    @NotNull(message = "El ID del ciberware es obligatorio")
    @Schema(description = "Referencia lógica a la pieza de cromo que se va a instalar", example = "1")
    private Long ciberwareId;

    @NotNull(message = "El impacto de humanidad es obligatorio")
    @Min(value = 0, message = "El impacto no puede ser negativo")
    @Schema(description = "Drenaje de humanidad que sufrirá la psique del paciente tras la operación", example = "88")
    private Integer impactoHumanidad;

    @NotBlank(message = "El estado no puede estar vacio")
    @Schema(description = "Estado actual del procedimiento en quirófano", allowableValues = {"EN PROCESO", "EXITOSA"}, example = "EXITOSA")
    private String estado = "EN PROCESO";
}

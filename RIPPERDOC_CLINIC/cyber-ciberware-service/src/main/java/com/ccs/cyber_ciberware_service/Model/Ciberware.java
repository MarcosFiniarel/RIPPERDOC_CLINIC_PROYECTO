package com.ccs.cyber_ciberware_service.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "ciberware")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo conceptual que representa una pieza de ciberware militar o civil en catálogo")
public class Ciberware {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único incremental del implante", example = "1")
    private Long id;

    @NotBlank(message = "El nombre del ciberware es obligatorio")
    @Column(unique = true)
    @Schema(description = "Nombre comercial o de diseño de la pieza", example = "Arasaka Sandevistan MK.V")
    private String nombre;

    @NotNull(message = "El costo en Eddies es obligatorio")
    @Positive(message = "El costo debe ser un valor positivo")
    @Schema(description = "Precio de venta en Eurodólares (Eddies)", example = "120000.0")
    private Double costoEddies;

    @NotNull(message = "El costo de humanidad es obligatorio")
    @Min(value = 0, message = "El costo de humanidad no puede ser negativo")
    @Schema(description = "Impacto psicológico y drenaje en la humanidad del paciente", example = "88")
    private Integer costoHumanidad;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Schema(description = "Unidades físicas disponibles en la vitrina de la clínica", example = "5")
    private Integer stock;

    // DATOS DE COMPATIBILIDAD DEL CIBERWARE

    @NotBlank(message = "La altura no puede estar en blanco.")
    @Schema(description = "Filtro de altura biológica requerida", allowableValues = {"ALTO", "MEDIO", "BAJO"}, example = "ALTO")
    private String alturaCompatibilidad; // ALTO, MEDIO, BAJO

    @NotBlank(message = "La densidad osea no puede estar en blanco.")
    @Schema(description = "Filtro de densidad ósea necesaria para soportar el peso", allowableValues = {"ALTA", "MEDIA", "BAJA"}, example = "ALTA")
    private String densidadOseaCompatibilidad; // ALTA, MEDIA, BAJA

    @NotBlank(message = "La densidad muscular no puede estar en blanco.")
    @Schema(description = "Filtro de densidad muscular necesaria para los impulsos nerviosos", allowableValues = {"ALTA", "MEDIA", "BAJA"}, example = "ALTA")
    private String densidadMuscularCompatibilidad; // ALTA, MEDIA, BAJA

    @NotNull(message = "La peso minimo no puede estar en blanco.")
    @Schema(description = "Límite de masa corporal mínimo requerido", example = "80.0")
    private Double pesoMinimoCompatibilidad;

    @NotNull(message = "La peso maximo no puede estar en blanco.")
    @Schema(description = "Límite de masa corporal máximo soportado", example = "110.0")
    private Double pesoMaximoCompatibilidad;

    @NotNull(message = "La edad no puede estar en blanco.")
    @Schema(description = "Edad mínima para que el sistema nervioso asimile el implante", example = "17")
    private Integer edadMinimaCompatibilidad;

    @NotNull(message = "La edad no puede estar en blanco.")
    @Schema(description = "Edad máxima antes de que los tejidos rechacen el cromo", example = "25")
    private Integer edadMaximaCompatibilidad;
}

package com.css.cyber_sale_service.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo conceptual que representa el registro y estado de una orden de compra de ciberware")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único incremental de la orden de venta", example = "105")
    private Long id;

    @NotNull(message = "El ID del paciente es obligatorio")
    @Schema(description = "Referencia lógica al ID del paciente comprador", example = "1")
    private Long pacienteId;

    @NotBlank(message = "El Alias no puede estar en blanco")
    @Schema(description = "Alias callejero del mercenario obtenido del servicio de pacientes", example = "David Martínez")
    private String aliasPaciente;

    @NotNull(message = "El ID del ciberware es obligatorio")
    @Schema(description = "Referencia lógica al ID del implante comprado", example = "1")
    private Long ciberwareId;

    @NotBlank(message = "El nombre del ciberware no puede estar en blanco")
    @Schema(description = "Nombre comercial de la pieza obtenido del catálogo", example = "Arasaka Sandevistan MK.V")
    private String nombreCiberware;

    @NotNull(message = "El precio cobrado es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    @Schema(description = "Monto final facturado por la pieza en Eurodólares (Eddies)", example = "120000.0")
    private Double precioCobrado;

    @NotNull(message = "El impacto de humanidad es obligatorio")
    @Min(value = 0, message = "El impacto no puede ser negativo")
    @Schema(description = "Drenaje neuronal de la pieza inyectado a la psique del paciente", example = "88")
    private Integer impactoHumanidad;

    @NotBlank(message = "El estado de la venta es obligatorio")
    @Schema(description = "Estado actual del flujo de la transacción", allowableValues = {"PENDIENTE", "PAGADA", "RECHAZADA"}, example = "PAGADA")
    private String estado = "PENDIENTE";
}
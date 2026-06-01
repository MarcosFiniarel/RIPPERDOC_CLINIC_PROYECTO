package com.css.cyber_sale_service.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "venta")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del paciente es obligatorio")
    private Long pacienteId; // Referencia lógica al cyber-patient-service

    @NotBlank(message = "El Alias no puede estar en blanco")
    private String aliasPaciente;

    @NotNull(message = "El ID del ciberware es obligatorio")
    private Long ciberwareId; // Referencia lógica al cyber-ciberware-service

    @NotBlank(message = "El nombre del ciberware no puede estar en blanco")
    private String nombreCiberware;

    @NotNull(message = "El precio cobrado es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    private Double precioCobrado; // Se trae como DTO del catálogo de cyber-ciberware-service

    @NotNull(message = "El impacto de humanidad es obligatorio")
    @Min(value = 0, message = "El impacto no puede ser negativo")
    private Integer impactoHumanidad; // Se trae como DTO del cyber-ciberware-service

    @NotBlank(message = "El estado de la venta es obligatorio")
    private String estado = "PENDIENTE"; // "PENDIENTE", "PAGADA", "RECHAZADA"
}

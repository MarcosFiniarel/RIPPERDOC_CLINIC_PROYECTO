package com.csus.cyber_surgery_service.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "instalacion")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Instalacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID de la venta es obligatorio")
    private Long ventaId; // Referencia lógica al cyber-sale-service

    @NotNull(message = "El ID del paciente es obligatorio")
    private Long pacienteId; // Referencia lógica al cyber-patient-service

    @NotNull(message = "El ID del ciberware es obligatorio")
    private Long ciberwareId; // Referencia lógica al cyber-ciberware-service

    @NotNull(message = "El impacto de humanidad es obligatorio")
    @Min(value = 0, message = "El impacto no puede ser negativo")
    private Integer impactoHumanidad; // Se trae como DTO del cyber-ciberware-service
}

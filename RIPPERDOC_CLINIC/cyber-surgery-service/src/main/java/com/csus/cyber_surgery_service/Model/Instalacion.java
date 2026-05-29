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

    @NotBlank(message = "El estado de la cirugía es obligatorio")
    private String estadoCirugia = "EXITOSA"; // "EXITOSA", "RECHAZO_DE_TEJIDO"
}

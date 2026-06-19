package com.cfs.cyber_finance_service.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "billetera")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo conceptual que representa la cuenta financiera (billetera de Eddies) de un paciente")
public class Billetera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único incremental de la billetera", example = "1")
    private Long id;

    @NotNull(message = "El ID del paciente es obligatorio")
    @Column(unique = true) // Un paciente solo tiene una billetera en la clínica
    @Schema(description = "Referencia lógica al ID del paciente dueño de la cuenta", example = "1")
    private Long pacienteId; // Referencia lógica al cyber-patient-service

    @NotNull(message = "El saldo no puede ser nulo")
    @Min(value = 0, message = "No puedes tener saldo negativo")
    @Schema(description = "Saldo disponible en Eurodólares (Eddies)", example = "500000.0")
    private Double saldoEddies = 0.0;
}

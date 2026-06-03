package com.cfs.cyber_finance_service.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "billetera")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Billetera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del paciente es obligatorio")
    @Column(unique = true) // Un paciente solo tiene una billetera en la clínica
    private Long pacienteId; // Referencia lógica al cyber-patient-service

    @NotNull(message = "El saldo no puede ser nulo")
    @Min(value = 0, message = "No puedes tener saldo negativo")
    private Double saldoEddies = 0.0;
}

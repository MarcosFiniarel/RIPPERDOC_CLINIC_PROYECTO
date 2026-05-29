package com.cps.cyber_patient_service.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "paciente")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El alias del paciente es obligatorio")
    @Size(max = 50, message = "El alias no puede superar los 50 caracteres")
    private String alias;

    @NotNull(message = "El nivel de cyberpsicosis no puede ser nulo")
    @Min(value = 0, message = "El nivel mínimo es 0%")
    private Integer nivelCyberpsicosis = 0;

    @NotBlank(message = "El estado de salud mental es obligatorio")
    private String estado = "ESTABLE"; // Valores lógicos: "ESTABLE", "CIBERPSICOPATA"
}
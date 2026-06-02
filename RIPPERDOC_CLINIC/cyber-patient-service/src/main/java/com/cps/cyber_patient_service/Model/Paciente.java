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
    @Column(unique = true)
    private String alias;

    @NotNull(message = "El nivel de cyberpsicosis no puede ser nulo")
    @Min(value = 0, message = "El nivel mínimo es 0%")
    private Integer nivelCyberpsicosis = 0;

    // ATRIBUTOS PARA COMPATIBILIDAD

    @NotBlank(message = "La altura no puede estar en blanco.")
    private String altura; // ALTO, MEDIO, BAJO

    @NotBlank(message = "La densidad osea no puede estar en blanco.")
    private String densidadOsea; // ALTA, MEDIA, BAJA

    @NotBlank(message = "La densidad muscular no puede estar en blanco.")
    private String densidadMuscular; // ALTA, MEDIA, BAJA

    @NotNull(message = "La peso no puede estar en blanco.")
    private Double peso;

    @NotNull(message = "La edad no puede estar en blanco.")
    private Integer edad;
}
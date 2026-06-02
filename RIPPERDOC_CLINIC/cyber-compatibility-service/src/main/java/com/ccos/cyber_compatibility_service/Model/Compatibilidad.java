package com.ccos.cyber_compatibility_service.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "compatibilidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compatibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El id de la cirugia no puede ser nulo")
    private Long idCirugia;

    @NotNull(message = "El estado de compatibilidad no puede estar vacio")
    private boolean estado;
}

package com.ccs.cyber_ciberware_service.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "ciberware")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Ciberware {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del ciberware es obligatorio")
    private String nombre;

    @NotNull(message = "El costo en Eddies es obligatorio")
    @Positive(message = "El costo debe ser un valor positivo")
    private Double costoEddies;

    @NotNull(message = "El costo de humanidad es obligatorio")
    @Min(value = 0, message = "El costo de humanidad no puede ser negativo")
    private Integer costoHumanidad;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
}

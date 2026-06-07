    package com.cfs.cyber_finance_service.Model;

    import jakarta.persistence.*;
    import jakarta.validation.constraints.*;
    import lombok.*;

    @Entity
    @Table(name = "transaccion")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor

    public class Transaccion {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotNull(message = "El ID de la billetera es obligatorio")
        private Long billeteraId; // Relación interna con la Billetera

        @NotNull(message = "El ID de la venta es obligatorio")
        private Long ventaId; // Referencia lógica al cyber-sale-service

        @NotNull(message = "El monto es obligatorio")
        @Positive(message = "El monto debe ser positivo")
        private Double monto; // Se trae como DTO del cyber-sale-service

        private Boolean estado; //TRUE = AUTORIZADA / FALSE = RECHAZADA
    }

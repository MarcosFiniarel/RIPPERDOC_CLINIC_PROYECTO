package com.cfs.cyber_finance_service.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "transaccion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo conceptual que representa el comprobante o auditoría de una transacción financiera de cobro")
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador de auditoría único de la transacción", example = "1")
    private Long id;

    @NotNull(message = "El ID de la billetera es obligatorio")
    @Schema(description = "ID de la billetera afectada por la operación", example = "1")
    private Long billeteraId; // Relación interna con la Billetera

    @NotNull(message = "El ID de la venta es obligatorio")
    @Schema(description = "Referencia lógica al ID de la orden generada en cyber-sale-service", example = "105")
    private Long ventaId; // Referencia lógica al cyber-sale-service

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser positivo")
    @Schema(description = "Monto total facturado por el implante", example = "120000.0")
    private Double monto; // Se trae como DTO del cyber-sale-service

    @Schema(description = "Estado de autorización de la red (true = APROBADA / false = RECHAZADA por fondos insuficientes)", example = "true")
    private Boolean estado; //TRUE = AUTORIZADA / FALSE = RECHAZADA
}

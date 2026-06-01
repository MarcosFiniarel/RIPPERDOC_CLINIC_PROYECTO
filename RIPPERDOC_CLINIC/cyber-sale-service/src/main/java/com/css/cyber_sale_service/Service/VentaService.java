package com.css.cyber_sale_service.Service;

import com.css.cyber_sale_service.DTO.CirugiaRequestDto;
import com.css.cyber_sale_service.WebClient.CiberwareClient;
import com.css.cyber_sale_service.WebClient.PacienteClient;
import com.css.cyber_sale_service.WebClient.InstalacionClient;
import com.css.cyber_sale_service.DTO.CiberwareContratoDto;
import com.css.cyber_sale_service.DTO.PacienteContratoDto;
import com.css.cyber_sale_service.DTO.RequestDto;
import com.css.cyber_sale_service.Model.Venta;
import com.css.cyber_sale_service.Repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VentaService {

    private final VentaRepository ventaRepository;
    private final CiberwareClient ciberwareClient;
    private final PacienteClient pacienteClient;
    private final InstalacionClient instalacionClient;
    // private final TransaccionClient transaccionClient; // Se inyectará en esta sección al conectar con la Wallet

    // 1. LISTAR TODAS LAS VENTAS
    @Transactional(readOnly = true)
    public List<Venta> obtenerTodas() {
        log.info("Buscando historial completo de ventas en la base de datos.");
        return ventaRepository.findAll();
    }

    // 2. LISTAR POR CLIENTE (PACIENTE)
    @Transactional(readOnly = true)
    public List<Venta> obtenerPorPaciente(Long pacienteId) {
        log.info("Filtrando historial de ventas para el Paciente ID: {}", pacienteId);
        return ventaRepository.findByPacienteId(pacienteId);
    }

    // 3. LISTAR POR CIBERWARE
    @Transactional(readOnly = true)
    public List<Venta> obtenerPorCiberware(Long ciberwareId) {
        log.info("Filtrando historial de ventas para el Ciberware ID: {}", ciberwareId);
        return ventaRepository.findByCiberwareId(ciberwareId);
    }

    // 4. BUSCAR VENTA POR ID
    @Transactional(readOnly = true)
    public Venta obtenerPorId(Long id) {
        log.info("Buscando ticket de venta con ID: {}", id);
        return ventaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "El ticket de venta Nro " + id + " no existe."));
    }

    /**
     * 5. CREAR LA VENTA
     * Recibe los IDs de Postman, valida contra Paciente y Ciberware en tiempo real,
     * guarda la orden previa y dispara el flujo de validación financiera local inmutable.
     */
    @Transactional
    public Venta crearVenta(RequestDto request) {
        log.info("=== Iniciando orquestación de venta con validación financiera inmediata ===");

        // PASO A: Validar Paciente y traer DTO (Viaje único de red para optimizar recursos)
        PacienteContratoDto paciente = pacienteClient.verificarYObtenerPaciente(request.getIdPaciente());

        // PASO B: Validar existencia, disponibilidad de stock y traer DTO del ciberware anidado
        CiberwareContratoDto ciberware = ciberwareClient.obtenerContratoSiEstaDisponible(request.getIdCiberware());

        // PASO C: Construir entidad base histórica en memoria (Nace en PENDIENTE por seguridad)
        Venta venta = new Venta();
        venta.setPacienteId(paciente.getId());
        venta.setAliasPaciente(paciente.getAlias());
        venta.setCiberwareId(ciberware.getId());
        venta.setNombreCiberware(ciberware.getNombre());
        venta.setPrecioCobrado(ciberware.getCostoEddies());
        venta.setImpactoHumanidad(ciberware.getCostoHumanidad());
        venta.setEstado("PENDIENTE");

        // Salva instantánea inicial en BD para asegurar el ID de auditoría antes de cobrar
        Venta ventaGuardada = ventaRepository.save(venta);
        log.info("Ticket de venta generado en pre-salva: ID {} | Estado: {}", ventaGuardada.getId(), ventaGuardada.getEstado());

        // PASO D: CONSULTAR AL MÓDULO FINANCIERO (Manejo mediante booleano definitivo)
        log.info("[RED -> FINANZAS] Solicitando cobro inmediato de {} Eddies a la Wallet del paciente ID: {}",
                ventaGuardada.getPrecioCobrado(), ventaGuardada.getPacienteId());

        // En este punto simulamos el éxito con un 'true' directo.
        // Integración futura: estadoTransaccion = transaccionClient.procesarCobro( request... );
        boolean estadoTransaccion = true;

        // PASO E: Derivar el control del flujo al evaluador de estados local
        return procesarResultadoPago(ventaGuardada, estadoTransaccion);
    }

    // 6. CAMBIAR EL ESTADO DE LA VENTA (EVALUADOR LOCAL Y CONTROL DE INVENTARIO)
    private Venta procesarResultadoPago(Venta venta, boolean estadoPago) {
        if (estadoPago) {
            log.info("¡Billetera validada con éxito! Cobro confirmado. Cambiando estado de Venta ID: {} a PAGADA", venta.getId());
            venta.setEstado("PAGADA");

            log.info("[RED -> CATÁLOGO] Conexión aprobada. Ordenando deducción automática de stock para ciberware ID: {}", venta.getCiberwareId());
            ciberwareClient.deducirStockCiberware(venta.getCiberwareId());

            // 1. GUARDAMOS PRIMERO EN LA BASE DE DATOS Y ASEGURAMOS EL REGISTRO
            // Usamos saveAndFlush para obligar a Hibernate a escribir en el disco YA, antes de seguir con la red
            Venta ventaConfirmada = ventaRepository.saveAndFlush(venta);
            log.info("Venta ID: {} guardada y confirmada en la base de datos local.", ventaConfirmada.getId());

            // 2. DISPARAMOS EL FLAG DE LA CIRUGÍA (Ahora con la seguridad de que la venta no se va a perder)
            log.info("--- [FLAG CIRUGÍA] Disparando creación automática de orden de quirófano para Paciente ID: {} e Implante ID: {} ---",
                    ventaConfirmada.getPacienteId(), ventaConfirmada.getCiberwareId());

            CirugiaRequestDto peticionQuirofano = new CirugiaRequestDto(
                    ventaConfirmada.getId(),
                    ventaConfirmada.getPacienteId(),
                    ventaConfirmada.getCiberwareId(),
                    ventaConfirmada.getImpactoHumanidad()
            );

            // Se ejecuta la llamada por red síncrona al puerto 8085
            instalacionClient.solicitarCirugiaInmediata(peticionQuirofano);

            // 3. RETORNAMOS EL OBJETO RESPALDADO
            return ventaConfirmada;
        }

        // Caso RECHAZADA: No entra al IF
        log.warn("Fondos insuficientes o fallo de red en Wallet. Cambiando estado de Venta ID: {} a RECHAZADA", venta.getId());
        venta.setEstado("RECHAZADA");

        // Esto asegura el registro en la BD antes de que la excepción dispare el Rollback.
        ventaRepository.saveAndFlush(venta);

        // Lanzamos el throw.
        throw new ResponseStatusException(
                HttpStatus.PAYMENT_REQUIRED,
                "Transacción abortada: Saldo insuficiente.");
    }

    // 7. ELIMINAR UNA VENTA
    @Transactional
    public void eliminarVenta(Long id) {
        log.warn("ALERTA CRÍTICA: Solicitud de eliminación permanente para el ticket de venta ID: {}", id);
        Venta venta = obtenerPorId(id);
        ventaRepository.delete(venta);
        log.info("Ticket de venta ID: {} purgado de los servidores históricos exitosamente.", id);
    }
}

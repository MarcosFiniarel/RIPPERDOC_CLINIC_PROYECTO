package com.cfs.cyber_finance_service.Services;

import com.cfs.cyber_finance_service.Model.Billetera;
import com.cfs.cyber_finance_service.Model.Transaccion;
import com.cfs.cyber_finance_service.Repository.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransaccionService {
    private final TransaccionRepository transaccionRepository;
    private final BilleteraService billeteraService;

    //CREAR TRANSACCION
    @Transactional
    public boolean crear (Long idVenta, Long idPaciente, Double costoEddies){

        log.info("=== Procesando pago para Venta ID: {}, Paciente ID: {} ===", idVenta, idPaciente);

        Billetera billetera = billeteraService.obtenerPorPacienteId(idPaciente);

        // 1. Intentamos hacer el cobro en la billetera
        boolean cobroExitoso = billeteraService.restarSaldo(billetera.getId(), costoEddies);

        // 2. Preparamos la transacción para el histórico (Se guarda SÍ O SÍ)
        Transaccion transaccion = new Transaccion();
        transaccion.setBilleteraId(billetera.getId());
        transaccion.setVentaId(idVenta);
        transaccion.setMonto(costoEddies);
        transaccion.setEstado(cobroExitoso); // Será true o false según el resultado del cobro

        transaccionRepository.save(transaccion);
        log.info("Histórico registrado. Estado de la transacción {}: {}", transaccion.getId(), cobroExitoso);

        // 3. Le respondemos a Ventas lo que quería saber
        return cobroExitoso;
    }

    //OBTENER TODAS LAS TRANSACCIONES
    @Transactional(readOnly = true)
    public List<Transaccion> obtenerTodas(){
        return transaccionRepository.findAll();
    }

    //OBTENER TRANSACCIONES POR BILLETERA
    @Transactional(readOnly = true)
    public List<Transaccion> obtenerPorBilletera(Long billeteraId){
        return transaccionRepository.findByBilleteraId(billeteraId);
    }
}

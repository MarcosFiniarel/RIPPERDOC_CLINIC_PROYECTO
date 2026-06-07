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
        public Transaccion crear (Transaccion transaccion){
            return transaccionRepository.save(transaccion);
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
    //VALIDAR Y PROCESAR TRANSACCIONES
    @Transactional
    public boolean procesarTransaccion(Transaccion transaccion){

        Billetera billetera = billeteraService.obtenerPorId(
                transaccion.getBilleteraId()
        );

        // VALIDAR SI EL MONTO EXCEDE EL SALDO
        if(transaccion.getMonto() > billetera.getSaldoEddies()){

            transaccion.setEstado(false);

            transaccionRepository.save(transaccion);

            log.warn("Monto a pagar excede saldo disponible");

            return false;
        }

        // TRANSACCION APROBADA
        transaccion.setEstado(true);

        billeteraService.restarSaldo(
                billetera.getId(),
                transaccion.getMonto()
        );

        transaccionRepository.save(transaccion);

        log.info("Transaccion realizada correctamente");

        return true;
    }
}

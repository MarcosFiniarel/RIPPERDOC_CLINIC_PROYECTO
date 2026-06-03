package com.cfs.cyber_finance_service.Services;

import com.cfs.cyber_finance_service.Model.Billetera;
import com.cfs.cyber_finance_service.Repository.BilleteraRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class BilleteraService {
    private final BilleteraRepository  billeteraRepository;

    // CREAR UNA NUEVA BILLETERA PARA UN PACIENTE
    @Transactional
    public Billetera crear (Long id){
        log.info("Creando billetera para paciente {}", id);
        Billetera billetera = new Billetera();
        billetera.setPacienteId(id);

    return billeteraRepository.save(billetera);
    }

    // CONSULTAR TODAS LAS BILLETERAS REGISTRADAS
    @Transactional(readOnly = true)
    public List<Billetera> obtenerTodas(){
        log.info("Obteniendo todas las billeteras");

    return billeteraRepository.findAll();
    }
    // CONSULTAR BILLETERA POR ID
    @Transactional(readOnly = true)
    public Billetera obtenerPorId(Long id){
        log.info("Buscando la billetera {}", id);
    return billeteraRepository.findById(id)
            .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Billetera no encontrada"));
    }

    // CONSULTAR BILLETERA POR ID DE PACIENTE
    @Transactional(readOnly = true)
    public Billetera obtenerPorPacienteId(Long pacienteId){
        log.info("Buscando billetera del paciente {}", pacienteId);

    return billeteraRepository.findByPacienteId(pacienteId)
            .orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Billetera no encontrada"));
    }
    //SUMAR SALDO A UNA BILLETERA (EN EDDIES)
    @Transactional
    public Billetera sumarSaldo(Long id, Double monto){
        Billetera billetera = obtenerPorId(id);

        billetera.setSaldoEddies(
                billetera.getSaldoEddies() + monto
        );
        log.info("Saldo aumentado en {} Eddies",monto);

        return billeteraRepository.save(billetera);
    }

    // RESTAR SALDO DE UNA BILLETERA
    // DESCUENTA SIEMPRE QUE HAYA SALDO SUFICIENTE
    // NUNCA PERMITE QUE EL SALDO QUEDE EN NEGATIVO
    @Transactional
    public Billetera restarSaldo(Long id, double monto){
        Billetera billetera = obtenerPorId(id);
        if(monto > billetera.getSaldoEddies()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Saldo Insuficiente");
        };
        billetera.setSaldoEddies(
                billetera.getSaldoEddies() - monto
        );
        log.info("Saldo descontado: {} Eddies", monto);

        return billeteraRepository.save(billetera);
    }

    //ELIMINAR BILLETERA POR ID
    @Transactional
    public void eliminar(Long id){
        Billetera billetera = obtenerPorId(id);

        billeteraRepository.delete(billetera);

        log.info("Eliminando la billetera {}", id);
    }
}

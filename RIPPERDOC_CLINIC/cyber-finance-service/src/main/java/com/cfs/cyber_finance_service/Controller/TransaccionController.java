package com.cfs.cyber_finance_service.Controller;

import com.cfs.cyber_finance_service.Model.Transaccion;
import com.cfs.cyber_finance_service.Services.TransaccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transacciones")

public class TransaccionController {
    private final TransaccionService transaccionService;

    //CREAR TRANSACCION
    @PostMapping
    public Transaccion crear(@RequestBody Transaccion transaccion){
        return transaccionService.crear(transaccion);
    }
    //OBTENER TODAS LAS TRANSACCIONES
    @GetMapping
    public List<Transaccion> obtenerTodas(){
        return transaccionService.obtenerTodas();
    }
    //OBTENER TRANSACCIONES POR BILLETERA
    @GetMapping("/billetera/{billeteraId}")
    public List<Transaccion> obtenerPorBilletera(
            @PathVariable Long billeteraId){
                return transaccionService.obtenerPorBilletera(billeteraId);
    }

    //VALIDAR Y PROCESAR TRANSACCION
    @PostMapping("/procesar")
    public boolean procesarTransaccion(
            @RequestBody Transaccion transaccion){
                return transaccionService.procesarTransaccion(transaccion);
    }
}

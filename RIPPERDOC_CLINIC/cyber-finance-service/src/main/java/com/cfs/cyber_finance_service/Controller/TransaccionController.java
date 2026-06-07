package com.cfs.cyber_finance_service.Controller;

import com.cfs.cyber_finance_service.Model.Transaccion;
import com.cfs.cyber_finance_service.Services.TransaccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transacciones")

public class TransaccionController {
    private final TransaccionService transaccionService;

    //CREAR TRANSACCION
    @PostMapping
    public boolean crear(@RequestParam Long idVenta, @RequestParam Long idPaciente, @RequestParam Double monto){
        return transaccionService.crear(idVenta,idPaciente,monto);
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
}

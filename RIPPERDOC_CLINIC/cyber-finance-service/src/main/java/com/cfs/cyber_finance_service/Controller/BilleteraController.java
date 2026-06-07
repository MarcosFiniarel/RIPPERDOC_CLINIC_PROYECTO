package com.cfs.cyber_finance_service.Controller;

import com.cfs.cyber_finance_service.Model.Billetera;
import com.cfs.cyber_finance_service.Services.BilleteraService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/billeteras")
@RequiredArgsConstructor
public class BilleteraController {
    private final BilleteraService billeteraService;

    // CREAR BILLETERA
    @PostMapping
    public Billetera crear(@RequestParam Long id) {
        return billeteraService.crear(id);
    }


    // OBTENER TODAS LAS BILLETERAS
    @GetMapping
    public List<Billetera> obtenerTodas() {
        return billeteraService.obtenerTodas();
    }

    // OBTENER BILLETERA POR ID
    @GetMapping("/{id}")
    public Billetera obtenerPorId(@PathVariable("id") Long id) {
        return billeteraService.obtenerPorId(id);
    }

    // OBTENER BILLETERA POR ID DE PACIENTE
    @GetMapping("/paciente/{pacienteId}")
    public Billetera obtenerPorPacienteId(@PathVariable("pacienteId") Long pacienteId) {
        return billeteraService.obtenerPorPacienteId(pacienteId);
    }

    // SUMAR SALDO A BILLETERA (EN EDDIES)
    @PutMapping("/{id}/sumar")
    public Billetera sumarSaldo(
            @PathVariable Long id,
            @RequestParam Double monto) {
        return billeteraService.sumarSaldo(id, monto);
    }
    // RESTAR SALDO A BILLETERA(EN EDDIES)
    @PutMapping("/{id}/restar")
    public Boolean restarSaldo(
            @PathVariable Long id,
            @RequestParam Double monto) {
        return billeteraService.restarSaldo(id, monto);
    }

    // ELIMINAR BILLETERA
    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {

        billeteraService.eliminar(id);

        return "Billetera eliminada correctamente";
    }
}


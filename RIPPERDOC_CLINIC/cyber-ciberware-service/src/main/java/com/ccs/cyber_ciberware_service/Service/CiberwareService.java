package com.ccs.cyber_ciberware_service.Service;

import com.ccs.cyber_ciberware_service.Model.Ciberware;
import com.ccs.cyber_ciberware_service.Repository.CiberwareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CiberwareService {
    private final CiberwareRepository ciberwareRepository;

    // 1. OBTENER TODOS los ciberwares
    @Transactional(readOnly = true)
    public List<Ciberware> obtenerTodos() {
        return ciberwareRepository.findAll();
    }

    // 2. OBTENER por ID: Servirá para que mapear el Dto que se enviara a ventas
    @Transactional(readOnly = true)
    public Ciberware obtenerPorId(Long id) {
        return ciberwareRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "El ciberware con ID " + id + " no existe en el catálogo."));
    }

    // 3. GUARDAR nuevo registro base
    @Transactional
    public Ciberware guardar(Ciberware ciberware) {
        // Escudo anti-duplicados: interceptamos antes de tocar la base de datos
        if (ciberwareRepository.existsByNombre(ciberware.getNombre())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El ciberware '" + ciberware.getNombre() + "' ya está registrado en el catálogo. No uses nombres redundantes.");
        }
        return ciberwareRepository.save(ciberware);
    }

    // VALIDAR DISPONIBILIDAD antes de iniciar una venta
    @Transactional(readOnly = true)
    public boolean verificarDisponibilidad(Long id) {
        Ciberware ciberware = obtenerPorId(id); // Si no existe se triggerea la excepcion del buscador por id

        if (ciberware.getStock() < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El ciberware '" + ciberware.getNombre() + "' está agotado.");
        }
        return true; // Está disponible
    }

    // DEDUCIR STOCK (Se ejecuta tras confirmar el pago)
    @Transactional
    public Ciberware deducirStock(Long id) {
        // Usamos la validación interna que acabamos de crear para asegurar consistencia
        verificarDisponibilidad(id);

        Ciberware ciberware = obtenerPorId(id);
        ciberware.setStock(ciberware.getStock() - 1);
        return ciberwareRepository.save(ciberware);
    }

    // ACTUALIZACION MANUAL DE STOCK (Reabastecimiento o Ajuste de Inventario)
    @Transactional
    public Ciberware actualizarStockManual(Long id, int modificador) {
        Ciberware ciberware = obtenerPorId(id);

        // Calculamos cómo quedaría el stock final
        int stockFinal = ciberware.getStock() + modificador;

        // Escudo: No podemos tener existencias negativas en la vitrina
        if (stockFinal < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Operación rechazada. No puedes restar " + Math.abs(modificador) + " unidades porque el stock actual de '" + ciberware.getNombre() + "' es de " + ciberware.getStock());
        }

        // Aplicamos el nuevo stock calculado
        ciberware.setStock(stockFinal);
        return ciberwareRepository.save(ciberware);
    }

    // ELIMINAR un registro base
    @Transactional
    public void eliminar(Long id) {
        Ciberware ciberware = obtenerPorId(id);
        ciberwareRepository.delete(ciberware);
    }
}

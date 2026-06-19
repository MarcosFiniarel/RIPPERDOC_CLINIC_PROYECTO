package com.ccs.cyber_ciberware_service.Controller;

import com.ccs.cyber_ciberware_service.DTO.CiberwareCompatibilidadDto;
import com.ccs.cyber_ciberware_service.DTO.CiberwareContratoDto;
import com.ccs.cyber_ciberware_service.Model.Ciberware;
import com.ccs.cyber_ciberware_service.Service.CiberwareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Habilita el framework para hacer logs
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ciberware")
@RequiredArgsConstructor
@Slf4j // Inyecta automáticamente el objeto 'log' de Lombok para auditoría y trazas en consola
@Tag(name = "1. Catálogo de Ciberware", description = "Endpoints de control de inventario, contratos y compatibilidad física de implantes")
public class CiberwareController {

    private final CiberwareService ciberwareService;

    /**
     * 1. LISTA DE CIBERWARE
     * Metodo HTTP: GET
     * Endpoint: http://localhost:8082/api/v1/ciberware
     */
    @GetMapping
    @Operation(summary = "Listar catálogo completo", description = "Devuelve una lista con todas las piezas de cromo registradas en la base de datos.")
    @ApiResponse(responseCode = "200", description = "Catálogo obtenido con éxito")
    public ResponseEntity<List<Ciberware>> listarTodos() {
        log.info("=== Solicitud de catálogo completo recibida ===");
        List<Ciberware> lista = ciberwareService.obtenerTodos();

        // El símbolo {} es un comodín (placeholder) dinámico que reemplaza su valor por el tamaño de la lista
        log.info("Catálogo enviado con éxito. Total de ítems: {}", lista.size());
        return ResponseEntity.ok(lista);
    }

    /**
     * 2. BUSCAR UN IMPLANTE ESPECÍFICO POR ID
     * Metodo HTTP: GET
     * Endpoint: http://localhost:8082/api/v1/ciberware/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar un implante por ID", description = "Localiza una pieza específica utilizando su clave única de la red.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ciberware localizado en vitrina"),
            @ApiResponse(responseCode = "404", description = "Implante no hallado en los servidores de la clínica o inexistente", content = @Content)
    })
    public ResponseEntity<Ciberware> buscarPorId(@PathVariable @Parameter(description = "ID único del implante a consultar", example = "1") Long id) {
        log.info("Buscando ciberware con ID: {}", id);
        Ciberware ciberware = ciberwareService.obtenerPorId(id);
        log.info("Ciberware encontrado: {}", ciberware.getNombre());
        return ResponseEntity.ok(ciberware);
    }

    /**
     * 3. REGISTRAR UNA NUEVA PIEZA (CREAR)
     * Metodo HTTP: POST
     * Endpoint: http://localhost:8082/api/v1/ciberware
     */
    @PostMapping
    @Operation(summary = "Registrar un nuevo implante", description = "Agrega un nuevo modelo de ciberware al catálogo con sus filtros de compatibilidad biológica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "¡Ciberware añadido con éxito a la vitrina!"),
            @ApiResponse(responseCode = "400", description = "Error de validación en la estructura del JSON enviado", content = @Content)
    })
    public ResponseEntity<Ciberware> crearCiberware(@Valid @RequestBody Ciberware ciberware) {
        log.info("Registrando nuevo ciberware en vitrina: {}", ciberware.getNombre());
        Ciberware guardado = ciberwareService.guardar(ciberware);
        log.info("¡CIBERWARE REGISTRADO CON ÉXITO! Asignado ID: {} | Stock inicial: {}", guardado.getId(), guardado.getStock());

        // Retornamos un estado 201 Created por buenas prácticas de diseño arquitectónico REST
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    /**
     * 4. VERIFICAR DISPONIBILIDAD TÉCNICA
     * Metodo HTTP: GET
     * Endpoint: http://localhost:8082/api/v1/ciberware/{id}/disponible
     * Retorna 'true' si el implante existe y su stock es mayor o igual a 1.
     */
    @GetMapping("/{id}/disponible")
    @Operation(summary = "Verificar disponibilidad de stock", description = "Retorna un valor booleano indicando si el implante cuenta con unidades físicas listas para cirugía.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chequeo de stock realizado con éxito"),
            @ApiResponse(responseCode = "404", description = "ID de ciberware no registrado", content = @Content)
    })
    public ResponseEntity<Boolean> chequearDisponibilidad(@PathVariable @Parameter(description = "ID del implante a evaluar", example = "1") Long id) {
        log.info("Verificando disponibilidad técnica para el ciberware ID: {}", id);
        boolean disponible = ciberwareService.verificarDisponibilidad(id);
        log.info("Verificación completada. ¿Disponible para la venta?: {}", disponible);
        return ResponseEntity.ok(disponible);
    }

    /**
     * 5. DEDUCIR STOCK AUTOMÁTICAMENTE EN 1 UNIDAD
     * Metodo HTTP: PATCH (Usado porque modificamos de forma parcial un solo atributo: el stock)
     * Endpoint: http://localhost:8082/api/v1/ciberware/{id}/deducir
     * Nota: Este endpoint está pensado para ser disparado internamente tras la confirmación de una venta exitosa.
     */
    @PatchMapping("/{id}/deducir")
    @Operation(summary = "Deducir una unidad de stock", description = "Reduce automáticamente en 1 la cantidad del inventario. Diseñado para automatización post-venta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "El implante no posee suficiente stock (Unidades = 0)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Implante no encontrado", content = @Content)
    })
    public ResponseEntity<Ciberware> deducirInventario(@PathVariable @Parameter(description = "ID de la pieza vendida", example = "1") Long id) {
        log.info("Iniciando deducción automática de stock para ciberware ID: {}", id);
        Ciberware actualizado = ciberwareService.deducirStock(id);
        log.info("Stock reducido con éxito. Nuevo stock para '{}': {}", actualizado.getNombre(), actualizado.getStock());
        return ResponseEntity.ok(actualizado);
    }

    /**
     * 6. AJUSTE DINÁMICO MANUAL DE STOCK (Reabastecimiento o Pérdidas)
     * Metodo HTTP: PUT
     * Endpoint: http://localhost:8082/api/v1/ciberware/{id}/stock-manual?cantidad=VALUE
     * @RequestParam: Recibe el modificador por URL. Soporta positivos (ej: 5 para sumar) y negativos (ej: -2 para restar).
     */
    @PutMapping("/{id}/stock-manual")
    @Operation(summary = "Ajuste manual de inventario", description = "Modifica manualmente el stock sumando o restando una cantidad específica desde la URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ajuste de inventario aplicado con éxito"),
            @ApiResponse(responseCode = "404", description = "Implante no encontrado", content = @Content)
    })
    public ResponseEntity<Ciberware> ajustarStockManual(
            @PathVariable @Parameter(description = "ID de la pieza a reabastecer", example = "1") Long id,
            @RequestParam @Parameter(description = "Cantidad a modificar (Valores positivos suman, negativos restan)", example = "10") int cantidad) {
        log.info("Solicitud de ajuste manual de inventario para ID: {}. Modificador: {}", id, cantidad);
        Ciberware actualizado = ciberwareService.actualizarStockManual(id, cantidad);
        log.info("Ajuste manual aplicado. Stock final de '{}': {}", actualizado.getNombre(), actualizado.getStock());
        return ResponseEntity.ok(actualizado);
    }

    /**
     * 7. ELIMINAR UN IMPLANTE DEL CATÁLOGO
     * Metodo HTTP: DELETE
     * Endpoint: http://localhost:8082/api/v1/ciberware/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Dar de baja un ciberware", description = "Elimina de forma permanente una pieza de la base de datos de catálogo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Implante eliminado sin dejar rastro"),
            @ApiResponse(responseCode = "404", description = "No se pudo eliminar: ID no existente", content = @Content)
    })
    public ResponseEntity<Void> eliminarCiberware(@PathVariable @Parameter(description = "ID de la pieza a purgar", example = "1") Long id) {
        // Usamos log.warn para alertar en consola que se está ejecutando una acción destructiva
        log.warn("ALERTA: Solicitud de eliminación para el ciberware ID: {}", id);
        ciberwareService.eliminar(id);
        log.info("Ciberware ID: {} eliminado permanentemente del catálogo", id);

        // Retornamos un 204 No Content porque el recurso ya no existe y no hay cuerpo que devolver
        return ResponseEntity.noContent().build();
    }

    // ENDPOINT DE INTEGRACIÓN: CONTRATO GLOBAL PARA EL FLUJO DE VENTAS
    /**
     * 8. EMITIR CONTRATO GLOBAL DE COMPRA
     * Metodo HTTP: GET
     * Endpoint: http://localhost:8082/api/v1/ciberware/{id}/contrato
     * Retorna un DTO optimizado (ID, Nombre, Costo Eddies, Costo Humanidad).
     * Evita que otros microservicios (como Finanzas) tengan que llamar redundantemente al Catalogo.
     */
    @GetMapping("/{id}/contrato")
    @Operation(summary = "Emitir Contrato de Venta (DTO)", description = "Genera un payload optimizado con los datos estrictos de cobro financiero y costo cerebral.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contrato de red generado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Implante no existente", content = @Content)
    })
    public ResponseEntity<CiberwareContratoDto> obtenerContratoParaVenta(@PathVariable @Parameter(description = "ID del ciberware para el contrato", example = "1") Long id) {
        log.info("Emitiendo Contrato Global de compra para ciberware ID: {}", id);
        Ciberware ciberware = ciberwareService.obtenerPorId(id);

        // Se empaquetan los datos de la entidad de la BD hacia el DTO de transferencia de red
        CiberwareContratoDto contrato = new CiberwareContratoDto(
                ciberware.getId(),
                ciberware.getNombre(),
                ciberware.getCostoEddies(),
                ciberware.getCostoHumanidad()
        );

        log.info("Contrato Global generado con éxito para '{}'. Enviando a Ventas...", contrato.getNombre());
        return ResponseEntity.ok(contrato);
    }

    /**
     * 9. EMITIR DATOS DE COMPATIBILIDAD
     * Metodo HTTP: GET
     * Endpoint: http://localhost:8082/api/v1/ciberware/{id}/compatibilidad
     * Retorna un DTO optimizado (ALTURA, DENSIDAD OSEA, DENSIDAD MUSCULAR, PESO Y EDAD).
     * Evita que otros microservicios (como Finanzas o Humanidad) tengan que llamar redundantemente al Catalogo.
     */
    @GetMapping("/{id}/compatibilidad")
    @Operation(summary = "Emitir Datos de Compatibilidad (DTO)", description = "Expone exclusivamente las restricciones biométricas del implante para validaciones cruzadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ficha biométrica transmitida con éxito"),
            @ApiResponse(responseCode = "404", description = "Implante no existente", content = @Content)
    })
    public ResponseEntity<CiberwareCompatibilidadDto> obtenerCompatibilidad(@PathVariable @Parameter(description = "ID del ciberware a contrastar", example = "1") Long id) {
        log.info("Emitiendo datos de compatibilidad para ciberware ID: {}", id);
        Ciberware ciberware = ciberwareService.obtenerPorId(id);

        // Se empaquetan los datos de la entidad de la BD hacia el DTO de transferencia de red
        CiberwareCompatibilidadDto compatibilidad = new CiberwareCompatibilidadDto(
                ciberware.getAlturaCompatibilidad(),
                ciberware.getDensidadOseaCompatibilidad(),
                ciberware.getDensidadMuscularCompatibilidad(),
                ciberware.getPesoMinimoCompatibilidad(),
                ciberware.getPesoMaximoCompatibilidad(),
                ciberware.getEdadMinimaCompatibilidad(),
                ciberware.getEdadMaximaCompatibilidad()
        );

        log.info("Datos de compatibilidad obtenidos con éxito para '{}'. Enviando a Compatibilidad...", ciberware.getNombre());
        return ResponseEntity.ok(compatibilidad);
    }
}

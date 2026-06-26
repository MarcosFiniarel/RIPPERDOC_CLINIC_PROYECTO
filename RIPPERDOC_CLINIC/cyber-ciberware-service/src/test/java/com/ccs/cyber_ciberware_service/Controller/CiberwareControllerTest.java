package com.ccs.cyber_ciberware_service.Controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.ccs.cyber_ciberware_service.Model.Ciberware;
import com.ccs.cyber_ciberware_service.Service.CiberwareService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(CiberwareController.class)
public class CiberwareControllerTest {

    @Autowired
    private MockMvc mockMvc; // Emulador de peticiones HTTP (Postman virtual)

    @MockitoBean
    private CiberwareService ciberwareService; // Clon simulado del servicio

    // Inicialización explícita para evitar UnsatisfiedDependencyException
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Ciberware ciberware;

    @BeforeEach
    void setUp() {
        // Inicializamos el objeto base simulando un implante real de la vitrina
        ciberware = new Ciberware();
        ciberware.setId(1L);
        ciberware.setNombre("Sandevistan MK.V");
        ciberware.setCostoEddies(50000.0);
        ciberware.setCostoHumanidad(20);
        ciberware.setStock(5);

        ciberware.setAlturaCompatibilidad("ALTO");
        ciberware.setDensidadOseaCompatibilidad("MEDIA");
        ciberware.setDensidadMuscularCompatibilidad("ALTA");
        ciberware.setPesoMinimoCompatibilidad(60.0);
        ciberware.setPesoMaximoCompatibilidad(120.0);
        ciberware.setEdadMinimaCompatibilidad(18);
        ciberware.setEdadMaximaCompatibilidad(65);
    }

    // 1. TEST LISTAR TODO (GET /api/v1/ciberware)
    @Test
    public void testListarTodos() throws Exception {
        when(ciberwareService.obtenerTodos()).thenReturn(List.of(ciberware));

        mockMvc.perform(get("/api/v1/ciberware"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Sandevistan MK.V"))
                .andExpect(jsonPath("$[0].costoEddies").value(50000.0))
                .andExpect(jsonPath("$[0].costoHumanidad").value(20))
                .andExpect(jsonPath("$[0].stock").value(5));
    }

    // 2. TEST BUSCAR POR ID (GET /api/v1/ciberware/{id})
    @Test
    public void testBuscarPorId() throws Exception {
        when(ciberwareService.obtenerPorId(1L)).thenReturn(ciberware);

        mockMvc.perform(get("/api/v1/ciberware/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Sandevistan MK.V"))
                .andExpect(jsonPath("$.costoEddies").value(50000.0));
    }

    // 3. TEST REGISTRAR NUEVO IMPLANTE (POST /api/v1/ciberware)
    @Test
    public void testCrearCiberware() throws Exception {
        when(ciberwareService.guardar(any(Ciberware.class))).thenReturn(ciberware);

        mockMvc.perform(post("/api/v1/ciberware")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ciberware))) // Convierte objeto Java a String JSON
                .andExpect(status().isCreated()) // Valida respuesta 201 Created
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Sandevistan MK.V"));
    }

    // 4. TEST VERIFICAR DISPONIBILIDAD (GET /api/v1/ciberware/{id}/disponible)
    @Test
    public void testChequearDisponibilidad() throws Exception {
        when(ciberwareService.verificarDisponibilidad(1L)).thenReturn(true);

        mockMvc.perform(get("/api/v1/ciberware/1/disponible"))
                .andExpect(status().isOk())
                .andExpect(content().string("true")); // El controlador responde directamente un booleano primitivo
    }

    // 5. TEST DEDUCIR STOCK AUTOMÁTICO EN 1 UNIDAD (PATCH /api/v1/ciberware/{id}/deducir)
    @Test
    public void testDeducirInventario() throws Exception {
        // Simulamos el objeto con el stock disminuido (5 - 1 = 4)
        ciberware.setStock(4);
        when(ciberwareService.deducirStock(1L)).thenReturn(ciberware);

        mockMvc.perform(patch("/api/v1/ciberware/1/deducir"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.stock").value(4));
    }

    // 6. TEST AJUSTE DINÁMICO MANUAL DE STOCK (PUT /api/v1/ciberware/{id}/stock-manual?cantidad=VALUE)
    @Test
    public void testAjustarStockManual() throws Exception {
        // Simulamos un reabastecimiento sumando 10 unidades (5 + 10 = 15)
        ciberware.setStock(15);
        when(ciberwareService.actualizarStockManual(1L, 10)).thenReturn(ciberware);

        mockMvc.perform(put("/api/v1/ciberware/1/stock-manual")
                        .param("cantidad", "10")) // Envía el @RequestParam requerido en la URL
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(15));
    }

    // 7. TEST ELIMINAR IMPLANTE (DELETE /api/v1/ciberware/{id})
    @Test
    public void testEliminarCiberware() throws Exception {
        doNothing().when(ciberwareService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/ciberware/1"))
                .andExpect(status().isNoContent()); // Verifica respuesta 204 No Content

        // Aseguramos que el controlador ejecutó la orden en el Service exactamente 1 vez
        verify(ciberwareService, times(1)).eliminar(1L);
    }

    // 8. TEST INTEGRACIÓN: EMITIR CONTRATO DE COMPRA DTO (GET /api/v1/ciberware/{id}/contrato)
    @Test
    public void testObtenerContratoParaVenta() throws Exception {
        when(ciberwareService.obtenerPorId(1L)).thenReturn(ciberware);

        mockMvc.perform(get("/api/v1/ciberware/1/contrato"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Sandevistan MK.V"))
                .andExpect(jsonPath("$.costoEddies").value(50000.0))
                .andExpect(jsonPath("$.costoHumanidad").value(20));
    }

    // 9. TEST INTEGRACIÓN: EMITIR DATOS COMPATIBILIDAD DTO (GET /api/v1/ciberware/{id}/compatibilidad)
    @Test
    public void testObtenerCompatibilidad() throws Exception {
        when(ciberwareService.obtenerPorId(1L)).thenReturn(ciberware);

        mockMvc.perform(get("/api/v1/ciberware/1/compatibilidad"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.altura").value("ALTO")) // Espera String
                .andExpect(jsonPath("$.densidadOsea").value("MEDIA")) // Espera String
                .andExpect(jsonPath("$.densidadMuscular").value("ALTA")) // Espera String
                .andExpect(jsonPath("$.pesoMinimo").value(60.0))
                .andExpect(jsonPath("$.pesoMaximo").value(120.0))
                .andExpect(jsonPath("$.edadMinima").value(18))
                .andExpect(jsonPath("$.edadMaxima").value(65));
    }
}

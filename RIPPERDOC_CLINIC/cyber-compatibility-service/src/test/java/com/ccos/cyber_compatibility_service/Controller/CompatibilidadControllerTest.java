package com.ccos.cyber_compatibility_service.Controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.ccos.cyber_compatibility_service.Model.Compatibilidad;
import com.ccos.cyber_compatibility_service.Service.CompatibilidadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(CompatibilidadController.class)
public class CompatibilidadControllerTest {

    @Autowired
    private MockMvc mockMvc; // Emulador de peticiones HTTP en entorno aislado

    @MockitoBean
    private CompatibilidadService compatibilidadService; // Clon simulado de la lógica de negocio

    // Inicialización explícita para blindar la inyección del Jackson Mapper
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Compatibilidad registroExitoso;
    private Compatibilidad registroFallido;

    @BeforeEach
    void setUp() {
        // Mock de un escaneo biológico aprobado
        registroExitoso = new Compatibilidad();
        registroExitoso.setId(701L);
        registroExitoso.setIdCirugia(501L);
        registroExitoso.setEstado(true); // APTO

        // Mock de un escaneo biológico rechazado
        registroFallido = new Compatibilidad();
        registroFallido.setId(702L);
        registroFallido.setIdCirugia(502L);
        registroFallido.setEstado(false); // RECHAZO BIOLÓGICO
    }

    // 1. TEST LISTAR HISTÓRICO COMPLETO (GET /api/v1/compatibilidad)
    @Test
    public void testListarTodas() throws Exception {
        when(compatibilidadService.obtenerTodas()).thenReturn(List.of(registroExitoso, registroFallido));

        mockMvc.perform(get("/api/v1/compatibilidad"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(701))
                .andExpect(jsonPath("$[0].idCirugia").value(501))
                .andExpect(jsonPath("$[0].estado").value(true))
                .andExpect(jsonPath("$[1].id").value(702))
                .andExpect(jsonPath("$[1].idCirugia").value(502))
                .andExpect(jsonPath("$[1].estado").value(false));
    }

    // 2. TEST FILTRAR POR ESTADO EXITOSO (GET /api/v1/compatibilidad/filtrar?estado=true)
    @Test
    public void testListarPorEstadoExitoso() throws Exception {
        when(compatibilidadService.obtenerPorEstado(true)).thenReturn(List.of(registroExitoso));

        mockMvc.perform(get("/api/v1/compatibilidad/filtrar")
                        .param("estado", "true")) // Pasamos el parámetro por URL
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(701))
                .andExpect(jsonPath("$[0].estado").value(true));
    }

    // 2B. TEST FILTRAR POR ESTADO FALLIDO (GET /api/v1/compatibilidad/filtrar?estado=false)
    @Test
    public void testListarPorEstadoFallido() throws Exception {
        when(compatibilidadService.obtenerPorEstado(false)).thenReturn(List.of(registroFallido));

        mockMvc.perform(get("/api/v1/compatibilidad/filtrar")
                        .param("estado", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(702))
                .andExpect(jsonPath("$[0].estado").value(false));
    }

    // 3. TEST EVALUACIÓN ULTRA-SIMPLIFICADA (POST /api/v1/compatibilidad/evaluar)
    @Test
    public void testEvaluarCompatibilidadCompleta() throws Exception {
        // Simulamos que el motor interno procesó los DTOs en memoria y dio luz verde (true)
        when(compatibilidadService.evaluarYRegistrar(501L, 1L, 1L)).thenReturn(true);

        mockMvc.perform(post("/api/v1/compatibilidad/evaluar")
                        .param("idCirugia", "501")
                        .param("idPaciente", "1")
                        .param("idCiberware", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true")); // El controlador responde el booleano puro
    }

    // 4. TEST ELIMINAR REGISTRO HISTÓRICO (DELETE /api/v1/compatibilidad/{id})
    @Test
    public void testEliminarRegistro() throws Exception {
        doNothing().when(compatibilidadService).eliminar(701L);

        mockMvc.perform(delete("/api/v1/compatibilidad/701"))
                .andExpect(status().isNoContent()); // Valida código HTTP 204 No Content

        // Verificamos auditoría interna: el servicio debió ejecutarse exactamente una vez
        verify(compatibilidadService, times(1)).eliminar(701L);
    }
}

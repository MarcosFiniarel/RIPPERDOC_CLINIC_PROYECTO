package com.cms.cyber_maxtac_service.Controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.cms.cyber_maxtac_service.Model.Alerta;
import com.cms.cyber_maxtac_service.Service.AlertaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(AlertaController.class)
public class AlertaControllerTest {

    @Autowired
    private MockMvc mockMvc; // Emulador virtual de peticiones tácticas por red

    @MockitoBean
    private AlertaService alertaService; // Clon simulado del servicio de despacho MaxTac

    // Inicialización manual explícita para blindar la inyección del Jackson Mapper
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Alerta alertaActiva;
    private Alerta alertaNeutralizada;

    @BeforeEach
    void setUp() {
        // Inicializamos una alerta en estado inicial de despacho
        alertaActiva = new Alerta();
        alertaActiva.setId(901L);
        alertaActiva.setPacienteId(1L);
        alertaActiva.setAlias("David Martínez");
        alertaActiva.setNivelAmenaza("CODIGO_NEGRO");
        alertaActiva.setEstadoDespliegue("COMPAÑIA_EN_CAMINO");

        // Inicializamos otra alerta simulando un caso cerrado por contención armada
        alertaNeutralizada = new Alerta();
        alertaNeutralizada.setId(902L);
        alertaNeutralizada.setPacienteId(2L);
        alertaNeutralizada.setAlias("James Norris");
        alertaNeutralizada.setNivelAmenaza("CODIGO_NEGRO");
        alertaNeutralizada.setEstadoDespliegue("AMENAZA_NEUTRALIZADA");
    }

    // 1. GATILLAR EMERGENCIA (POST /api/v1/alertas?pacienteId=X&alias=Y)
    @Test
    public void testCrearAlerta() throws Exception {
        when(alertaService.crearAlerta(1L, "David Martínez")).thenReturn(alertaActiva);

        mockMvc.perform(post("/api/v1/alertas")
                        .param("pacienteId", "1")
                        .param("alias", "David Martínez")) // Parámetros query requeridos en URL
                .andExpect(status().isCreated()) // Valida código HTTP 201 Created
                .andExpect(jsonPath("$.id").value(901))
                .andExpect(jsonPath("$.alias").value("David Martínez"))
                .andExpect(jsonPath("$.estadoDespliegue").value("COMPAÑIA_EN_CAMINO"));
    }

    // 2. ACTUALIZAR ESTADO OPERATIVO (PATCH /api/v1/alertas/{id}/neutralizar)
    @Test
    public void testNeutralizarAmenaza() throws Exception {
        when(alertaService.neutralizarAmenaza(901L)).thenReturn(alertaNeutralizada);

        mockMvc.perform(patch("/api/v1/alertas/901/neutralizar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(902)) // Retorna el reporte actualizado
                .andExpect(jsonPath("$.estadoDespliegue").value("AMENAZA_NEUTRALIZADA"));
    }

    // 3. LISTAR TODA LA BITÁCORA TÁCTICA (GET /api/v1/alertas)
    @Test
    public void testListarTodas() throws Exception {
        when(alertaService.listarTodas()).thenReturn(List.of(alertaActiva, alertaNeutralizada));

        mockMvc.perform(get("/api/v1/alertas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(901))
                .andExpect(jsonPath("$[0].alias").value("David Martínez"))
                .andExpect(jsonPath("$[1].id").value(902))
                .andExpect(jsonPath("$[1].alias").value("James Norris"));
    }

    // 4. BUSCAR REPORTE POR SU ID ÚNICO (GET /api/v1/alertas/{id})
    @Test
    public void testObtenerPorId() throws Exception {
        when(alertaService.obtenerPorId(901L)).thenReturn(alertaActiva);

        mockMvc.perform(get("/api/v1/alertas/901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(901))
                .andExpect(jsonPath("$.alias").value("David Martínez"));
    }

    // 5. BUSCAR REPORTE POR PACIENTE ID (GET /api/v1/alertas/paciente/{pacienteId})
    @Test
    public void testObtenerPorPacienteId() throws Exception {
        when(alertaService.obtenerPorPacienteId(1L)).thenReturn(List.of(alertaActiva));

        mockMvc.perform(get("/api/v1/alertas/paciente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(901))
                .andExpect(jsonPath("$[0].pacienteId").value(1));
    }

    // 6. BUSCAR REPORTE POR ALIAS DEL SOSPECHOSO (GET /api/v1/alertas/alias/{alias})
    @Test
    public void testObtenerPorAlias() throws Exception {
        when(alertaService.obtenerPorAlias("David Martínez")).thenReturn(List.of(alertaActiva));

        mockMvc.perform(get("/api/v1/alertas/alias/David Martínez"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(901))
                .andExpect(jsonPath("$[0].alias").value("David Martínez"));
    }

    // 7. PURGAR REPORTE (DELETE /api/v1/alertas/{id})
    @Test
    public void testEliminarAlerta() throws Exception {
        doNothing().when(alertaService).eliminarAlerta(901L);

        mockMvc.perform(delete("/api/v1/alertas/901"))
                .andExpect(status().isNoContent()); // Verifica respuesta HTTP 204 No Content

        // Verificamos auditoría: confirmamos la ejecución exacta en el Service
        verify(alertaService, times(1)).eliminarAlerta(901L);
    }
}
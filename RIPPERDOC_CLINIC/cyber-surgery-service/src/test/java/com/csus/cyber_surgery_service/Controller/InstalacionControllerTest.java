package com.csus.cyber_surgery_service.Controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.csus.cyber_surgery_service.DTO.CirugiaRequestDto;
import com.csus.cyber_surgery_service.Model.Instalacion;
import com.csus.cyber_surgery_service.Service.InstalacionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(InstalacionController.class)
public class InstalacionControllerTest {

    @Autowired
    private MockMvc mockMvc; // Emulador virtual de peticiones al panel de control del quirófano

    @MockitoBean
    private InstalacionService instalacionService; // Clon simulado del servicio de implantes biológicos

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Instalacion cirugiaExitosa;
    private Instalacion cirugiaEnProceso;
    private CirugiaRequestDto requestDto;

    @BeforeEach
    void setUp() {
        // Reporte 1: Cirugía acoplada con éxito en el sistema nervioso
        cirugiaExitosa = new Instalacion();
        cirugiaExitosa.setId(501L);
        cirugiaExitosa.setVentaId(105L);
        cirugiaExitosa.setPacienteId(1L);
        cirugiaExitosa.setCiberwareId(1L);
        cirugiaExitosa.setImpactoHumanidad(88);
        cirugiaExitosa.setEstado("EXITOSA");

        // Reporte 2: Procedimiento inicial en camilla
        cirugiaEnProceso = new Instalacion();
        cirugiaEnProceso.setId(502L);
        cirugiaEnProceso.setVentaId(106L);
        cirugiaEnProceso.setPacienteId(2L);
        cirugiaEnProceso.setCiberwareId(3L);
        cirugiaEnProceso.setImpactoHumanidad(45);
        cirugiaEnProceso.setEstado("EN PROCESO");

        // DTO de entrada simulado desde el microservicio de Ventas
        requestDto = new CirugiaRequestDto();
        requestDto.setVentaId(105L);
        requestDto.setPacienteId(1L);
        requestDto.setCiberwareId(1L);
        requestDto.setImpactoHumanidad(88);
        requestDto.setEstado("EN PROCESO");
    }

    // 1. CREAR UNA CIRUGÍA (POST /api/v1/cirugias)
    @Test
    public void testRegistrarCirugia() throws Exception {
        // Simulamos que el orquestador del servicio procesa el DTO y retorna la cirugía exitosa
        when(instalacionService.crearCirugia(any(CirugiaRequestDto.class))).thenReturn(cirugiaExitosa);

        mockMvc.perform(post("/api/v1/cirugias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated()) // Valida HTTP 201 Created
                .andExpect(jsonPath("$.id").value(501))
                .andExpect(jsonPath("$.estado").value("EXITOSA"))
                .andExpect(jsonPath("$.pacienteId").value(1));
    }

    // 2. LISTAR TODAS LAS CIRUGÍAS (GET /api/v1/cirugias)
    @Test
    public void testListarTodas() throws Exception {
        when(instalacionService.obtenerTodas()).thenReturn(List.of(cirugiaExitosa, cirugiaEnProceso));

        mockMvc.perform(get("/api/v1/cirugias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(501))
                .andExpect(jsonPath("$[0].estado").value("EXITOSA"))
                .andExpect(jsonPath("$[1].id").value(502))
                .andExpect(jsonPath("$[1].estado").value("EN PROCESO"));
    }

    // 3. LISTAR CIRUGÍAS POR PACIENTE (GET /api/v1/cirugias/paciente/{pacienteId})
    @Test
    public void testListarPorPaciente() throws Exception {
        when(instalacionService.obtenerPorPaciente(1L)).thenReturn(List.of(cirugiaExitosa));

        mockMvc.perform(get("/api/v1/cirugias/paciente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(501))
                .andExpect(jsonPath("$[0].pacienteId").value(1))
                .andExpect(jsonPath("$[0].ventaId").value(105));
    }

    // 4. LISTAR CIRUGÍAS POR CIBERWARE (GET /api/v1/cirugias/ciberware/{ciberwareId})
    @Test
    public void testListarPorCiberware() throws Exception {
        when(instalacionService.obtenerPorCiberware(1L)).thenReturn(List.of(cirugiaExitosa));

        mockMvc.perform(get("/api/v1/cirugias/ciberware/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(501))
                .andExpect(jsonPath("$[0].ciberwareId").value(1));
    }

    // 5. BUSCAR POR ID DE CIRUGÍA (GET /api/v1/cirugias/{id})
    @Test
    public void testBuscarPorId() throws Exception {
        when(instalacionService.obtenerPorId(501L)).thenReturn(cirugiaExitosa);

        mockMvc.perform(get("/api/v1/cirugias/501"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(501))
                .andExpect(jsonPath("$.estado").value("EXITOSA"))
                .andExpect(jsonPath("$.impactoHumanidad").value(88));
    }

    // 7. ELIMINAR CIRUGÍA (DELETE /api/v1/cirugias/{id})
    @Test
    public void testEliminarCirugia() throws Exception {
        doNothing().when(instalacionService).eliminarCirugia(501L);

        mockMvc.perform(delete("/api/v1/cirugias/501"))
                .andExpect(status().isNoContent()); // Valida HTTP 204 No Content

        // Verificamos la ejecución del hilo destructor en la capa Service
        verify(instalacionService, times(1)).eliminarCirugia(501L);
    }
}

package com.cps.cyber_patient_service.Controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.cps.cyber_patient_service.DTO.PacienteCompatibilidadDto;
import com.cps.cyber_patient_service.DTO.PacienteContratoDto;
import com.cps.cyber_patient_service.Model.Paciente;
import com.cps.cyber_patient_service.Service.PacienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(PacienteController.class)
public class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc; // Emulador virtual de peticiones a la red médica clandestina

    @MockitoBean
    private PacienteService pacienteService; // Clon simulado del servicio de control biológico

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Paciente davidMartinez;
    private Paciente lucyKushinada;

    @BeforeEach
    void setUp() {
        // Inicialización del paciente 1: Al borde de la cyberpsicosis
        davidMartinez = new Paciente();
        davidMartinez.setId(1L);
        davidMartinez.setAlias("David Martínez");
        davidMartinez.setNivelCyberpsicosis(88);
        davidMartinez.setAltura("ALTO");
        davidMartinez.setDensidadOsea("ALTA");
        davidMartinez.setDensidadMuscular("ALTA");
        davidMartinez.setPeso(85.5);
        davidMartinez.setEdad(18);

        // Inicialización del paciente 2: Estable
        lucyKushinada = new Paciente();
        lucyKushinada.setId(2L);
        lucyKushinada.setAlias("Lucy Kushinada");
        lucyKushinada.setNivelCyberpsicosis(20);
        lucyKushinada.setAltura("MEDIA");
        lucyKushinada.setDensidadOsea("MEDIA");
        lucyKushinada.setDensidadMuscular("MEDIA");
        lucyKushinada.setPeso(60.0);
        lucyKushinada.setEdad(20);
    }

    // 1. LISTA DE PACIENTES (GET /api/v1/paciente)
    @Test
    public void testListarTodos() throws Exception {
        when(pacienteService.obtenerTodos()).thenReturn(List.of(davidMartinez, lucyKushinada));

        mockMvc.perform(get("/api/v1/paciente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].alias").value("David Martínez"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].alias").value("Lucy Kushinada"));
    }

    // 2. BUSCAR UN PACIENTE ESPECÍFICO POR ID (GET /api/v1/paciente/{id})
    @Test
    public void testBuscarPorId() throws Exception {
        when(pacienteService.obtenerPorId(1L)).thenReturn(davidMartinez);

        mockMvc.perform(get("/api/v1/paciente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.alias").value("David Martínez"))
                .andExpect(jsonPath("$.nivelCyberpsicosis").value(88));
    }

    // 3. REGISTRAR UN NUEVO PACIENTE (POST /api/v1/paciente)
    @Test
    public void testCrearPaciente() throws Exception {
        when(pacienteService.guardar(any(Paciente.class))).thenReturn(davidMartinez);

        mockMvc.perform(post("/api/v1/paciente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(davidMartinez)))
                .andExpect(status().isCreated()) // Valida HTTP 201 Created
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.alias").value("David Martínez"));
    }

    // 4. ELIMINAR UN PACIENTE DEL REGISTRO (DELETE /api/v1/paciente/{id})
    @Test
    public void testEliminarPaciente() throws Exception {
        doNothing().when(pacienteService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/paciente/1"))
                .andExpect(status().isNoContent()); // Valida HTTP 204 No Content

        verify(pacienteService, times(1)).eliminar(1L);
    }

    // 5. ACTUALIZAR NIVEL DE CYBERPSICOSIS (PATCH /api/v1/paciente/{id}/psico?modificador=VALUE)
    @Test
    public void testNvlCyberpsico() throws Exception {
        // Simulamos que tras la cirugía su nivel de degradación neurológica sube
        Paciente davidActualizado = davidMartinez;
        davidActualizado.setNivelCyberpsicosis(98);

        when(pacienteService.actualizacionCyberpsicosis(1L, 10)).thenReturn(davidActualizado);

        mockMvc.perform(patch("/api/v1/paciente/1/psico")
                        .param("modificador", "10")) // Parámetro Query requerido
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nivelCyberpsicosis").value(98));
    }

    // 6. VALIDAR ESTADO DE APTITUD DEL PACIENTE (GET /api/v1/paciente/{id}/check)
    @Test
    public void testCheckPaciente() throws Exception {
        when(pacienteService.checkPaciente(1L)).thenReturn(true);

        mockMvc.perform(get("/api/v1/paciente/1/check"))
                .andExpect(status().isOk())
                .andExpect(content().string("true")); // El endpoint retorna una respuesta primitiva pura (Boolean)
    }

    // 7. EMITIR CONTRATO GLOBAL DE COMPRA (GET /api/v1/paciente/{id}/contrato)
    @Test
    public void testObtenerContratoParaVenta() throws Exception {
        when(pacienteService.obtenerPorId(1L)).thenReturn(davidMartinez);

        mockMvc.perform(get("/api/v1/paciente/1/contrato"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPaciente").value(1))
                .andExpect(jsonPath("$.AliasPaciente").value("David Martínez")); // Ojo con la mayúscula que definiste en el DTO (AliasPaciente)
    }

    // 8. EMITIR DATOS DE COMPATIBILIDAD (GET /api/v1/paciente/{id}/compatibilidad)
    @Test
    public void testObtenerCompatibilidad() throws Exception {
        when(pacienteService.obtenerPorId(2L)).thenReturn(lucyKushinada);

        mockMvc.perform(get("/api/v1/paciente/2/compatibilidad"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.altura").value("MEDIA"))
                .andExpect(jsonPath("$.densidadOsea").value("MEDIA"))
                .andExpect(jsonPath("$.densidadMuscular").value("MEDIA"))
                .andExpect(jsonPath("$.peso").value(60.0))
                .andExpect(jsonPath("$.edad").value(20));
    }
}

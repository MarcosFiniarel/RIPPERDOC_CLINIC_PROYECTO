package com.css.cyber_sale_service.Controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.css.cyber_sale_service.DTO.RequestDto;
import com.css.cyber_sale_service.Model.Venta;
import com.css.cyber_sale_service.Service.VentaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(VentaController.class)
public class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc; // Emulador virtual de peticiones a la pasarela financiera y de ventas

    @MockitoBean
    private VentaService ventaService; // Clon simulado del core orquestador de transacciones de cromo

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Venta ordenSandevistan;
    private Venta ordenMantisBlades;
    private RequestDto requestValido;

    @BeforeEach
    void setUp() {
        // Venta 1: Compra confirmada y pagada de un Sandevistan
        ordenSandevistan = new Venta();
        ordenSandevistan.setId(105L);
        ordenSandevistan.setPacienteId(1L);
        ordenSandevistan.setAliasPaciente("David Martínez");
        ordenSandevistan.setCiberwareId(1L);
        ordenSandevistan.setNombreCiberware("Arasaka Sandevistan MK.V");
        ordenSandevistan.setPrecioCobrado(120000.0);
        ordenSandevistan.setImpactoHumanidad(88);
        ordenSandevistan.setEstado("PAGADA");

        // Venta 2: Transacción paralela en el historial
        ordenMantisBlades = new Venta();
        ordenMantisBlades.setId(106L);
        ordenMantisBlades.setPacienteId(2L);
        ordenMantisBlades.setAliasPaciente("Lucy Kushinada");
        ordenMantisBlades.setCiberwareId(3L);
        ordenMantisBlades.setNombreCiberware("Mantis Blades Thermal");
        ordenMantisBlades.setPrecioCobrado(85000.0);
        ordenMantisBlades.setImpactoHumanidad(45);
        ordenMantisBlades.setEstado("PAGADA");

        // Request DTO minimalista para la simulación del POST
        requestValido = new RequestDto();
        requestValido.setIdPaciente(1L);
        requestValido.setIdCiberware(1L);
    }

    // 1. LISTAR TODAS LAS VENTAS (GET /api/v1/ventas)
    @Test
    public void testListarTodas() throws Exception {
        when(ventaService.obtenerTodas()).thenReturn(List.of(ordenSandevistan, ordenMantisBlades));

        mockMvc.perform(get("/api/v1/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(105))
                .andExpect(jsonPath("$[0].nombreCiberware").value("Arasaka Sandevistan MK.V"))
                .andExpect(jsonPath("$[1].id").value(106))
                .andExpect(jsonPath("$[1].aliasPaciente").value("Lucy Kushinada"));
    }

    // 2. LISTAR VENTAS POR CLIENTE (GET /api/v1/ventas/paciente/{pacienteId})
    @Test
    public void testListarPorPaciente() throws Exception {
        when(ventaService.obtenerPorPaciente(1L)).thenReturn(List.of(ordenSandevistan));

        mockMvc.perform(get("/api/v1/ventas/paciente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(105))
                .andExpect(jsonPath("$[0].pacienteId").value(1))
                .andExpect(jsonPath("$[0].aliasPaciente").value("David Martínez"));
    }

    // 3. LISTAR VENTAS POR CIBERWARE (GET /api/v1/ventas/ciberware/{ciberwareId})
    @Test
    public void testListarPorCiberware() throws Exception {
        when(ventaService.obtenerPorCiberware(1L)).thenReturn(List.of(ordenSandevistan));

        mockMvc.perform(get("/api/v1/ventas/ciberware/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(105))
                .andExpect(jsonPath("$[0].ciberwareId").value(1))
                .andExpect(jsonPath("$[0].nombreCiberware").value("Arasaka Sandevistan MK.V"));
    }

    // 4. BUSCAR VENTA POR ID (GET /api/v1/ventas/{id})
    @Test
    public void testBuscarPorId() throws Exception {
        when(ventaService.obtenerPorId(105L)).thenReturn(ordenSandevistan);

        mockMvc.perform(get("/api/v1/ventas/105"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(105))
                .andExpect(jsonPath("$.estado").value("PAGADA"))
                .andExpect(jsonPath("$.precioCobrado").value(120000.0));
    }

    // 5. CREAR LA VENTA / PROCESAR COMPRA INMEDIATA (POST /api/v1/ventas)
    @Test
    public void testRegistrarVenta() throws Exception {
        when(ventaService.crearVenta(any(RequestDto.class))).thenReturn(ordenSandevistan);

        mockMvc.perform(post("/api/v1/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido)))
                .andExpect(status().isCreated()) // Valida HTTP 201 Created arquitectónico
                .andExpect(jsonPath("$.id").value(105))
                .andExpect(jsonPath("$.estado").value("PAGADA"))
                .andExpect(jsonPath("$.nombreCiberware").value("Arasaka Sandevistan MK.V"));
    }

    // 6. ELIMINAR UNA VENTA DEL HISTÓRICO (DELETE /api/v1/ventas/{id})
    @Test
    public void testEliminarVenta() throws Exception {
        doNothing().when(ventaService).eliminarVenta(105L);

        mockMvc.perform(delete("/api/v1/ventas/105"))
                .andExpect(status().isNoContent()); // Valida HTTP 204 No Content

        // Verificación de auditoría del orquestador local
        verify(ventaService, times(1)).eliminarVenta(105L);
    }
}

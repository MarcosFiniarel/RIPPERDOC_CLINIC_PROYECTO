package com.cfs.cyber_finance_service.Controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.cfs.cyber_finance_service.Model.Billetera;
import com.cfs.cyber_finance_service.Services.BilleteraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(BilleteraController.class)
public class BilleteraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BilleteraService billeteraService;

    private Billetera billeteraDavid;
    private Billetera billeteraLucy;

    @BeforeEach
    void setUp() {
        billeteraDavid = new Billetera(1L, 1L, 500000.0);
        billeteraLucy = new Billetera(2L, 2L, 75000.0);
    }

    // CREAR BILLETERA (POST /api/v1/billeteras)
    @Test
    public void testCrearBilletera() throws Exception {
        Billetera nuevaBilletera = new Billetera(3L, 3L, 0.0);
        when(billeteraService.crear(3L)).thenReturn(nuevaBilletera);

        mockMvc.perform(post("/api/v1/billeteras")
                        .param("id", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()) // Valida HTTP 201 Created
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.pacienteId").value(3))
                .andExpect(jsonPath("$.saldoEddies").value(0.0));
    }

    // OBTENER TODAS LAS BILLETERAS (GET /api/v1/billeteras)
    @Test
    public void testObtenerTodas() throws Exception {
        when(billeteraService.obtenerTodas()).thenReturn(List.of(billeteraDavid, billeteraLucy));

        mockMvc.perform(get("/api/v1/billeteras"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].saldoEddies").value(500000.0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].saldoEddies").value(75000.0));
    }

    // OBTENER BILLETERA POR ID (GET /api/v1/billeteras/{id})
    @Test
    public void testObtenerPorId() throws Exception {
        when(billeteraService.obtenerPorId(1L)).thenReturn(billeteraDavid);

        mockMvc.perform(get("/api/v1/billeteras/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.pacienteId").value(1))
                .andExpect(jsonPath("$.saldoEddies").value(500000.0));
    }

    // OBTENER BILLETERA POR ID DE PACIENTE (GET /api/v1/billeteras/paciente/{pacienteId})
    @Test
    public void testObtenerPorPacienteId() throws Exception {
        when(billeteraService.obtenerPorPacienteId(2L)).thenReturn(billeteraLucy);

        mockMvc.perform(get("/api/v1/billeteras/paciente/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.pacienteId").value(2))
                .andExpect(jsonPath("$.saldoEddies").value(75000.0));
    }

    // SUMAR SALDO A BILLETERA (PUT /api/v1/billeteras/{id}/sumar)
    @Test
    public void testSumarSaldo() throws Exception {
        Billetera davidActualizada = new Billetera(1L, 1L, 550000.0);
        when(billeteraService.sumarSaldo(1L, 50000.0)).thenReturn(davidActualizada);

        mockMvc.perform(put("/api/v1/billeteras/1/sumar")
                        .param("monto", "50000.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.saldoEddies").value(550000.0));
    }

    // RESTAR SALDO A BILLETERA (PUT /api/v1/billeteras/{id}/restar)
    @Test
    public void testRestarSaldo() throws Exception {
        when(billeteraService.restarSaldo(1L, 120000.0)).thenReturn(true);

        mockMvc.perform(put("/api/v1/billeteras/1/restar")
                        .param("monto", "120000.0"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    // ELIMINAR BILLETERA (DELETE /api/v1/billeteras/{id})
    @Test
    public void testEliminar() throws Exception {
        doNothing().when(billeteraService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/billeteras/1"))
                .andExpect(status().isNoContent()); // Valida HTTP 204 No Content

        verify(billeteraService, times(1)).eliminar(1L);
    }
}

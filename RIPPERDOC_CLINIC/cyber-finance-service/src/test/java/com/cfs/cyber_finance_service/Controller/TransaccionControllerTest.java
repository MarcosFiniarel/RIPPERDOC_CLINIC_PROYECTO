package com.cfs.cyber_finance_service.Controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.cfs.cyber_finance_service.Model.Transaccion;
import com.cfs.cyber_finance_service.Services.TransaccionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(TransaccionController.class)
public class TransaccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransaccionService transaccionService;

    private Transaccion transaccionAprobada;
    private Transaccion transaccionRechazada;

    @BeforeEach
    void setUp() {
        // Transpórtate a la auditoría: id, billeteraId, ventaId, monto, estado
        transaccionAprobada = new Transaccion(1L, 1L, 105L, 120000.0, true);
        transaccionRechazada = new Transaccion(2L, 2L, 106L, 350000.0, false);
    }

    // CREAR TRANSACCION (POST /api/v1/transacciones)
    @Test
    public void testCrearTransaccion() throws Exception {
        when(transaccionService.crear(105L, 1L, 120000.0)).thenReturn(true);

        mockMvc.perform(post("/api/v1/transacciones")
                        .param("idVenta", "105")
                        .param("idPaciente", "1")
                        .param("monto", "120000.0"))
                .andExpect(status().isCreated()) // Valida tu HTTP 201 arquitectónico
                .andExpect(content().string("true"));
    }

    // OBTENER TODAS LAS TRANSACCIONES (GET /api/v1/transacciones)
    @Test
    public void testObtenerTodas() throws Exception {
        when(transaccionService.obtenerTodas()).thenReturn(List.of(transaccionAprobada, transaccionRechazada));

        mockMvc.perform(get("/api/v1/transacciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].estado").value(true))
                .andExpect(jsonPath("$[0].monto").value(120000.0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].estado").value(false));
    }

    // OBTENER TRANSACCIONES POR BILLETERA (GET /api/v1/transacciones/billetera/{billeteraId})
    @Test
    public void testObtenerPorBilletera() throws Exception {
        when(transaccionService.obtenerPorBilletera(1L)).thenReturn(List.of(transaccionAprobada));

        mockMvc.perform(get("/api/v1/transacciones/billetera/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].billeteraId").value(1))
                .andExpect(jsonPath("$[0].ventaId").value(105));
    }
}

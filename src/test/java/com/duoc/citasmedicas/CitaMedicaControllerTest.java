package com.duoc.citasmedicas;

import com.duoc.citasmedicas.controller.CitaMedicaController;
import com.duoc.citasmedicas.dto.CitaMedicaDTO;
import com.duoc.citasmedicas.service.CitaMedicaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CitaMedicaController.class)
public class CitaMedicaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CitaMedicaService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /citas debe retornar lista de citas con status 200")
    void testObtenerTodas() throws Exception {
        // Arrange
        CitaMedicaDTO dto = new CitaMedicaDTO(
                1L, "Laura Soto", "12.345.678-9",
                "Medicina General", "Dr. Andrés Fuentes",
                "2025-04-01", "09:00", "RESERVADA"
        );
        when(service.obtenerTodas()).thenReturn(List.of(dto));

        // Act & Assert
        mockMvc.perform(get("/citas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).obtenerTodas();
    }

    @Test
    @DisplayName("POST /citas debe crear una cita y retornar status 201")
    void testProgramarCita() throws Exception {
        // Arrange
        CitaMedicaDTO dto = new CitaMedicaDTO(
                null, "Valentina Ríos", "22.333.444-5",
                "Neurología", "Dr. Esteban Campos",
                "2025-04-20", "09:30", "RESERVADA"
        );
        CitaMedicaDTO dtoCreado = new CitaMedicaDTO(
                6L, "Valentina Ríos", "22.333.444-5",
                "Neurología", "Dr. Esteban Campos",
                "2025-04-20", "09:30", "RESERVADA"
        );
        when(service.programarCita(any(CitaMedicaDTO.class))).thenReturn(dtoCreado);

        // Act & Assert
        mockMvc.perform(post("/citas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(service, times(1)).programarCita(any(CitaMedicaDTO.class));
    }

    @Test
    @DisplayName("GET /citas/{id} debe retornar una cita con status 200")
    void testObtenerPorId() throws Exception {
        // Arrange
        CitaMedicaDTO dto = new CitaMedicaDTO(
                1L, "Laura Soto", "12.345.678-9",
                "Medicina General", "Dr. Andrés Fuentes",
                "2025-04-01", "09:00", "RESERVADA"
        );
        when(service.obtenerPorId(1L)).thenReturn(Optional.of(dto));

        // Act & Assert
        mockMvc.perform(get("/citas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).obtenerPorId(1L);
    }

    @Test
    @DisplayName("GET /citas/{id} debe retornar 404 cuando no existe")
    void testObtenerPorIdNoEncontrado() throws Exception {
        // Arrange
        when(service.obtenerPorId(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/citas/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(1)).obtenerPorId(99L);
    }
}
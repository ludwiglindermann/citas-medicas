package com.duoc.citasmedicas;

import com.duoc.citasmedicas.dto.CitaMedicaDTO;
import com.duoc.citasmedicas.model.CitaMedica;
import com.duoc.citasmedicas.repository.CitaMedicaRepository;
import com.duoc.citasmedicas.service.CitaMedicaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CitaMedicaServiceTest {

    @Mock
    private CitaMedicaRepository repository;

    @InjectMocks
    private CitaMedicaService service;

    private CitaMedica citaMock;

    @BeforeEach
    void setUp() {
        citaMock = new CitaMedica();
        citaMock.setId(1L);
        citaMock.setNombrePaciente("Laura Soto");
        citaMock.setRutPaciente("12.345.678-9");
        citaMock.setEspecialidad("Medicina General");
        citaMock.setNombreMedico("Dr. Andrés Fuentes");
        citaMock.setFecha("2025-04-01");
        citaMock.setHora("09:00");
        citaMock.setEstado("RESERVADA");
    }

    @Test
    @DisplayName("Debe retornar todas las citas correctamente")
    void testObtenerTodas() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(citaMock));

        // Act
        List<CitaMedicaDTO> resultado = service.obtenerTodas();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Laura Soto", resultado.get(0).getNombrePaciente());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar una cita por ID correctamente")
    void testObtenerPorId() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(citaMock));

        // Act
        Optional<CitaMedicaDTO> resultado = service.obtenerPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Laura Soto", resultado.get().getNombrePaciente());
        assertEquals("RESERVADA", resultado.get().getEstado());
        verify(repository, times(1)).findById(1L);
    }
}
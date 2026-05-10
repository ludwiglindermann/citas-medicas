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

    @Test
    @DisplayName("Debe retornar citas disponibles correctamente")
    void testObtenerDisponibles() {
        // Arrange
        citaMock.setEstado("DISPONIBLE");
        when(repository.findByEstado("DISPONIBLE")).thenReturn(List.of(citaMock));

        // Act
        List<CitaMedicaDTO> resultado = service.obtenerDisponibles();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("DISPONIBLE", resultado.get(0).getEstado());
        verify(repository, times(1)).findByEstado("DISPONIBLE");
    }

    @Test
    @DisplayName("Debe programar una cita correctamente con estado RESERVADA")
    void testProgramarCita() {
        // Arrange
        CitaMedicaDTO dto = new CitaMedicaDTO(
                null, "Valentina Ríos", "22.333.444-5",
                "Neurología", "Dr. Esteban Campos",
                "2025-06-01", "10:00", null
        );
        CitaMedica citaGuardada = new CitaMedica();
        citaGuardada.setId(7L);
        citaGuardada.setNombrePaciente("Valentina Ríos");
        citaGuardada.setRutPaciente("22.333.444-5");
        citaGuardada.setEspecialidad("Neurología");
        citaGuardada.setNombreMedico("Dr. Esteban Campos");
        citaGuardada.setFecha("2025-06-01");
        citaGuardada.setHora("10:00");
        citaGuardada.setEstado("RESERVADA");

        when(repository.save(any(CitaMedica.class))).thenReturn(citaGuardada);

        // Act
        CitaMedicaDTO resultado = service.programarCita(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("RESERVADA", resultado.getEstado());
        assertEquals("Valentina Ríos", resultado.getNombrePaciente());
        verify(repository, times(1)).save(any(CitaMedica.class));
    }
}
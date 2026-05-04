package com.duoc.citasmedicas.service;

import com.duoc.citasmedicas.dto.CitaMedicaDTO;
import com.duoc.citasmedicas.model.CitaMedica;
import com.duoc.citasmedicas.repository.CitaMedicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CitaMedicaService {

    @Autowired
    private CitaMedicaRepository repository;

    // Convierte Entity → DTO
    private CitaMedicaDTO convertirADTO(CitaMedica cita) {
        return new CitaMedicaDTO(
                cita.getId(),
                cita.getNombrePaciente(),
                cita.getRutPaciente(),
                cita.getEspecialidad(),
                cita.getNombreMedico(),
                cita.getFecha(),
                cita.getHora(),
                cita.getEstado()
        );
    }

    // Convierte DTO → Entity
    private CitaMedica convertirAEntity(CitaMedicaDTO dto) {
        CitaMedica cita = new CitaMedica();
        cita.setNombrePaciente(dto.getNombrePaciente());
        cita.setRutPaciente(dto.getRutPaciente());
        cita.setEspecialidad(dto.getEspecialidad());
        cita.setNombreMedico(dto.getNombreMedico());
        cita.setFecha(dto.getFecha());
        cita.setHora(dto.getHora());
        cita.setEstado(dto.getEstado());
        return cita;
    }

    public List<CitaMedicaDTO> obtenerTodas() {
        return repository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<CitaMedicaDTO> obtenerPorId(Long id) {
        return repository.findById(id).map(this::convertirADTO);
    }

    public List<CitaMedicaDTO> obtenerDisponibles() {
        return repository.findByEstado("DISPONIBLE").stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<CitaMedicaDTO> obtenerPorEspecialidad(String especialidad) {
        return repository.findByEspecialidadIgnoreCase(especialidad).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public CitaMedicaDTO programarCita(CitaMedicaDTO dto) {
        CitaMedica cita = convertirAEntity(dto);
        cita.setEstado("RESERVADA");
        return convertirADTO(repository.save(cita));
    }

    public Optional<CitaMedicaDTO> actualizarCita(Long id, CitaMedicaDTO dto) {
        return repository.findById(id).map(cita -> {
            if (cita.getEstado().equals("CANCELADA")) {
                throw new IllegalStateException("No se puede modificar una cita cancelada.");
            }
            if (dto.getNombrePaciente() != null && !dto.getNombrePaciente().isBlank())
                cita.setNombrePaciente(dto.getNombrePaciente());
            if (dto.getRutPaciente() != null && !dto.getRutPaciente().isBlank())
                cita.setRutPaciente(dto.getRutPaciente());
            if (dto.getEspecialidad() != null && !dto.getEspecialidad().isBlank())
                cita.setEspecialidad(dto.getEspecialidad());
            if (dto.getNombreMedico() != null && !dto.getNombreMedico().isBlank())
                cita.setNombreMedico(dto.getNombreMedico());
            if (dto.getFecha() != null && !dto.getFecha().isBlank())
                cita.setFecha(dto.getFecha());
            if (dto.getHora() != null && !dto.getHora().isBlank())
                cita.setHora(dto.getHora());
            if (dto.getEstado() != null && !dto.getEstado().isBlank())
                cita.setEstado(dto.getEstado().toUpperCase());
            return convertirADTO(repository.save(cita));
        });
    }

    public Optional<CitaMedicaDTO> cancelarCita(Long id) {
        return repository.findById(id).map(cita -> {
            if (cita.getEstado().equals("CANCELADA")) {
                throw new IllegalStateException("La cita ya está cancelada.");
            }
            cita.setEstado("CANCELADA");
            return convertirADTO(repository.save(cita));
        });
    }

    public boolean eliminarCita(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
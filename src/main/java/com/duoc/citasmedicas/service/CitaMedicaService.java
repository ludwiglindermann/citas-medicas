package com.duoc.citasmedicas.service;

import com.duoc.citasmedicas.model.CitaMedica;
import com.duoc.citasmedicas.repository.CitaMedicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CitaMedicaService {

    @Autowired
    private CitaMedicaRepository repository;

    public List<CitaMedica> obtenerTodas() {
        return repository.findAll();
    }

    public Optional<CitaMedica> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public List<CitaMedica> obtenerDisponibles() {
        return repository.findByEstado("DISPONIBLE");
    }

    public List<CitaMedica> obtenerPorEspecialidad(String especialidad) {
        return repository.findByEspecialidadIgnoreCase(especialidad);
    }

    public CitaMedica programarCita(CitaMedica cita) {
        cita.setEstado("RESERVADA");
        return repository.save(cita);
    }

    public Optional<CitaMedica> actualizarCita(Long id, CitaMedica datos) {
        return repository.findById(id).map(cita -> {
            if (cita.getEstado().equals("CANCELADA")) {
                throw new IllegalStateException("No se puede modificar una cita cancelada.");
            }
            if (datos.getNombrePaciente() != null && !datos.getNombrePaciente().isBlank())
                cita.setNombrePaciente(datos.getNombrePaciente());
            if (datos.getRutPaciente() != null && !datos.getRutPaciente().isBlank())
                cita.setRutPaciente(datos.getRutPaciente());
            if (datos.getEspecialidad() != null && !datos.getEspecialidad().isBlank())
                cita.setEspecialidad(datos.getEspecialidad());
            if (datos.getNombreMedico() != null && !datos.getNombreMedico().isBlank())
                cita.setNombreMedico(datos.getNombreMedico());
            if (datos.getFecha() != null && !datos.getFecha().isBlank())
                cita.setFecha(datos.getFecha());
            if (datos.getHora() != null && !datos.getHora().isBlank())
                cita.setHora(datos.getHora());
            if (datos.getEstado() != null && !datos.getEstado().isBlank())
                cita.setEstado(datos.getEstado().toUpperCase());
            return repository.save(cita);
        });
    }

    public Optional<CitaMedica> cancelarCita(Long id) {
        return repository.findById(id).map(cita -> {
            if (cita.getEstado().equals("CANCELADA")) {
                throw new IllegalStateException("La cita ya está cancelada.");
            }
            cita.setEstado("CANCELADA");
            return repository.save(cita);
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
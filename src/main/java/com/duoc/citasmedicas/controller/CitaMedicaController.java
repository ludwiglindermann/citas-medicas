package com.duoc.citasmedicas.controller;

import com.duoc.citasmedicas.model.CitaMedica;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/citas")
public class CitaMedicaController {

    private List<CitaMedica> citas = new ArrayList<>(List.of(
        new CitaMedica(1L, "Laura Soto",    "12.345.678-9", "Medicina General", "Dr. Andrés Fuentes", "2025-04-01", "09:00", "RESERVADA"),
        new CitaMedica(2L, "Pedro Núñez",   "98.765.432-1", "Cardiología",      "Dra. Paula Herrera", "2025-04-02", "10:30", "RESERVADA"),
        new CitaMedica(3L, "Carmen Vega",   "11.223.344-5", "Dermatología",     "Dr. Luis Morales",   "2025-04-03", "11:00", "CANCELADA"),
        new CitaMedica(4L, "Roberto Díaz",  "55.667.788-K", "Traumatología",    "Dra. Claudia Ríos",  "2025-04-05", "14:00", "DISPONIBLE"),
        new CitaMedica(5L, "Isabel Vargas", "33.445.566-7", "Oftalmología",     "Dr. Marcelo Torres", "2025-04-07", "15:30", "DISPONIBLE")
    ));

    private long nextId = 6L;

    // GET /citas → Listar todas las citas
    @GetMapping
    public ResponseEntity<List<CitaMedica>> obtenerTodas() {
        return ResponseEntity.ok(citas);
    }

    // GET /citas/{id} → Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<CitaMedica> cita = citas.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
        if (cita.isPresent()) {
            return ResponseEntity.ok(cita.get());
        }
        return ResponseEntity.status(404).body("Cita con ID " + id + " no encontrada.");
    }

    // GET /citas/disponibles → Ver horarios disponibles
    @GetMapping("/disponibles")
    public ResponseEntity<?> obtenerDisponibles() {
        List<CitaMedica> disponibles = citas.stream()
                .filter(c -> c.getEstado().equalsIgnoreCase("DISPONIBLE"))
                .collect(Collectors.toList());
        if (disponibles.isEmpty()) {
            return ResponseEntity.ok("No hay citas disponibles en este momento.");
        }
        return ResponseEntity.ok(disponibles);
    }

    // GET /citas/especialidad/{especialidad} → Buscar por especialidad
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<?> obtenerPorEspecialidad(@PathVariable String especialidad) {
        List<CitaMedica> resultado = citas.stream()
                .filter(c -> c.getEspecialidad().equalsIgnoreCase(especialidad))
                .collect(Collectors.toList());
        if (resultado.isEmpty()) {
            return ResponseEntity.ok("No se encontraron citas para: " + especialidad);
        }
        return ResponseEntity.ok(resultado);
    }

    // POST /citas → Programar nueva cita
    @PostMapping
    public ResponseEntity<?> programarCita(@RequestBody CitaMedica nuevaCita) {
        if (nuevaCita.getNombrePaciente() == null || nuevaCita.getNombrePaciente().isBlank()) {
            return ResponseEntity.badRequest().body("El nombre del paciente es obligatorio.");
        }
        if (nuevaCita.getRutPaciente() == null || nuevaCita.getRutPaciente().isBlank()) {
            return ResponseEntity.badRequest().body("El RUT del paciente es obligatorio.");
        }
        if (nuevaCita.getEspecialidad() == null || nuevaCita.getEspecialidad().isBlank()) {
            return ResponseEntity.badRequest().body("La especialidad es obligatoria.");
        }
        if (nuevaCita.getNombreMedico() == null || nuevaCita.getNombreMedico().isBlank()) {
            return ResponseEntity.badRequest().body("El nombre del médico es obligatorio.");
        }
        if (nuevaCita.getFecha() == null || nuevaCita.getFecha().isBlank()) {
            return ResponseEntity.badRequest().body("La fecha es obligatoria.");
        }
        if (nuevaCita.getHora() == null || nuevaCita.getHora().isBlank()) {
            return ResponseEntity.badRequest().body("La hora es obligatoria.");
        }

        // Validar conflicto de horario
        boolean conflicto = citas.stream()
                .anyMatch(c -> c.getNombreMedico().equalsIgnoreCase(nuevaCita.getNombreMedico())
                        && c.getFecha().equals(nuevaCita.getFecha())
                        && c.getHora().equals(nuevaCita.getHora())
                        && !c.getEstado().equals("CANCELADA"));
        if (conflicto) {
            return ResponseEntity.badRequest().body("El médico ya tiene una cita en ese horario.");
        }

        nuevaCita.setId(nextId++);
        nuevaCita.setEstado("RESERVADA");
        citas.add(nuevaCita);
        return ResponseEntity.status(201).body(nuevaCita);
    }

    // PUT /citas/{id} → Actualizar cita
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCita(@PathVariable Long id, @RequestBody CitaMedica datos) {
        Optional<CitaMedica> citaExistente = citas.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
        if (citaExistente.isEmpty()) {
            return ResponseEntity.status(404).body("Cita con ID " + id + " no encontrada.");
        }
        CitaMedica cita = citaExistente.get();
        if (cita.getEstado().equals("CANCELADA")) {
            return ResponseEntity.badRequest().body("No se puede modificar una cita cancelada.");
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
        return ResponseEntity.ok(cita);
    }

    // DELETE /citas/{id} → Cancelar cita
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelarCita(@PathVariable Long id) {
        Optional<CitaMedica> citaExistente = citas.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
        if (citaExistente.isEmpty()) {
            return ResponseEntity.status(404).body("Cita con ID " + id + " no encontrada.");
        }
        CitaMedica cita = citaExistente.get();
        if (cita.getEstado().equals("CANCELADA")) {
            return ResponseEntity.badRequest().body("La cita ya está cancelada.");
        }
        cita.setEstado("CANCELADA");
        return ResponseEntity.ok("Cita con ID " + id + " cancelada correctamente.");
    }
}
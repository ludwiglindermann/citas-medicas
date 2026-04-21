package com.duoc.citasmedicas.controller;

import com.duoc.citasmedicas.model.CitaMedica;
import com.duoc.citasmedicas.service.CitaMedicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/citas")
public class CitaMedicaController {

    @Autowired
    private CitaMedicaService service;

    // GET /citas → Listar todas las citas
    @GetMapping
    public ResponseEntity<List<CitaMedica>> obtenerTodas() {
        return ResponseEntity.ok(service.obtenerTodas());
    }

    // GET /citas/{id} → Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("Cita con ID " + id + " no encontrada."));
    }

    // GET /citas/disponibles → Ver horarios disponibles
    @GetMapping("/disponibles")
    public ResponseEntity<?> obtenerDisponibles() {
        List<CitaMedica> disponibles = service.obtenerDisponibles();
        if (disponibles.isEmpty()) {
            return ResponseEntity.ok("No hay citas disponibles en este momento.");
        }
        return ResponseEntity.ok(disponibles);
    }

    // GET /citas/especialidad/{especialidad} → Buscar por especialidad
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<?> obtenerPorEspecialidad(@PathVariable String especialidad) {
        List<CitaMedica> resultado = service.obtenerPorEspecialidad(especialidad);
        if (resultado.isEmpty()) {
            return ResponseEntity.ok("No se encontraron citas para: " + especialidad);
        }
        return ResponseEntity.ok(resultado);
    }

    // POST /citas → Programar nueva cita
    @PostMapping
    public ResponseEntity<?> programarCita(@RequestBody CitaMedica cita) {
        if (cita.getNombrePaciente() == null || cita.getNombrePaciente().isBlank()) {
            return ResponseEntity.badRequest().body("El nombre del paciente es obligatorio.");
        }
        if (cita.getRutPaciente() == null || cita.getRutPaciente().isBlank()) {
            return ResponseEntity.badRequest().body("El RUT del paciente es obligatorio.");
        }
        if (cita.getEspecialidad() == null || cita.getEspecialidad().isBlank()) {
            return ResponseEntity.badRequest().body("La especialidad es obligatoria.");
        }
        if (cita.getNombreMedico() == null || cita.getNombreMedico().isBlank()) {
            return ResponseEntity.badRequest().body("El nombre del médico es obligatorio.");
        }
        if (cita.getFecha() == null || cita.getFecha().isBlank()) {
            return ResponseEntity.badRequest().body("La fecha es obligatoria.");
        }
        if (cita.getHora() == null || cita.getHora().isBlank()) {
            return ResponseEntity.badRequest().body("La hora es obligatoria.");
        }
        return ResponseEntity.status(201).body(service.programarCita(cita));
    }

    // PUT /citas/{id} → Actualizar cita
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCita(@PathVariable Long id, @RequestBody CitaMedica datos) {
        if (datos.getEstado() != null) {
            String upper = datos.getEstado().toUpperCase();
            if (!upper.equals("DISPONIBLE") && !upper.equals("RESERVADA") && !upper.equals("CANCELADA")) {
                return ResponseEntity.badRequest()
                        .body("Estado inválido. Use: DISPONIBLE, RESERVADA o CANCELADA.");
            }
        }
        try {
            return service.actualizarCita(id, datos)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(404).body("Cita con ID " + id + " no encontrada."));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE /citas/{id} → Cancelar cita
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelarCita(@PathVariable Long id) {
        try {
            return service.cancelarCita(id)
                    .<ResponseEntity<?>>map(c -> ResponseEntity.ok("Cita con ID " + id + " cancelada correctamente."))
                    .orElse(ResponseEntity.status(404).body("Cita con ID " + id + " no encontrada."));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
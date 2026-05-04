package com.duoc.citasmedicas.controller;

import com.duoc.citasmedicas.dto.CitaMedicaDTO;
import com.duoc.citasmedicas.service.CitaMedicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/citas")
public class CitaMedicaController {

    @Autowired
    private CitaMedicaService service;

    // GET /citas → Listar todas las citas
    @GetMapping
    public ResponseEntity<CollectionModel<CitaMedicaDTO>> obtenerTodas() {
        List<CitaMedicaDTO> citas = service.obtenerTodas();
        citas.forEach(dto -> {
            dto.add(linkTo(methodOn(CitaMedicaController.class).obtenerPorId(dto.getId())).withSelfRel());
            dto.add(linkTo(methodOn(CitaMedicaController.class).obtenerTodas()).withRel("todas-las-citas"));
        });
        CollectionModel<CitaMedicaDTO> response = CollectionModel.of(citas,
                linkTo(methodOn(CitaMedicaController.class).obtenerTodas()).withSelfRel());
        return ResponseEntity.ok(response);
    }

    // GET /citas/{id} → Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<CitaMedicaDTO> resultado = service.obtenerPorId(id);
        if (resultado.isPresent()) {
            CitaMedicaDTO dto = resultado.get();
            dto.add(linkTo(methodOn(CitaMedicaController.class).obtenerPorId(id)).withSelfRel());
            dto.add(linkTo(methodOn(CitaMedicaController.class).obtenerTodas()).withRel("todas-las-citas"));
            dto.add(Link.of("/citas/" + id).withRel("cancelar"));
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.status(404).body("Cita con ID " + id + " no encontrada.");
    }

    // GET /citas/disponibles → Ver horarios disponibles
    @GetMapping("/disponibles")
    public ResponseEntity<?> obtenerDisponibles() {
        List<CitaMedicaDTO> disponibles = service.obtenerDisponibles();
        disponibles.forEach(dto ->
            dto.add(linkTo(methodOn(CitaMedicaController.class).obtenerPorId(dto.getId())).withSelfRel())
        );
        if (disponibles.isEmpty()) {
            return ResponseEntity.ok("No hay citas disponibles en este momento.");
        }
        return ResponseEntity.ok(disponibles);
    }

    // GET /citas/especialidad/{especialidad} → Buscar por especialidad
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<?> obtenerPorEspecialidad(@PathVariable String especialidad) {
        List<CitaMedicaDTO> resultado = service.obtenerPorEspecialidad(especialidad);
        resultado.forEach(dto ->
            dto.add(linkTo(methodOn(CitaMedicaController.class).obtenerPorId(dto.getId())).withSelfRel())
        );
        if (resultado.isEmpty()) {
            return ResponseEntity.ok("No se encontraron citas para: " + especialidad);
        }
        return ResponseEntity.ok(resultado);
    }

    // POST /citas → Programar nueva cita
    @PostMapping
    public ResponseEntity<?> programarCita(@RequestBody CitaMedicaDTO dto) {
        if (dto.getNombrePaciente() == null || dto.getNombrePaciente().isBlank()) {
            return ResponseEntity.badRequest().body("El nombre del paciente es obligatorio.");
        }
        if (dto.getRutPaciente() == null || dto.getRutPaciente().isBlank()) {
            return ResponseEntity.badRequest().body("El RUT del paciente es obligatorio.");
        }
        if (dto.getEspecialidad() == null || dto.getEspecialidad().isBlank()) {
            return ResponseEntity.badRequest().body("La especialidad es obligatoria.");
        }
        if (dto.getNombreMedico() == null || dto.getNombreMedico().isBlank()) {
            return ResponseEntity.badRequest().body("El nombre del médico es obligatorio.");
        }
        if (dto.getFecha() == null || dto.getFecha().isBlank()) {
            return ResponseEntity.badRequest().body("La fecha es obligatoria.");
        }
        if (dto.getHora() == null || dto.getHora().isBlank()) {
            return ResponseEntity.badRequest().body("La hora es obligatoria.");
        }
        CitaMedicaDTO nueva = service.programarCita(dto);
        nueva.add(linkTo(methodOn(CitaMedicaController.class).obtenerPorId(nueva.getId())).withSelfRel());
        nueva.add(linkTo(methodOn(CitaMedicaController.class).obtenerTodas()).withRel("todas-las-citas"));
        return ResponseEntity.status(201).body(nueva);
    }

    // PUT /citas/{id} → Actualizar cita
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCita(@PathVariable Long id, @RequestBody CitaMedicaDTO dto) {
        if (dto.getEstado() != null) {
            String upper = dto.getEstado().toUpperCase();
            if (!upper.equals("DISPONIBLE") && !upper.equals("RESERVADA") && !upper.equals("CANCELADA")) {
                return ResponseEntity.badRequest()
                        .body("Estado inválido. Use: DISPONIBLE, RESERVADA o CANCELADA.");
            }
        }
        Optional<CitaMedicaDTO> resultado = service.actualizarCita(id, dto);
        if (resultado.isPresent()) {
            CitaMedicaDTO actualizado = resultado.get();
            actualizado.add(linkTo(methodOn(CitaMedicaController.class).obtenerPorId(id)).withSelfRel());
            actualizado.add(linkTo(methodOn(CitaMedicaController.class).obtenerTodas()).withRel("todas-las-citas"));
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.status(404).body("Cita con ID " + id + " no encontrada.");
    }

    // DELETE /citas/{id} → Cancelar cita
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelarCita(@PathVariable Long id) {
        try {
            Optional<CitaMedicaDTO> resultado = service.cancelarCita(id);
            if (resultado.isPresent()) {
                return ResponseEntity.ok("Cita con ID " + id + " cancelada correctamente.");
            }
            return ResponseEntity.status(404).body("Cita con ID " + id + " no encontrada.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
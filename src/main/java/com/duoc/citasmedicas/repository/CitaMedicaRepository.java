package com.duoc.citasmedicas.repository;

import com.duoc.citasmedicas.model.CitaMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitaMedicaRepository extends JpaRepository<CitaMedica, Long> {

    List<CitaMedica> findByEstado(String estado);

    List<CitaMedica> findByEspecialidadIgnoreCase(String especialidad);
}
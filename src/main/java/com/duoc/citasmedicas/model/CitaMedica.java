package com.duoc.citasmedicas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "CITAS_MEDICAS")
public class CitaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cita")
    @SequenceGenerator(name = "seq_cita", sequenceName = "SEQ_CITAS_MEDICAS", allocationSize = 1)
    private Long id;

    @Column(name = "NOMBRE_PACIENTE", nullable = false, length = 100)
    private String nombrePaciente;

    @Column(name = "RUT_PACIENTE", nullable = false, length = 15)
    private String rutPaciente;

    @Column(name = "ESPECIALIDAD", nullable = false, length = 100)
    private String especialidad;

    @Column(name = "NOMBRE_MEDICO", nullable = false, length = 100)
    private String nombreMedico;

    @Column(name = "FECHA", nullable = false, length = 10)
    private String fecha;

    @Column(name = "HORA", nullable = false, length = 5)
    private String hora;

    @Column(name = "ESTADO", nullable = false, length = 20)
    private String estado;

    public CitaMedica() {}

    public CitaMedica(String nombrePaciente, String rutPaciente, String especialidad,
                      String nombreMedico, String fecha, String hora, String estado) {
        this.nombrePaciente = nombrePaciente;
        this.rutPaciente = rutPaciente;
        this.especialidad = especialidad;
        this.nombreMedico = nombreMedico;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }

    public String getRutPaciente() { return rutPaciente; }
    public void setRutPaciente(String rutPaciente) { this.rutPaciente = rutPaciente; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getNombreMedico() { return nombreMedico; }
    public void setNombreMedico(String nombreMedico) { this.nombreMedico = nombreMedico; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
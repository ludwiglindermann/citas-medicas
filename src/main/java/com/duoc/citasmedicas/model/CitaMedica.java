package com.duoc.citasmedicas.model;

public class CitaMedica {

    private Long id;
    private String nombrePaciente;
    private String rutPaciente;
    private String especialidad;
    private String nombreMedico;
    private String fecha;
    private String hora;
    private String estado;

    public CitaMedica() {}

    public CitaMedica(Long id, String nombrePaciente, String rutPaciente,
                      String especialidad, String nombreMedico,
                      String fecha, String hora, String estado) {
        this.id = id;
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
package edu.pucmm.eventosacademicos.dto;

import edu.pucmm.eventosacademicos.modelo.Inscripcion;

import java.time.LocalDateTime;

public class InscripcionDTO {

    private Long id;
    private LocalDateTime fechaInscripcion;
    private String estado;
    private EventoResumenDTO evento;

    public InscripcionDTO() {
    }

    public static InscripcionDTO desde(Inscripcion inscripcion, long inscritosEvento) {
        InscripcionDTO dto = new InscripcionDTO();
        dto.setId(inscripcion.getId());
        dto.setFechaInscripcion(inscripcion.getFechaInscripcion());
        dto.setEstado(inscripcion.getEstado().name());
        dto.setEvento(EventoResumenDTO.desde(inscripcion.getEvento(), inscritosEvento));
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDateTime fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public EventoResumenDTO getEvento() {
        return evento;
    }

    public void setEvento(EventoResumenDTO evento) {
        this.evento = evento;
    }
}

package edu.pucmm.eventosacademicos.dto;

import edu.pucmm.eventosacademicos.modelo.Evento;

import java.time.LocalDateTime;

public class EventoResumenDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaHora;
    private String lugar;
    private int cupoMaximo;
    private String estado;
    private long inscritos;
    private String organizadorNombre;

    public EventoResumenDTO() {
    }

    public static EventoResumenDTO desde(Evento evento, long inscritos) {
        EventoResumenDTO dto = new EventoResumenDTO();
        dto.setId(evento.getId());
        dto.setTitulo(evento.getTitulo());
        dto.setDescripcion(evento.getDescripcion());
        dto.setFechaHora(evento.getFechaHora());
        dto.setLugar(evento.getLugar());
        dto.setCupoMaximo(evento.getCupoMaximo());
        dto.setEstado(evento.getEstado().name());
        dto.setInscritos(inscritos);
        dto.setOrganizadorNombre(evento.getOrganizador().getNombreCompleto());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public long getInscritos() {
        return inscritos;
    }

    public void setInscritos(long inscritos) {
        this.inscritos = inscritos;
    }

    public String getOrganizadorNombre() {
        return organizadorNombre;
    }

    public void setOrganizadorNombre(String organizadorNombre) {
        this.organizadorNombre = organizadorNombre;
    }
}

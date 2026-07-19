package edu.pucmm.eventosacademicos.dto;

import java.time.LocalDateTime;

public class EventoResumenDTO {

    private Long id;
    private String titulo;
    private LocalDateTime fechaHora;
    private String lugar;
    private int cupoMaximo;
    private long inscritos;

    public EventoResumenDTO() {
    }

    public EventoResumenDTO(Long id, String titulo, LocalDateTime fechaHora, String lugar, int cupoMaximo, long inscritos) {
        this.id = id;
        this.titulo = titulo;
        this.fechaHora = fechaHora;
        this.lugar = lugar;
        this.cupoMaximo = cupoMaximo;
        this.inscritos = inscritos;
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

    public long getInscritos() {
        return inscritos;
    }

    public void setInscritos(long inscritos) {
        this.inscritos = inscritos;
    }
}

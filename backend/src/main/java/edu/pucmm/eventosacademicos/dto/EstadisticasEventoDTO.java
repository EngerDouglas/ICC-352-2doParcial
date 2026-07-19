package edu.pucmm.eventosacademicos.dto;

import java.util.List;

public class EstadisticasEventoDTO {

    private long totalInscritos;
    private long totalAsistentes;
    private double porcentajeAsistencia;
    private List<PuntoEstadisticaDTO> inscripcionesPorDia;
    private List<PuntoEstadisticaDTO> asistenciaPorHora;

    public EstadisticasEventoDTO() {
    }

    public EstadisticasEventoDTO(long totalInscritos, long totalAsistentes, double porcentajeAsistencia,
                                  List<PuntoEstadisticaDTO> inscripcionesPorDia, List<PuntoEstadisticaDTO> asistenciaPorHora) {
        this.totalInscritos = totalInscritos;
        this.totalAsistentes = totalAsistentes;
        this.porcentajeAsistencia = porcentajeAsistencia;
        this.inscripcionesPorDia = inscripcionesPorDia;
        this.asistenciaPorHora = asistenciaPorHora;
    }

    public long getTotalInscritos() {
        return totalInscritos;
    }

    public void setTotalInscritos(long totalInscritos) {
        this.totalInscritos = totalInscritos;
    }

    public long getTotalAsistentes() {
        return totalAsistentes;
    }

    public void setTotalAsistentes(long totalAsistentes) {
        this.totalAsistentes = totalAsistentes;
    }

    public double getPorcentajeAsistencia() {
        return porcentajeAsistencia;
    }

    public void setPorcentajeAsistencia(double porcentajeAsistencia) {
        this.porcentajeAsistencia = porcentajeAsistencia;
    }

    public List<PuntoEstadisticaDTO> getInscripcionesPorDia() {
        return inscripcionesPorDia;
    }

    public void setInscripcionesPorDia(List<PuntoEstadisticaDTO> inscripcionesPorDia) {
        this.inscripcionesPorDia = inscripcionesPorDia;
    }

    public List<PuntoEstadisticaDTO> getAsistenciaPorHora() {
        return asistenciaPorHora;
    }

    public void setAsistenciaPorHora(List<PuntoEstadisticaDTO> asistenciaPorHora) {
        this.asistenciaPorHora = asistenciaPorHora;
    }
}

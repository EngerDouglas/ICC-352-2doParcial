package edu.pucmm.eventosacademicos.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "asistencias")
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inscripcion_id", nullable = false, unique = true)
    private Inscripcion inscripcion;

    @Column(name = "fecha_hora_escaneo", nullable = false)
    private LocalDateTime fechaHoraEscaneo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validado_por_id")
    private Usuario validadoPor;

    public Asistencia() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Inscripcion getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
    }

    public LocalDateTime getFechaHoraEscaneo() {
        return fechaHoraEscaneo;
    }

    public void setFechaHoraEscaneo(LocalDateTime fechaHoraEscaneo) {
        this.fechaHoraEscaneo = fechaHoraEscaneo;
    }

    public Usuario getValidadoPor() {
        return validadoPor;
    }

    public void setValidadoPor(Usuario validadoPor) {
        this.validadoPor = validadoPor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Asistencia)) return false;
        Asistencia that = (Asistencia) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Asistencia{id=" + id + ", fechaHoraEscaneo=" + fechaHoraEscaneo + "}";
    }
}

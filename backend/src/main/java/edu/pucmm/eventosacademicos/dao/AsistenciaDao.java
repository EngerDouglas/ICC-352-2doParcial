package edu.pucmm.eventosacademicos.dao;

import edu.pucmm.eventosacademicos.dto.PuntoEstadisticaDTO;
import edu.pucmm.eventosacademicos.modelo.Asistencia;
import edu.pucmm.eventosacademicos.util.JpaUtil;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class AsistenciaDao {

    public Asistencia buscarPorInscripcionId(Long inscripcionId) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            List<Asistencia> resultado = em.createQuery(
                            "SELECT a FROM Asistencia a WHERE a.inscripcion.id = :inscripcionId", Asistencia.class)
                    .setParameter("inscripcionId", inscripcionId)
                    .getResultList();
            return resultado.isEmpty() ? null : resultado.get(0);
        } finally {
            em.close();
        }
    }

    public boolean existeAsistenciaParaInscripcion(Long inscripcionId) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            Long total = em.createQuery(
                            "SELECT COUNT(a) FROM Asistencia a WHERE a.inscripcion.id = :inscripcionId", Long.class)
                    .setParameter("inscripcionId", inscripcionId)
                    .getSingleResult();
            return total > 0;
        } finally {
            em.close();
        }
    }

    public Asistencia guardar(Asistencia asistencia) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(asistencia);
            em.getTransaction().commit();
            return asistencia;
        } finally {
            em.close();
        }
    }

    public long contarAsistenciasPorEvento(Long eventoId) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(a) FROM Asistencia a WHERE a.inscripcion.evento.id = :eventoId", Long.class)
                    .setParameter("eventoId", eventoId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<PuntoEstadisticaDTO> contarAsistenciasPorHora(Long eventoId) {
        EntityManager em = JpaUtil.crearEntityManager();
        List<LocalDateTime> fechas;
        try {
            fechas = em.createQuery(
                            "SELECT a.fechaHoraEscaneo FROM Asistencia a WHERE a.inscripcion.evento.id = :eventoId",
                            LocalDateTime.class)
                    .setParameter("eventoId", eventoId)
                    .getResultList();
        } finally {
            em.close();
        }

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:00");
        Map<String, Long> porHora = new TreeMap<>();
        for (LocalDateTime fecha : fechas) {
            String etiqueta = fecha.format(formato);
            porHora.merge(etiqueta, 1L, Long::sum);
        }
        return porHora.entrySet().stream()
                .map(entrada -> new PuntoEstadisticaDTO(entrada.getKey(), entrada.getValue()))
                .collect(Collectors.toList());
    }
}

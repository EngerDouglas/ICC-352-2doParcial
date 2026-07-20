package edu.pucmm.eventosacademicos.dao;

import edu.pucmm.eventosacademicos.dto.PuntoEstadisticaDTO;
import edu.pucmm.eventosacademicos.modelo.EstadoInscripcion;
import edu.pucmm.eventosacademicos.modelo.Inscripcion;
import edu.pucmm.eventosacademicos.util.JpaUtil;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class InscripcionDao {

    public Inscripcion buscarPorId(Long id) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.find(Inscripcion.class, id);
        } finally {
            em.close();
        }
    }

    public Inscripcion buscarPorEventoYParticipante(Long eventoId, Long participanteId) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            List<Inscripcion> resultado = em.createQuery(
                            "SELECT i FROM Inscripcion i WHERE i.evento.id = :eventoId AND i.participante.id = :participanteId",
                            Inscripcion.class)
                    .setParameter("eventoId", eventoId)
                    .setParameter("participanteId", participanteId)
                    .getResultList();
            return resultado.isEmpty() ? null : resultado.get(0);
        } finally {
            em.close();
        }
    }

    public Inscripcion buscarPorToken(String token) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            List<Inscripcion> resultado = em.createQuery(
                            "SELECT i FROM Inscripcion i WHERE i.tokenValidacionQr = :token", Inscripcion.class)
                    .setParameter("token", token)
                    .getResultList();
            return resultado.isEmpty() ? null : resultado.get(0);
        } finally {
            em.close();
        }
    }

    public List<Inscripcion> listarPorEvento(Long eventoId) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery(
                            "SELECT i FROM Inscripcion i WHERE i.evento.id = :eventoId ORDER BY i.fechaInscripcion",
                            Inscripcion.class)
                    .setParameter("eventoId", eventoId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Inscripcion> listarPorParticipante(Long participanteId) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery(
                            "SELECT i FROM Inscripcion i WHERE i.participante.id = :participanteId ORDER BY i.fechaInscripcion DESC",
                            Inscripcion.class)
                    .setParameter("participanteId", participanteId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public long contarInscripcionesActivasPorEvento(Long eventoId) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(i) FROM Inscripcion i WHERE i.evento.id = :eventoId AND i.estado = :estado",
                            Long.class)
                    .setParameter("eventoId", eventoId)
                    .setParameter("estado", EstadoInscripcion.ACTIVA)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public Inscripcion guardar(Inscripcion inscripcion) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(inscripcion);
            em.getTransaction().commit();
            return inscripcion;
        } finally {
            em.close();
        }
    }

    public void actualizar(Inscripcion inscripcion) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(inscripcion);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<PuntoEstadisticaDTO> contarInscripcionesPorDia(Long eventoId) {
        EntityManager em = JpaUtil.crearEntityManager();
        List<LocalDateTime> fechas;
        try {
            fechas = em.createQuery(
                            "SELECT i.fechaInscripcion FROM Inscripcion i WHERE i.evento.id = :eventoId",
                            LocalDateTime.class)
                    .setParameter("eventoId", eventoId)
                    .getResultList();
        } finally {
            em.close();
        }

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Map<String, Long> porDia = new TreeMap<>();
        for (LocalDateTime fecha : fechas) {
            String etiqueta = fecha.toLocalDate().format(formato);
            porDia.merge(etiqueta, 1L, Long::sum);
        }
        return porDia.entrySet().stream()
                .map(entrada -> new PuntoEstadisticaDTO(entrada.getKey(), entrada.getValue()))
                .collect(Collectors.toList());
    }
}

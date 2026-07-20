package edu.pucmm.eventosacademicos.dao;

import edu.pucmm.eventosacademicos.modelo.EstadoEvento;
import edu.pucmm.eventosacademicos.modelo.Evento;
import edu.pucmm.eventosacademicos.util.JpaUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class EventoDao {

    public Evento buscarPorId(Long id) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.find(Evento.class, id);
        } finally {
            em.close();
        }
    }

    public List<Evento> listarTodos() {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery("SELECT e FROM Evento e ORDER BY e.fechaHora DESC", Evento.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Evento> listarPublicados() {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery(
                            "SELECT e FROM Evento e WHERE e.estado = :estado ORDER BY e.fechaHora", Evento.class)
                    .setParameter("estado", EstadoEvento.PUBLICADO)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Evento> listarPorOrganizador(Long organizadorId) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery(
                            "SELECT e FROM Evento e WHERE e.organizador.id = :organizadorId ORDER BY e.fechaHora DESC",
                            Evento.class)
                    .setParameter("organizadorId", organizadorId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Evento guardar(Evento evento) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(evento);
            em.getTransaction().commit();
            return evento;
        } finally {
            em.close();
        }
    }

    public void actualizar(Evento evento) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(evento);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void eliminar(Long id) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            em.getTransaction().begin();
            Evento evento = em.find(Evento.class, id);
            if (evento != null) {
                em.remove(evento);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public long contarEventos() {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery("SELECT COUNT(e) FROM Evento e", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }
}

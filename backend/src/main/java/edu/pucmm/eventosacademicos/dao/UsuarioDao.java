package edu.pucmm.eventosacademicos.dao;

import edu.pucmm.eventosacademicos.modelo.RolUsuario;
import edu.pucmm.eventosacademicos.modelo.Usuario;
import edu.pucmm.eventosacademicos.util.JpaUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class UsuarioDao {

    public Usuario buscarPorId(Long id) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public Usuario buscarPorEmail(String email) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            List<Usuario> resultado = em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                    .setParameter("email", email)
                    .getResultList();
            return resultado.isEmpty() ? null : resultado.get(0);
        } finally {
            em.close();
        }
    }

    public List<Usuario> listarTodos() {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u ORDER BY u.nombreCompleto", Usuario.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Usuario> listarPorRol(RolUsuario rol) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.rol = :rol ORDER BY u.nombreCompleto", Usuario.class)
                    .setParameter("rol", rol)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Usuario guardar(Usuario usuario) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
            return usuario;
        } finally {
            em.close();
        }
    }

    public void actualizar(Usuario usuario) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(usuario);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void eliminar(Long id) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            em.getTransaction().begin();
            Usuario usuario = em.find(Usuario.class, id);
            if (usuario != null) {
                em.remove(usuario);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public boolean existePorEmail(String email) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            Long total = em.createQuery(
                            "SELECT COUNT(u) FROM Usuario u WHERE u.email = :email", Long.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return total > 0;
        } finally {
            em.close();
        }
    }

    public long contarUsuarios() {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery("SELECT COUNT(u) FROM Usuario u", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }
}

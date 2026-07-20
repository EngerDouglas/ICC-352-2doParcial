package edu.pucmm.eventosacademicos.servicios;

import edu.pucmm.eventosacademicos.dao.EventoDao;
import edu.pucmm.eventosacademicos.dao.UsuarioDao;
import edu.pucmm.eventosacademicos.modelo.EstadoEvento;
import edu.pucmm.eventosacademicos.modelo.Evento;
import edu.pucmm.eventosacademicos.modelo.RolUsuario;
import edu.pucmm.eventosacademicos.modelo.Usuario;
import edu.pucmm.eventosacademicos.util.excepciones.AccesoNoAutorizadoException;
import edu.pucmm.eventosacademicos.util.excepciones.RecursoNoEncontradoException;

import java.time.LocalDateTime;
import java.util.List;

public class EventoServicio {

    private final EventoDao eventoDao = new EventoDao();
    private final UsuarioDao usuarioDao = new UsuarioDao();

    public Evento crearEvento(Evento evento, Long organizadorId) {
        Usuario organizador = usuarioDao.buscarPorId(organizadorId);
        if (organizador == null) {
            throw new RecursoNoEncontradoException("El organizador no existe.");
        }
        evento.setOrganizador(organizador);
        evento.setEstado(EstadoEvento.BORRADOR);
        evento.setFechaCreacion(LocalDateTime.now());
        return eventoDao.guardar(evento);
    }

    public Evento actualizarEvento(Long id, Evento datosActualizados, Long solicitanteId) throws AccesoNoAutorizadoException {
        Evento evento = obtenerEvento(id);
        exigirPuedeGestionar(evento, solicitanteId);
        evento.setTitulo(datosActualizados.getTitulo());
        evento.setDescripcion(datosActualizados.getDescripcion());
        evento.setFechaHora(datosActualizados.getFechaHora());
        evento.setLugar(datosActualizados.getLugar());
        evento.setCupoMaximo(datosActualizados.getCupoMaximo());
        evento.setFechaActualizacion(LocalDateTime.now());
        eventoDao.actualizar(evento);
        return evento;
    }

    public void publicarEvento(Long id, Long solicitanteId) throws AccesoNoAutorizadoException {
        Evento evento = obtenerEvento(id);
        exigirPuedeGestionar(evento, solicitanteId);
        evento.setEstado(EstadoEvento.PUBLICADO);
        evento.setFechaActualizacion(LocalDateTime.now());
        eventoDao.actualizar(evento);
    }

    public void despublicarEvento(Long id, Long solicitanteId) throws AccesoNoAutorizadoException {
        Evento evento = obtenerEvento(id);
        exigirPuedeGestionar(evento, solicitanteId);
        evento.setEstado(EstadoEvento.BORRADOR);
        evento.setFechaActualizacion(LocalDateTime.now());
        eventoDao.actualizar(evento);
    }

    public void cancelarEvento(Long id, Long solicitanteId) throws AccesoNoAutorizadoException {
        Evento evento = obtenerEvento(id);
        exigirPuedeGestionar(evento, solicitanteId);
        evento.setEstado(EstadoEvento.CANCELADO);
        evento.setFechaActualizacion(LocalDateTime.now());
        eventoDao.actualizar(evento);
    }

    public void eliminarEventoAdmin(Long id) {
        obtenerEvento(id);
        eventoDao.eliminar(id);
    }

    public Evento obtenerEvento(Long id) throws RecursoNoEncontradoException {
        Evento evento = eventoDao.buscarPorId(id);
        if (evento == null) {
            throw new RecursoNoEncontradoException("El evento no existe.");
        }
        return evento;
    }

    public List<Evento> listarEventosPublicados() {
        return eventoDao.listarPublicados();
    }

    public List<Evento> listarTodosLosEventos() {
        return eventoDao.listarTodos();
    }

    public List<Evento> listarEventosPorOrganizador(Long organizadorId) {
        return eventoDao.listarPorOrganizador(organizadorId);
    }

    private void exigirPuedeGestionar(Evento evento, Long solicitanteId) {
        if (evento.getOrganizador().getId().equals(solicitanteId)) {
            return;
        }
        Usuario solicitante = usuarioDao.buscarPorId(solicitanteId);
        if (solicitante != null && solicitante.getRol() == RolUsuario.ADMINISTRADOR) {
            return;
        }
        throw new AccesoNoAutorizadoException("No tiene permiso para gestionar este evento.");
    }
}

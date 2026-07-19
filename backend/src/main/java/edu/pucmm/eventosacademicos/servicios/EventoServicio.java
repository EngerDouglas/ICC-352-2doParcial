package edu.pucmm.eventosacademicos.servicios;

import edu.pucmm.eventosacademicos.modelo.Evento;
import edu.pucmm.eventosacademicos.util.excepciones.AccesoNoAutorizadoException;
import edu.pucmm.eventosacademicos.util.excepciones.RecursoNoEncontradoException;

import java.util.List;

public class EventoServicio {

    public Evento crearEvento(Evento evento, Long organizadorId) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public Evento actualizarEvento(Long id, Evento datosActualizados, Long solicitanteId) throws AccesoNoAutorizadoException {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public void publicarEvento(Long id, Long solicitanteId) throws AccesoNoAutorizadoException {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public void despublicarEvento(Long id, Long solicitanteId) throws AccesoNoAutorizadoException {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public void cancelarEvento(Long id, Long solicitanteId) throws AccesoNoAutorizadoException {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public void eliminarEventoAdmin(Long id) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public Evento obtenerEvento(Long id) throws RecursoNoEncontradoException {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public List<Evento> listarEventosPublicados() {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public List<Evento> listarTodosLosEventos() {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public List<Evento> listarEventosPorOrganizador(Long organizadorId) {
        throw new UnsupportedOperationException("TODO: implementar");
    }
}

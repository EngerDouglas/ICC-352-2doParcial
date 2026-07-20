package edu.pucmm.eventosacademicos.servicios;

import com.google.zxing.WriterException;
import edu.pucmm.eventosacademicos.dao.EventoDao;
import edu.pucmm.eventosacademicos.dao.InscripcionDao;
import edu.pucmm.eventosacademicos.dao.UsuarioDao;
import edu.pucmm.eventosacademicos.modelo.EstadoInscripcion;
import edu.pucmm.eventosacademicos.modelo.Evento;
import edu.pucmm.eventosacademicos.modelo.Inscripcion;
import edu.pucmm.eventosacademicos.modelo.RolUsuario;
import edu.pucmm.eventosacademicos.modelo.Usuario;
import edu.pucmm.eventosacademicos.util.QrUtil;
import edu.pucmm.eventosacademicos.util.excepciones.AccesoNoAutorizadoException;
import edu.pucmm.eventosacademicos.util.excepciones.CupoAgotadoException;
import edu.pucmm.eventosacademicos.util.excepciones.InscripcionDuplicadaException;
import edu.pucmm.eventosacademicos.util.excepciones.OperacionNoPermitidaException;
import edu.pucmm.eventosacademicos.util.excepciones.RecursoNoEncontradoException;
import edu.pucmm.eventosacademicos.util.excepciones.TokenInvalidoException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class InscripcionServicio {

    private final InscripcionDao inscripcionDao = new InscripcionDao();
    private final EventoDao eventoDao = new EventoDao();
    private final UsuarioDao usuarioDao = new UsuarioDao();

    public Inscripcion inscribirParticipante(Long eventoId, Long participanteId)
            throws CupoAgotadoException, InscripcionDuplicadaException {
        Evento evento = eventoDao.buscarPorId(eventoId);
        if (evento == null) {
            throw new RecursoNoEncontradoException("El evento no existe.");
        }
        Usuario participante = usuarioDao.buscarPorId(participanteId);
        if (participante == null) {
            throw new RecursoNoEncontradoException("El usuario no existe.");
        }

        if (inscripcionDao.buscarPorEventoYParticipante(eventoId, participanteId) != null) {
            throw new InscripcionDuplicadaException("Ya está inscrito en este evento.");
        }

        long inscritosActivos = inscripcionDao.contarInscripcionesActivasPorEvento(eventoId);
        if (inscritosActivos >= evento.getCupoMaximo()) {
            throw new CupoAgotadoException("El evento ya alcanzó su cupo máximo.");
        }

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setEvento(evento);
        inscripcion.setParticipante(participante);
        inscripcion.setFechaInscripcion(LocalDateTime.now());
        inscripcion.setEstado(EstadoInscripcion.ACTIVA);
        inscripcion.setTokenValidacionQr(UUID.randomUUID().toString());
        return inscripcionDao.guardar(inscripcion);
    }

    public void cancelarInscripcion(Long inscripcionId, Long participanteId) throws OperacionNoPermitidaException {
        Inscripcion inscripcion = inscripcionDao.buscarPorId(inscripcionId);
        if (inscripcion == null) {
            throw new RecursoNoEncontradoException("La inscripción no existe.");
        }
        if (!inscripcion.getParticipante().getId().equals(participanteId)) {
            throw new AccesoNoAutorizadoException("Esta inscripción no le pertenece.");
        }
        if (!inscripcion.getEvento().getFechaHora().isAfter(LocalDateTime.now())) {
            throw new OperacionNoPermitidaException("No se puede cancelar una inscripción después de la fecha del evento.");
        }
        inscripcion.setEstado(EstadoInscripcion.CANCELADA);
        inscripcionDao.actualizar(inscripcion);
    }

    public List<Inscripcion> listarPorEvento(Long eventoId) {
        return inscripcionDao.listarPorEvento(eventoId);
    }

    public List<Inscripcion> listarPorParticipante(Long participanteId) {
        return inscripcionDao.listarPorParticipante(participanteId);
    }

    public Inscripcion obtenerPorToken(String token) throws TokenInvalidoException {
        Inscripcion inscripcion = inscripcionDao.buscarPorToken(token);
        if (inscripcion == null) {
            throw new TokenInvalidoException("El código QR no es válido.");
        }
        return inscripcion;
    }

    public Inscripcion obtenerParaParticipante(Long inscripcionId, Long participanteId) {
        Inscripcion inscripcion = inscripcionDao.buscarPorId(inscripcionId);
        if (inscripcion == null) {
            throw new RecursoNoEncontradoException("La inscripción no existe.");
        }
        if (!inscripcion.getParticipante().getId().equals(participanteId)) {
            throw new AccesoNoAutorizadoException("Esta inscripción no le pertenece.");
        }
        return inscripcion;
    }

    public byte[] generarImagenQr(Long inscripcionId, Long solicitanteId) throws WriterException, IOException {
        Inscripcion inscripcion = inscripcionDao.buscarPorId(inscripcionId);
        if (inscripcion == null) {
            throw new RecursoNoEncontradoException("La inscripción no existe.");
        }
        if (!inscripcion.getParticipante().getId().equals(solicitanteId)) {
            Usuario solicitante = usuarioDao.buscarPorId(solicitanteId);
            boolean autorizado = solicitante != null
                    && (solicitante.getRol() == RolUsuario.ORGANIZADOR
                    || solicitante.getRol() == RolUsuario.ADMINISTRADOR);
            if (!autorizado) {
                throw new AccesoNoAutorizadoException("Esta inscripción no le pertenece.");
            }
        }
        String payload = QrUtil.construirPayload(
                inscripcion.getEvento().getId(),
                inscripcion.getParticipante().getId(),
                inscripcion.getTokenValidacionQr());
        return QrUtil.generarPng(payload, 300);
    }
}

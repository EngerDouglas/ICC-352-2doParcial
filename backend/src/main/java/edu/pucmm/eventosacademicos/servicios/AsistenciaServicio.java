package edu.pucmm.eventosacademicos.servicios;

import edu.pucmm.eventosacademicos.dao.AsistenciaDao;
import edu.pucmm.eventosacademicos.dao.UsuarioDao;
import edu.pucmm.eventosacademicos.modelo.Asistencia;
import edu.pucmm.eventosacademicos.modelo.EstadoInscripcion;
import edu.pucmm.eventosacademicos.modelo.Inscripcion;
import edu.pucmm.eventosacademicos.modelo.Usuario;
import edu.pucmm.eventosacademicos.util.excepciones.AsistenciaDuplicadaException;
import edu.pucmm.eventosacademicos.util.excepciones.TokenInvalidoException;

import java.time.LocalDateTime;

public class AsistenciaServicio {

    private final AsistenciaDao asistenciaDao = new AsistenciaDao();
    private final UsuarioDao usuarioDao = new UsuarioDao();
    private final InscripcionServicio inscripcionServicio = new InscripcionServicio();

    public Asistencia registrarAsistencia(String tokenEscaneado, Long organizadorId)
            throws TokenInvalidoException, AsistenciaDuplicadaException {
        Inscripcion inscripcion = inscripcionServicio.obtenerPorToken(tokenEscaneado);
        if (inscripcion.getEstado() != EstadoInscripcion.ACTIVA) {
            throw new TokenInvalidoException("La inscripción fue cancelada.");
        }
        if (asistenciaDao.existeAsistenciaParaInscripcion(inscripcion.getId())) {
            throw new AsistenciaDuplicadaException("Ya se registró la asistencia de este participante.");
        }

        Usuario organizador = usuarioDao.buscarPorId(organizadorId);

        Asistencia asistencia = new Asistencia();
        asistencia.setInscripcion(inscripcion);
        asistencia.setFechaHoraEscaneo(LocalDateTime.now());
        asistencia.setValidadoPor(organizador);
        return asistenciaDao.guardar(asistencia);
    }
}

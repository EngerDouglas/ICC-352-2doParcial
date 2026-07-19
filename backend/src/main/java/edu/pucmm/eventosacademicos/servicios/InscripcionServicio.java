package edu.pucmm.eventosacademicos.servicios;

import com.google.zxing.WriterException;
import edu.pucmm.eventosacademicos.modelo.Inscripcion;
import edu.pucmm.eventosacademicos.util.excepciones.CupoAgotadoException;
import edu.pucmm.eventosacademicos.util.excepciones.InscripcionDuplicadaException;
import edu.pucmm.eventosacademicos.util.excepciones.OperacionNoPermitidaException;
import edu.pucmm.eventosacademicos.util.excepciones.TokenInvalidoException;

import java.io.IOException;
import java.util.List;

public class InscripcionServicio {

    public Inscripcion inscribirParticipante(Long eventoId, Long participanteId)
            throws CupoAgotadoException, InscripcionDuplicadaException {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public void cancelarInscripcion(Long inscripcionId, Long participanteId) throws OperacionNoPermitidaException {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public List<Inscripcion> listarPorEvento(Long eventoId) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public List<Inscripcion> listarPorParticipante(Long participanteId) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public Inscripcion obtenerPorToken(String token) throws TokenInvalidoException {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public byte[] generarImagenQr(Long inscripcionId) throws WriterException, IOException {
        throw new UnsupportedOperationException("TODO: implementar");
    }
}

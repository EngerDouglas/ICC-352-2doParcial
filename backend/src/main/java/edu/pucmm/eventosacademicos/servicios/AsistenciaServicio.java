package edu.pucmm.eventosacademicos.servicios;

import edu.pucmm.eventosacademicos.modelo.Asistencia;
import edu.pucmm.eventosacademicos.util.excepciones.AsistenciaDuplicadaException;
import edu.pucmm.eventosacademicos.util.excepciones.TokenInvalidoException;

public class AsistenciaServicio {

    public Asistencia registrarAsistencia(String tokenEscaneado, Long organizadorId)
            throws TokenInvalidoException, AsistenciaDuplicadaException {
        throw new UnsupportedOperationException("TODO: implementar");
    }
}

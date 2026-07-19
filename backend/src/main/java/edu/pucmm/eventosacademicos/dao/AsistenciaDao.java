package edu.pucmm.eventosacademicos.dao;

import edu.pucmm.eventosacademicos.dto.PuntoEstadisticaDTO;
import edu.pucmm.eventosacademicos.modelo.Asistencia;

import java.util.List;

public class AsistenciaDao {

    public Asistencia buscarPorInscripcionId(Long inscripcionId) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public boolean existeAsistenciaParaInscripcion(Long inscripcionId) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public Asistencia guardar(Asistencia asistencia) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public long contarAsistenciasPorEvento(Long eventoId) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public List<PuntoEstadisticaDTO> contarAsistenciasPorHora(Long eventoId) {
        throw new UnsupportedOperationException("TODO: implementar");
    }
}

package edu.pucmm.eventosacademicos.dao;

import edu.pucmm.eventosacademicos.dto.PuntoEstadisticaDTO;
import edu.pucmm.eventosacademicos.modelo.Inscripcion;

import java.util.List;

public class InscripcionDao {

    public Inscripcion buscarPorId(Long id) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public Inscripcion buscarPorEventoYParticipante(Long eventoId, Long participanteId) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public Inscripcion buscarPorToken(String token) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public List<Inscripcion> listarPorEvento(Long eventoId) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public List<Inscripcion> listarPorParticipante(Long participanteId) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public long contarInscripcionesActivasPorEvento(Long eventoId) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public Inscripcion guardar(Inscripcion inscripcion) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public void actualizar(Inscripcion inscripcion) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public List<PuntoEstadisticaDTO> contarInscripcionesPorDia(Long eventoId) {
        throw new UnsupportedOperationException("TODO: implementar");
    }
}

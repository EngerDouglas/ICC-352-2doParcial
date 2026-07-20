package edu.pucmm.eventosacademicos.servicios;

import edu.pucmm.eventosacademicos.dao.AsistenciaDao;
import edu.pucmm.eventosacademicos.dao.InscripcionDao;
import edu.pucmm.eventosacademicos.dto.EstadisticasEventoDTO;

public class EstadisticasServicio {

    private final InscripcionDao inscripcionDao = new InscripcionDao();
    private final AsistenciaDao asistenciaDao = new AsistenciaDao();

    public EstadisticasEventoDTO obtenerEstadisticas(Long eventoId) {
        long totalInscritos = inscripcionDao.contarInscripcionesActivasPorEvento(eventoId);
        long totalAsistentes = asistenciaDao.contarAsistenciasPorEvento(eventoId);
        double porcentajeAsistencia = totalInscritos == 0 ? 0.0 : (totalAsistentes * 100.0) / totalInscritos;

        return new EstadisticasEventoDTO(
                totalInscritos,
                totalAsistentes,
                porcentajeAsistencia,
                inscripcionDao.contarInscripcionesPorDia(eventoId),
                asistenciaDao.contarAsistenciasPorHora(eventoId));
    }
}

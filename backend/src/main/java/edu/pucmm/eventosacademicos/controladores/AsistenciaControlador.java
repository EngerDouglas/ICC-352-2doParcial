package edu.pucmm.eventosacademicos.controladores;

import edu.pucmm.eventosacademicos.dto.RespuestaEscaneoDTO;
import edu.pucmm.eventosacademicos.dto.SolicitudEscaneoDTO;
import edu.pucmm.eventosacademicos.modelo.Asistencia;
import edu.pucmm.eventosacademicos.modelo.Usuario;
import edu.pucmm.eventosacademicos.servicios.AsistenciaServicio;
import edu.pucmm.eventosacademicos.util.SessionUtil;
import edu.pucmm.eventosacademicos.util.excepciones.AsistenciaDuplicadaException;
import edu.pucmm.eventosacademicos.util.excepciones.TokenInvalidoException;
import io.javalin.http.Context;

public class AsistenciaControlador {

    private static final AsistenciaServicio asistenciaServicio = new AsistenciaServicio();

    public static void validarEscaneo(Context ctx) throws Exception {
        SolicitudEscaneoDTO solicitud = ctx.bodyAsClass(SolicitudEscaneoDTO.class);
        Usuario organizador = SessionUtil.usuarioActual(ctx);
        try {
            Asistencia asistencia = asistenciaServicio.registrarAsistencia(solicitud.getTokenEscaneado(), organizador.getId());
            ctx.json(new RespuestaEscaneoDTO(
                    true,
                    "Asistencia registrada.",
                    asistencia.getInscripcion().getParticipante().getNombreCompleto(),
                    asistencia.getInscripcion().getEvento().getTitulo()));
        } catch (TokenInvalidoException | AsistenciaDuplicadaException e) {
            ctx.json(new RespuestaEscaneoDTO(false, e.getMessage(), null, null));
        }
    }
}

package edu.pucmm.eventosacademicos.controladores;

import edu.pucmm.eventosacademicos.dao.InscripcionDao;
import edu.pucmm.eventosacademicos.dto.InscripcionDTO;
import edu.pucmm.eventosacademicos.dto.RespuestaApiDTO;
import edu.pucmm.eventosacademicos.modelo.Inscripcion;
import edu.pucmm.eventosacademicos.modelo.Usuario;
import edu.pucmm.eventosacademicos.servicios.InscripcionServicio;
import edu.pucmm.eventosacademicos.util.SessionUtil;
import io.javalin.http.Context;

import java.util.List;
import java.util.stream.Collectors;

public class InscripcionControlador {

    private static final InscripcionServicio inscripcionServicio = new InscripcionServicio();
    private static final InscripcionDao inscripcionDao = new InscripcionDao();

    public static void inscribirse(Context ctx) throws Exception {
        Long eventoId = Long.valueOf(ctx.pathParam("id"));
        Usuario usuario = SessionUtil.usuarioActual(ctx);
        Inscripcion inscripcion = inscripcionServicio.inscribirParticipante(eventoId, usuario.getId());
        ctx.json(aDto(inscripcion));
    }

    public static void cancelarInscripcion(Context ctx) throws Exception {
        Long id = Long.valueOf(ctx.pathParam("id"));
        Usuario usuario = SessionUtil.usuarioActual(ctx);
        inscripcionServicio.cancelarInscripcion(id, usuario.getId());
        ctx.json(new RespuestaApiDTO(true, null));
    }

    public static void listarMisInscripciones(Context ctx) throws Exception {
        Usuario usuario = SessionUtil.usuarioActual(ctx);
        List<Inscripcion> inscripciones = inscripcionServicio.listarPorParticipante(usuario.getId());
        ctx.json(inscripciones.stream().map(InscripcionControlador::aDto).collect(Collectors.toList()));
    }

    public static void obtenerDetalle(Context ctx) throws Exception {
        Long id = Long.valueOf(ctx.pathParam("id"));
        Usuario usuario = SessionUtil.usuarioActual(ctx);
        Inscripcion inscripcion = inscripcionServicio.obtenerParaParticipante(id, usuario.getId());
        ctx.json(aDto(inscripcion));
    }

    public static void obtenerCodigoQr(Context ctx) throws Exception {
        Long id = Long.valueOf(ctx.pathParam("id"));
        Usuario usuario = SessionUtil.usuarioActual(ctx);
        byte[] imagen = inscripcionServicio.generarImagenQr(id, usuario.getId());
        ctx.contentType("image/png").result(imagen);
    }

    private static InscripcionDTO aDto(Inscripcion inscripcion) {
        long inscritos = inscripcionDao.contarInscripcionesActivasPorEvento(inscripcion.getEvento().getId());
        return InscripcionDTO.desde(inscripcion, inscritos);
    }
}

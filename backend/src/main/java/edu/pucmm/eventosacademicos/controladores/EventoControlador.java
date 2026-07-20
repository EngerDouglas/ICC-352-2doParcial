package edu.pucmm.eventosacademicos.controladores;

import edu.pucmm.eventosacademicos.dao.InscripcionDao;
import edu.pucmm.eventosacademicos.dto.EventoResumenDTO;
import edu.pucmm.eventosacademicos.dto.RespuestaApiDTO;
import edu.pucmm.eventosacademicos.dto.SolicitudEventoDTO;
import edu.pucmm.eventosacademicos.modelo.Evento;
import edu.pucmm.eventosacademicos.modelo.Usuario;
import edu.pucmm.eventosacademicos.servicios.EventoServicio;
import edu.pucmm.eventosacademicos.util.SessionUtil;
import io.javalin.http.Context;

import java.util.List;
import java.util.stream.Collectors;

public class EventoControlador {

    private static final EventoServicio eventoServicio = new EventoServicio();
    private static final InscripcionDao inscripcionDao = new InscripcionDao();

    public static void listarPublicados(Context ctx) throws Exception {
        List<Evento> eventos = eventoServicio.listarEventosPublicados();
        ctx.json(aResumenes(eventos));
    }

    public static void obtenerDetalle(Context ctx) throws Exception {
        Long id = Long.valueOf(ctx.pathParam("id"));
        Evento evento = eventoServicio.obtenerEvento(id);
        ctx.json(aResumen(evento));
    }

    public static void listarPropios(Context ctx) throws Exception {
        Usuario usuario = SessionUtil.usuarioActual(ctx);
        List<Evento> eventos = eventoServicio.listarEventosPorOrganizador(usuario.getId());
        ctx.json(aResumenes(eventos));
    }

    public static void crearEvento(Context ctx) throws Exception {
        Usuario usuario = SessionUtil.usuarioActual(ctx);
        Evento evento = aEntidad(ctx.bodyAsClass(SolicitudEventoDTO.class));
        Evento creado = eventoServicio.crearEvento(evento, usuario.getId());
        ctx.json(aResumen(creado));
    }

    public static void actualizarEvento(Context ctx) throws Exception {
        Long id = Long.valueOf(ctx.pathParam("id"));
        Usuario usuario = SessionUtil.usuarioActual(ctx);
        Evento datos = aEntidad(ctx.bodyAsClass(SolicitudEventoDTO.class));
        Evento actualizado = eventoServicio.actualizarEvento(id, datos, usuario.getId());
        ctx.json(aResumen(actualizado));
    }

    public static void publicarEvento(Context ctx) throws Exception {
        Long id = Long.valueOf(ctx.pathParam("id"));
        Usuario usuario = SessionUtil.usuarioActual(ctx);
        eventoServicio.publicarEvento(id, usuario.getId());
        ctx.json(new RespuestaApiDTO(true, null));
    }

    public static void despublicarEvento(Context ctx) throws Exception {
        Long id = Long.valueOf(ctx.pathParam("id"));
        Usuario usuario = SessionUtil.usuarioActual(ctx);
        eventoServicio.despublicarEvento(id, usuario.getId());
        ctx.json(new RespuestaApiDTO(true, null));
    }

    public static void cancelarEvento(Context ctx) throws Exception {
        Long id = Long.valueOf(ctx.pathParam("id"));
        Usuario usuario = SessionUtil.usuarioActual(ctx);
        eventoServicio.cancelarEvento(id, usuario.getId());
        ctx.json(new RespuestaApiDTO(true, null));
    }

    private static Evento aEntidad(SolicitudEventoDTO solicitud) {
        Evento evento = new Evento();
        evento.setTitulo(solicitud.getTitulo());
        evento.setDescripcion(solicitud.getDescripcion());
        evento.setFechaHora(solicitud.getFechaHora());
        evento.setLugar(solicitud.getLugar());
        evento.setCupoMaximo(solicitud.getCupoMaximo());
        return evento;
    }

    private static EventoResumenDTO aResumen(Evento evento) {
        long inscritos = inscripcionDao.contarInscripcionesActivasPorEvento(evento.getId());
        return EventoResumenDTO.desde(evento, inscritos);
    }

    private static List<EventoResumenDTO> aResumenes(List<Evento> eventos) {
        return eventos.stream().map(EventoControlador::aResumen).collect(Collectors.toList());
    }
}

package edu.pucmm.eventosacademicos.controladores;

import edu.pucmm.eventosacademicos.modelo.Evento;
import edu.pucmm.eventosacademicos.modelo.RolUsuario;
import edu.pucmm.eventosacademicos.modelo.Usuario;
import edu.pucmm.eventosacademicos.servicios.EstadisticasServicio;
import edu.pucmm.eventosacademicos.servicios.EventoServicio;
import edu.pucmm.eventosacademicos.util.SessionUtil;
import edu.pucmm.eventosacademicos.util.excepciones.AccesoNoAutorizadoException;
import io.javalin.http.Context;

public class EstadisticasControlador {

    private static final EstadisticasServicio estadisticasServicio = new EstadisticasServicio();
    private static final EventoServicio eventoServicio = new EventoServicio();

    public static void obtenerEstadisticas(Context ctx) throws Exception {
        Long eventoId = Long.valueOf(ctx.pathParam("id"));
        Usuario usuario = SessionUtil.usuarioActual(ctx);
        Evento evento = eventoServicio.obtenerEvento(eventoId);
        boolean esDueno = evento.getOrganizador().getId().equals(usuario.getId());
        if (!esDueno && usuario.getRol() != RolUsuario.ADMINISTRADOR) {
            throw new AccesoNoAutorizadoException("No tiene permiso para ver las estadísticas de este evento.");
        }
        ctx.json(estadisticasServicio.obtenerEstadisticas(eventoId));
    }
}

package edu.pucmm.eventosacademicos.controladores;

import edu.pucmm.eventosacademicos.dto.RespuestaApiDTO;
import edu.pucmm.eventosacademicos.dto.SolicitudLoginDTO;
import edu.pucmm.eventosacademicos.dto.SolicitudRegistroDTO;
import edu.pucmm.eventosacademicos.dto.UsuarioResumenDTO;
import edu.pucmm.eventosacademicos.modelo.Usuario;
import edu.pucmm.eventosacademicos.servicios.AuthServicio;
import edu.pucmm.eventosacademicos.util.SessionUtil;
import io.javalin.http.Context;

public class AuthControlador {

    private static final AuthServicio authServicio = new AuthServicio();

    public static void procesarLogin(Context ctx) throws Exception {
        SolicitudLoginDTO solicitud = ctx.bodyAsClass(SolicitudLoginDTO.class);
        Usuario usuario = authServicio.autenticar(solicitud.getEmail(), solicitud.getPassword());
        SessionUtil.iniciarSesion(ctx, usuario);
        ctx.json(UsuarioResumenDTO.desde(usuario));
    }

    public static void procesarLogout(Context ctx) throws Exception {
        SessionUtil.cerrarSesion(ctx);
        ctx.json(new RespuestaApiDTO(true, "Sesión cerrada."));
    }

    public static void procesarRegistro(Context ctx) throws Exception {
        SolicitudRegistroDTO solicitud = ctx.bodyAsClass(SolicitudRegistroDTO.class);
        Usuario usuario = authServicio.registrarParticipante(
                solicitud.getNombreCompleto(), solicitud.getEmail(), solicitud.getPassword());
        SessionUtil.iniciarSesion(ctx, usuario);
        ctx.json(UsuarioResumenDTO.desde(usuario));
    }

    public static void obtenerUsuarioActual(Context ctx) throws Exception {
        Usuario usuario = SessionUtil.usuarioActual(ctx);
        if (usuario == null) {
            ctx.status(401).json(new RespuestaApiDTO(false, "No hay sesión activa."));
            return;
        }
        ctx.json(UsuarioResumenDTO.desde(usuario));
    }
}

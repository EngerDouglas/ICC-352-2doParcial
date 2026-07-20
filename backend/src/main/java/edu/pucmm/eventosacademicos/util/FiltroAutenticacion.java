package edu.pucmm.eventosacademicos.util;

import edu.pucmm.eventosacademicos.modelo.RolUsuario;
import edu.pucmm.eventosacademicos.modelo.Usuario;
import edu.pucmm.eventosacademicos.util.excepciones.AccesoNoAutorizadoException;
import edu.pucmm.eventosacademicos.util.excepciones.UsuarioBloqueadoException;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;

public class FiltroAutenticacion {

    private FiltroAutenticacion() {
    }

    public static void exigirAutenticacion(Context ctx) {
        if (ctx.method() == HandlerType.OPTIONS) {
            return;
        }
        Usuario usuario = SessionUtil.usuarioActual(ctx);
        if (usuario == null) {
            throw new AccesoNoAutorizadoException("Debe iniciar sesión para acceder a este recurso.");
        }
        exigirNoBloqueado(ctx);
    }

    public static void exigirRol(Context ctx, RolUsuario... permitidos) {
        if (ctx.method() == HandlerType.OPTIONS) {
            return;
        }
        exigirAutenticacion(ctx);
        Usuario usuario = SessionUtil.usuarioActual(ctx);
        for (RolUsuario rol : permitidos) {
            if (usuario.getRol() == rol) {
                return;
            }
        }
        throw new AccesoNoAutorizadoException("No tiene permiso para acceder a este recurso.");
    }

    public static void exigirNoBloqueado(Context ctx) {
        Usuario usuario = SessionUtil.usuarioActual(ctx);
        if (usuario != null && usuario.isBloqueado()) {
            throw new UsuarioBloqueadoException("Su cuenta ha sido bloqueada.");
        }
    }
}

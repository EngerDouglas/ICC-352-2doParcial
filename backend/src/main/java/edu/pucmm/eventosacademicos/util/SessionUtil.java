package edu.pucmm.eventosacademicos.util;

import edu.pucmm.eventosacademicos.modelo.Usuario;
import io.javalin.http.Context;

public class SessionUtil {

    private static final String ATRIBUTO_USUARIO = "usuarioActual";

    private SessionUtil() {
    }

    public static Usuario usuarioActual(Context ctx) {
        return ctx.sessionAttribute(ATRIBUTO_USUARIO);
    }

    public static void iniciarSesion(Context ctx, Usuario usuario) {
        ctx.sessionAttribute(ATRIBUTO_USUARIO, usuario);
    }

    public static void cerrarSesion(Context ctx) {
        ctx.sessionAttribute(ATRIBUTO_USUARIO, null);
    }
}

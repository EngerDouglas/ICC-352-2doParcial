package edu.pucmm.eventosacademicos.util;

import edu.pucmm.eventosacademicos.dao.UsuarioDao;
import edu.pucmm.eventosacademicos.modelo.Usuario;
import io.javalin.http.Context;

public class SessionUtil {

    private static final String ATRIBUTO_USUARIO_ID = "usuarioId";
    private static final UsuarioDao usuarioDao = new UsuarioDao();

    private SessionUtil() {
    }

    public static Usuario usuarioActual(Context ctx) {
        Long id = ctx.sessionAttribute(ATRIBUTO_USUARIO_ID);
        return id == null ? null : usuarioDao.buscarPorId(id);
    }

    public static void iniciarSesion(Context ctx, Usuario usuario) {
        ctx.sessionAttribute(ATRIBUTO_USUARIO_ID, usuario.getId());
    }

    public static void cerrarSesion(Context ctx) {
        ctx.sessionAttribute(ATRIBUTO_USUARIO_ID, null);
    }
}

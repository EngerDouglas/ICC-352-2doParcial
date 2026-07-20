package edu.pucmm.eventosacademicos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.pucmm.eventosacademicos.controladores.AdminControlador;
import edu.pucmm.eventosacademicos.controladores.AsistenciaControlador;
import edu.pucmm.eventosacademicos.controladores.AuthControlador;
import edu.pucmm.eventosacademicos.controladores.EstadisticasControlador;
import edu.pucmm.eventosacademicos.controladores.EventoControlador;
import edu.pucmm.eventosacademicos.controladores.InscripcionControlador;
import edu.pucmm.eventosacademicos.dto.RespuestaApiDTO;
import edu.pucmm.eventosacademicos.modelo.RolUsuario;
import edu.pucmm.eventosacademicos.util.FiltroAutenticacion;
import edu.pucmm.eventosacademicos.util.H2ServerManager;
import edu.pucmm.eventosacademicos.util.JpaUtil;
import edu.pucmm.eventosacademicos.util.SeedInicial;
import edu.pucmm.eventosacademicos.util.excepciones.AccesoNoAutorizadoException;
import edu.pucmm.eventosacademicos.util.excepciones.AsistenciaDuplicadaException;
import edu.pucmm.eventosacademicos.util.excepciones.CredencialesInvalidasException;
import edu.pucmm.eventosacademicos.util.excepciones.CupoAgotadoException;
import edu.pucmm.eventosacademicos.util.excepciones.EmailDuplicadoException;
import edu.pucmm.eventosacademicos.util.excepciones.InscripcionDuplicadaException;
import edu.pucmm.eventosacademicos.util.excepciones.OperacionNoPermitidaException;
import edu.pucmm.eventosacademicos.util.excepciones.RecursoNoEncontradoException;
import edu.pucmm.eventosacademicos.util.excepciones.TokenInvalidoException;
import edu.pucmm.eventosacademicos.util.excepciones.UsuarioBloqueadoException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.json.JavalinJackson;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {

    public static void main(String[] args) {
        H2ServerManager.iniciar();
        JpaUtil.obtenerFabrica();
        SeedInicial.ejecutar();

        Javalin app = Javalin.create(config -> {
            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            config.jsonMapper(new JavalinJackson(mapper, false));

            // Backend puramente API (JSON): el frontend vive aparte en /frontend
            // y consume estas rutas por fetch. En desarrollo corren en distinto
            // origen/puerto, por eso se habilita CORS con credenciales (sesión
            // manejada por cookie).
            config.bundledPlugins.enableCors(cors -> cors.addRule(rule -> {
                rule.allowCredentials = true;
                rule.reflectClientOrigin = true;
            }));

            config.routes.exception(RecursoNoEncontradoException.class, (e, ctx) -> error(ctx, 404, e.getMessage()));
            config.routes.exception(CredencialesInvalidasException.class, (e, ctx) -> error(ctx, 401, e.getMessage()));
            config.routes.exception(AccesoNoAutorizadoException.class, (e, ctx) -> error(ctx, 401, e.getMessage()));
            config.routes.exception(UsuarioBloqueadoException.class, (e, ctx) -> error(ctx, 403, e.getMessage()));
            config.routes.exception(EmailDuplicadoException.class, (e, ctx) -> error(ctx, 409, e.getMessage()));
            config.routes.exception(CupoAgotadoException.class, (e, ctx) -> error(ctx, 409, e.getMessage()));
            config.routes.exception(InscripcionDuplicadaException.class, (e, ctx) -> error(ctx, 409, e.getMessage()));
            config.routes.exception(AsistenciaDuplicadaException.class, (e, ctx) -> error(ctx, 409, e.getMessage()));
            config.routes.exception(TokenInvalidoException.class, (e, ctx) -> error(ctx, 400, e.getMessage()));
            config.routes.exception(OperacionNoPermitidaException.class, (e, ctx) -> error(ctx, 400, e.getMessage()));
            config.routes.exception(NumberFormatException.class, (e, ctx) -> error(ctx, 400, "Parámetro inválido."));
            config.routes.exception(Exception.class, (e, ctx) -> {
                e.printStackTrace();
                error(ctx, 500, "Ocurrió un error inesperado en el servidor.");
            });

            config.routes.apiBuilder(() -> {
                path("/api", () -> {
                    before("/organizador/*", ctx -> FiltroAutenticacion.exigirRol(ctx, RolUsuario.ORGANIZADOR, RolUsuario.ADMINISTRADOR));
                    before("/admin/*", ctx -> FiltroAutenticacion.exigirRol(ctx, RolUsuario.ADMINISTRADOR));
                    before("/inscripciones/*", FiltroAutenticacion::exigirAutenticacion);
                    before("/participante/*", FiltroAutenticacion::exigirAutenticacion);
                    before("/asistencia/*", ctx -> FiltroAutenticacion.exigirRol(ctx, RolUsuario.ORGANIZADOR, RolUsuario.ADMINISTRADOR));
                    before("/eventos/{id}/estadisticas", ctx -> FiltroAutenticacion.exigirRol(ctx, RolUsuario.ORGANIZADOR, RolUsuario.ADMINISTRADOR));
                    before("/eventos/{id}/inscripciones", FiltroAutenticacion::exigirAutenticacion);

                    path("/auth", () -> {
                        post("/login", AuthControlador::procesarLogin);
                        post("/logout", AuthControlador::procesarLogout);
                        post("/registro", AuthControlador::procesarRegistro);
                        get("/me", AuthControlador::obtenerUsuarioActual);
                    });

                    get("/eventos", EventoControlador::listarPublicados);
                    get("/eventos/{id}", EventoControlador::obtenerDetalle);
                    get("/eventos/{id}/estadisticas", EstadisticasControlador::obtenerEstadisticas);
                    post("/eventos/{id}/inscripciones", InscripcionControlador::inscribirse);

                    path("/organizador/eventos", () -> {
                        get(EventoControlador::listarPropios);
                        post(EventoControlador::crearEvento);
                        post("/{id}", EventoControlador::actualizarEvento);
                        post("/{id}/publicar", EventoControlador::publicarEvento);
                        post("/{id}/despublicar", EventoControlador::despublicarEvento);
                        post("/{id}/cancelar", EventoControlador::cancelarEvento);
                    });

                    path("/inscripciones", () -> {
                        get("/{id}", InscripcionControlador::obtenerDetalle);
                        get("/{id}/qr", InscripcionControlador::obtenerCodigoQr);
                        post("/{id}/cancelar", InscripcionControlador::cancelarInscripcion);
                    });
                    get("/participante/inscripciones", InscripcionControlador::listarMisInscripciones);

                    post("/asistencia/validar", AsistenciaControlador::validarEscaneo);

                    path("/admin", () -> {
                        get("/usuarios", AdminControlador::listarUsuarios);
                        post("/usuarios/{id}/bloquear", AdminControlador::bloquearUsuario);
                        post("/usuarios/{id}/otorgar-organizador", AdminControlador::otorgarOrganizador);
                        post("/usuarios/{id}/revocar-organizador", AdminControlador::revocarOrganizador);
                        post("/usuarios/{id}/eliminar", AdminControlador::eliminarUsuario);
                        get("/eventos", AdminControlador::listarEventosAdmin);
                        post("/eventos/{id}/eliminar", AdminControlador::eliminarEventoAdmin);
                    });
                });
            });
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            JpaUtil.cerrar();
            H2ServerManager.detener();
        }));

        app.start(Integer.parseInt(System.getenv().getOrDefault("PORT", "7070")));
    }

    private static void error(Context ctx, int status, String mensaje) {
        ctx.status(status).json(new RespuestaApiDTO(false, mensaje));
    }
}

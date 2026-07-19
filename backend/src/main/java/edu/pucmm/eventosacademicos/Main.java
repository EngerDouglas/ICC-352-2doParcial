package edu.pucmm.eventosacademicos;

import edu.pucmm.eventosacademicos.controladores.AdminControlador;
import edu.pucmm.eventosacademicos.controladores.AsistenciaControlador;
import edu.pucmm.eventosacademicos.controladores.AuthControlador;
import edu.pucmm.eventosacademicos.controladores.EstadisticasControlador;
import edu.pucmm.eventosacademicos.controladores.EventoControlador;
import edu.pucmm.eventosacademicos.controladores.InscripcionControlador;
import edu.pucmm.eventosacademicos.modelo.RolUsuario;
import edu.pucmm.eventosacademicos.util.FiltroAutenticacion;
import edu.pucmm.eventosacademicos.util.H2ServerManager;
import edu.pucmm.eventosacademicos.util.JpaUtil;
import edu.pucmm.eventosacademicos.util.SeedInicial;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {

    public static void main(String[] args) {
        H2ServerManager.iniciar();
        JpaUtil.obtenerFabrica();
        SeedInicial.ejecutar();

        Javalin app = Javalin.create(config -> {
            // Backend puramente API (JSON): el frontend vive aparte en /frontend
            // y consume estas rutas por fetch. En desarrollo corren en distinto
            // origen/puerto, por eso se habilita CORS con credenciales (sesión
            // manejada por cookie).
            config.bundledPlugins.enableCors(cors -> cors.addRule(rule -> {
                rule.allowCredentials = true;
                rule.reflectClientOrigin = true;
            }));

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
}

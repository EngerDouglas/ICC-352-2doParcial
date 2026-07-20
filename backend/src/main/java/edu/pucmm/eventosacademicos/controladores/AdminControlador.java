package edu.pucmm.eventosacademicos.controladores;

import edu.pucmm.eventosacademicos.dto.EventoResumenDTO;
import edu.pucmm.eventosacademicos.dto.RespuestaApiDTO;
import edu.pucmm.eventosacademicos.dto.UsuarioResumenDTO;
import edu.pucmm.eventosacademicos.modelo.Evento;
import edu.pucmm.eventosacademicos.modelo.Usuario;
import edu.pucmm.eventosacademicos.servicios.EventoServicio;
import edu.pucmm.eventosacademicos.servicios.UsuarioServicio;
import io.javalin.http.Context;

import java.util.List;
import java.util.stream.Collectors;

public class AdminControlador {

    private static final UsuarioServicio usuarioServicio = new UsuarioServicio();
    private static final EventoServicio eventoServicio = new EventoServicio();

    public static void listarUsuarios(Context ctx) throws Exception {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        ctx.json(usuarios.stream().map(UsuarioResumenDTO::desde).collect(Collectors.toList()));
    }

    public static void bloquearUsuario(Context ctx) throws Exception {
        Long id = Long.valueOf(ctx.pathParam("id"));
        usuarioServicio.bloquearUsuario(id);
        ctx.json(new RespuestaApiDTO(true, null));
    }

    public static void otorgarOrganizador(Context ctx) throws Exception {
        Long id = Long.valueOf(ctx.pathParam("id"));
        usuarioServicio.otorgarRolOrganizador(id);
        ctx.json(new RespuestaApiDTO(true, null));
    }

    public static void revocarOrganizador(Context ctx) throws Exception {
        Long id = Long.valueOf(ctx.pathParam("id"));
        usuarioServicio.revocarRolOrganizador(id);
        ctx.json(new RespuestaApiDTO(true, null));
    }

    public static void eliminarUsuario(Context ctx) throws Exception {
        Long id = Long.valueOf(ctx.pathParam("id"));
        usuarioServicio.eliminarUsuario(id);
        ctx.json(new RespuestaApiDTO(true, null));
    }

    public static void listarEventosAdmin(Context ctx) throws Exception {
        List<Evento> eventos = eventoServicio.listarTodosLosEventos();
        ctx.json(eventos.stream().map(evento -> EventoResumenDTO.desde(evento, 0)).collect(Collectors.toList()));
    }

    public static void eliminarEventoAdmin(Context ctx) throws Exception {
        Long id = Long.valueOf(ctx.pathParam("id"));
        eventoServicio.eliminarEventoAdmin(id);
        ctx.json(new RespuestaApiDTO(true, null));
    }
}

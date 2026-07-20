package edu.pucmm.eventosacademicos.servicios;

import edu.pucmm.eventosacademicos.dao.UsuarioDao;
import edu.pucmm.eventosacademicos.modelo.RolUsuario;
import edu.pucmm.eventosacademicos.modelo.Usuario;
import edu.pucmm.eventosacademicos.util.excepciones.OperacionNoPermitidaException;
import edu.pucmm.eventosacademicos.util.excepciones.RecursoNoEncontradoException;

import java.util.List;

public class UsuarioServicio {

    private final UsuarioDao usuarioDao = new UsuarioDao();

    public List<Usuario> listarUsuarios() {
        return usuarioDao.listarTodos();
    }

    public Usuario obtenerUsuario(Long id) throws RecursoNoEncontradoException {
        Usuario usuario = usuarioDao.buscarPorId(id);
        if (usuario == null) {
            throw new RecursoNoEncontradoException("El usuario no existe.");
        }
        return usuario;
    }

    public void bloquearUsuario(Long id) {
        Usuario usuario = obtenerUsuario(id);
        if (usuario.isNoEliminable()) {
            throw new OperacionNoPermitidaException("No se puede bloquear al administrador.");
        }
        usuario.setBloqueado(!usuario.isBloqueado());
        usuarioDao.actualizar(usuario);
    }

    public void otorgarRolOrganizador(Long id) {
        Usuario usuario = obtenerUsuario(id);
        if (usuario.getRol() != RolUsuario.PARTICIPANTE) {
            throw new OperacionNoPermitidaException("Solo se puede otorgar el rol de organizador a un participante.");
        }
        usuario.setRol(RolUsuario.ORGANIZADOR);
        usuarioDao.actualizar(usuario);
    }

    public void revocarRolOrganizador(Long id) {
        Usuario usuario = obtenerUsuario(id);
        if (usuario.getRol() != RolUsuario.ORGANIZADOR) {
            throw new OperacionNoPermitidaException("El usuario no tiene el rol de organizador.");
        }
        usuario.setRol(RolUsuario.PARTICIPANTE);
        usuarioDao.actualizar(usuario);
    }

    public void eliminarUsuario(Long id) throws OperacionNoPermitidaException {
        Usuario usuario = obtenerUsuario(id);
        if (usuario.isNoEliminable()) {
            throw new OperacionNoPermitidaException("No se puede eliminar al administrador.");
        }
        usuarioDao.eliminar(id);
    }
}

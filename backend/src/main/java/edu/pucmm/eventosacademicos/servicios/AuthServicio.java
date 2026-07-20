package edu.pucmm.eventosacademicos.servicios;

import edu.pucmm.eventosacademicos.dao.UsuarioDao;
import edu.pucmm.eventosacademicos.modelo.RolUsuario;
import edu.pucmm.eventosacademicos.modelo.Usuario;
import edu.pucmm.eventosacademicos.util.PasswordUtil;
import edu.pucmm.eventosacademicos.util.excepciones.CredencialesInvalidasException;
import edu.pucmm.eventosacademicos.util.excepciones.EmailDuplicadoException;
import edu.pucmm.eventosacademicos.util.excepciones.UsuarioBloqueadoException;

import java.time.LocalDateTime;

public class AuthServicio {

    private final UsuarioDao usuarioDao = new UsuarioDao();

    public Usuario autenticar(String email, String password) throws CredencialesInvalidasException {
        Usuario usuario = usuarioDao.buscarPorEmail(email);
        if (usuario == null || !PasswordUtil.verificar(password, usuario.getPasswordHash())) {
            throw new CredencialesInvalidasException("Correo o contraseña incorrectos.");
        }
        if (usuario.isBloqueado()) {
            throw new UsuarioBloqueadoException("Su cuenta ha sido bloqueada.");
        }
        return usuario;
    }

    public Usuario registrarParticipante(String nombreCompleto, String email, String password) {
        if (usuarioDao.existePorEmail(email)) {
            throw new EmailDuplicadoException("Ya existe una cuenta con ese correo electrónico.");
        }

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(nombreCompleto);
        usuario.setEmail(email);
        usuario.setPasswordHash(PasswordUtil.hashear(password));
        usuario.setRol(RolUsuario.PARTICIPANTE);
        usuario.setBloqueado(false);
        usuario.setNoEliminable(false);
        usuario.setFechaCreacion(LocalDateTime.now());
        return usuarioDao.guardar(usuario);
    }
}

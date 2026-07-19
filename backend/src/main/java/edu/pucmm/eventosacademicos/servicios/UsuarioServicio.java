package edu.pucmm.eventosacademicos.servicios;

import edu.pucmm.eventosacademicos.modelo.Usuario;
import edu.pucmm.eventosacademicos.util.excepciones.OperacionNoPermitidaException;
import edu.pucmm.eventosacademicos.util.excepciones.RecursoNoEncontradoException;

import java.util.List;

public class UsuarioServicio {

    public List<Usuario> listarUsuarios() {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public Usuario obtenerUsuario(Long id) throws RecursoNoEncontradoException {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public void bloquearUsuario(Long id) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public void otorgarRolOrganizador(Long id) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public void revocarRolOrganizador(Long id) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public void eliminarUsuario(Long id) throws OperacionNoPermitidaException {
        throw new UnsupportedOperationException("TODO: implementar");
    }
}

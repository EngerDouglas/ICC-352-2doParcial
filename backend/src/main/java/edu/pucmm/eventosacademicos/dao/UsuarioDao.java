package edu.pucmm.eventosacademicos.dao;

import edu.pucmm.eventosacademicos.modelo.RolUsuario;
import edu.pucmm.eventosacademicos.modelo.Usuario;

import java.util.List;

public class UsuarioDao {

    public Usuario buscarPorId(Long id) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public Usuario buscarPorEmail(String email) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public List<Usuario> listarTodos() {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public List<Usuario> listarPorRol(RolUsuario rol) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public Usuario guardar(Usuario usuario) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public void actualizar(Usuario usuario) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public void eliminar(Long id) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public boolean existePorEmail(String email) {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public long contarUsuarios() {
        throw new UnsupportedOperationException("TODO: implementar");
    }
}

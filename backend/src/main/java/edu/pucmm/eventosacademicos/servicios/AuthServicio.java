package edu.pucmm.eventosacademicos.servicios;

import edu.pucmm.eventosacademicos.modelo.Usuario;
import edu.pucmm.eventosacademicos.util.excepciones.CredencialesInvalidasException;

public class AuthServicio {

    public Usuario autenticar(String email, String password) throws CredencialesInvalidasException {
        throw new UnsupportedOperationException("TODO: implementar");
    }

    public Usuario registrarParticipante(String nombreCompleto, String email, String password) {
        throw new UnsupportedOperationException("TODO: implementar");
    }
}

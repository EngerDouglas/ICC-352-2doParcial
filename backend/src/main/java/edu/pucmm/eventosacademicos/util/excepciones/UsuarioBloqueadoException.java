package edu.pucmm.eventosacademicos.util.excepciones;

public class UsuarioBloqueadoException extends RuntimeException {
    public UsuarioBloqueadoException(String mensaje) {
        super(mensaje);
    }
}

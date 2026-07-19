package edu.pucmm.eventosacademicos.util.excepciones;

public class CupoAgotadoException extends RuntimeException {
    public CupoAgotadoException(String mensaje) {
        super(mensaje);
    }
}

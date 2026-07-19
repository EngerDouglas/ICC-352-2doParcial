package edu.pucmm.eventosacademicos.util.excepciones;

public class AsistenciaDuplicadaException extends RuntimeException {
    public AsistenciaDuplicadaException(String mensaje) {
        super(mensaje);
    }
}

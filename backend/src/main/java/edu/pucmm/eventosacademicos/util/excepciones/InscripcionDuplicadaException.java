package edu.pucmm.eventosacademicos.util.excepciones;

public class InscripcionDuplicadaException extends RuntimeException {
    public InscripcionDuplicadaException(String mensaje) {
        super(mensaje);
    }
}

package edu.pucmm.eventosacademicos.dto;

public class RespuestaEscaneoDTO {

    private boolean exito;
    private String mensaje;
    private String nombreParticipante;
    private String tituloEvento;

    public RespuestaEscaneoDTO() {
    }

    public RespuestaEscaneoDTO(boolean exito, String mensaje, String nombreParticipante, String tituloEvento) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.nombreParticipante = nombreParticipante;
        this.tituloEvento = tituloEvento;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombreParticipante() {
        return nombreParticipante;
    }

    public void setNombreParticipante(String nombreParticipante) {
        this.nombreParticipante = nombreParticipante;
    }

    public String getTituloEvento() {
        return tituloEvento;
    }

    public void setTituloEvento(String tituloEvento) {
        this.tituloEvento = tituloEvento;
    }
}

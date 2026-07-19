package edu.pucmm.eventosacademicos.dto;

public class PuntoEstadisticaDTO {

    private String etiqueta;
    private long valor;

    public PuntoEstadisticaDTO() {
    }

    public PuntoEstadisticaDTO(String etiqueta, long valor) {
        this.etiqueta = etiqueta;
        this.valor = valor;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public long getValor() {
        return valor;
    }

    public void setValor(long valor) {
        this.valor = valor;
    }
}

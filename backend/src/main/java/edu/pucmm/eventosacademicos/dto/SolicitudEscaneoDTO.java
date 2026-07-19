package edu.pucmm.eventosacademicos.dto;

public class SolicitudEscaneoDTO {

    private String tokenEscaneado;

    public SolicitudEscaneoDTO() {
    }

    public SolicitudEscaneoDTO(String tokenEscaneado) {
        this.tokenEscaneado = tokenEscaneado;
    }

    public String getTokenEscaneado() {
        return tokenEscaneado;
    }

    public void setTokenEscaneado(String tokenEscaneado) {
        this.tokenEscaneado = tokenEscaneado;
    }
}

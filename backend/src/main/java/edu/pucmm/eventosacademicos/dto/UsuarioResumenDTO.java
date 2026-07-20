package edu.pucmm.eventosacademicos.dto;

import edu.pucmm.eventosacademicos.modelo.Usuario;

public class UsuarioResumenDTO {

    private Long id;
    private String nombreCompleto;
    private String email;
    private String rol;
    private boolean bloqueado;
    private boolean noEliminable;

    public UsuarioResumenDTO() {
    }

    public static UsuarioResumenDTO desde(Usuario usuario) {
        UsuarioResumenDTO dto = new UsuarioResumenDTO();
        dto.setId(usuario.getId());
        dto.setNombreCompleto(usuario.getNombreCompleto());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol().name());
        dto.setBloqueado(usuario.isBloqueado());
        dto.setNoEliminable(usuario.isNoEliminable());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public boolean isNoEliminable() {
        return noEliminable;
    }

    public void setNoEliminable(boolean noEliminable) {
        this.noEliminable = noEliminable;
    }
}

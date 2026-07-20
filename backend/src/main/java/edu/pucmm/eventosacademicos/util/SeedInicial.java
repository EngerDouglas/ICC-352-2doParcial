package edu.pucmm.eventosacademicos.util;

import edu.pucmm.eventosacademicos.dao.UsuarioDao;
import edu.pucmm.eventosacademicos.modelo.RolUsuario;
import edu.pucmm.eventosacademicos.modelo.Usuario;

import java.time.LocalDateTime;

public class SeedInicial {

    private static final String ADMIN_EMAIL = "admin@eventosacademicos.pucmm.edu.do";
    private static final String ADMIN_PASSWORD = "Admin123!";

    private SeedInicial() {
    }

    public static void ejecutar() {
        UsuarioDao usuarioDao = new UsuarioDao();
        if (usuarioDao.contarUsuarios() > 0) {
            return;
        }

        Usuario admin = new Usuario();
        admin.setNombreCompleto("Administrador");
        admin.setEmail(ADMIN_EMAIL);
        admin.setPasswordHash(PasswordUtil.hashear(ADMIN_PASSWORD));
        admin.setRol(RolUsuario.ADMINISTRADOR);
        admin.setBloqueado(false);
        admin.setNoEliminable(true);
        admin.setFechaCreacion(LocalDateTime.now());
        usuarioDao.guardar(admin);

        System.out.println("Usuario administrador creado: " + ADMIN_EMAIL + " / " + ADMIN_PASSWORD);
    }
}

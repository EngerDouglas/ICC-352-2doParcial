package edu.pucmm.eventosacademicos.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    private PasswordUtil() {
    }

    public static String hashear(String claro) {
        return BCrypt.hashpw(claro, BCrypt.gensalt());
    }

    public static boolean verificar(String claro, String hash) {
        return BCrypt.checkpw(claro, hash);
    }
}

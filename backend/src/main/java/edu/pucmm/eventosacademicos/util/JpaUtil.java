package edu.pucmm.eventosacademicos.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {

    private static final String NOMBRE_UNIDAD_PERSISTENCIA = "eventosAcademicosPU";
    private static EntityManagerFactory fabrica;

    private JpaUtil() {
    }

    public static synchronized EntityManagerFactory obtenerFabrica() {
        if (fabrica == null) {
            fabrica = Persistence.createEntityManagerFactory(NOMBRE_UNIDAD_PERSISTENCIA);
        }
        return fabrica;
    }

    public static EntityManager crearEntityManager() {
        return obtenerFabrica().createEntityManager();
    }

    public static synchronized void cerrar() {
        if (fabrica != null && fabrica.isOpen()) {
            fabrica.close();
        }
    }
}

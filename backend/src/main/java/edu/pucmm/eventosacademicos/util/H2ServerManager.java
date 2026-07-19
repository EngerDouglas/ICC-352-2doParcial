package edu.pucmm.eventosacademicos.util;

import org.h2.tools.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.SQLException;

public class H2ServerManager {

    private static final String TCP_HOST = "localhost";
    private static final int TCP_PORT = 9092;
    private static final int CHECK_TIMEOUT_MS = 500;

    private static Server servidor;

    private H2ServerManager() {
    }

    public static void iniciar() {
        if (puertoEnUso(TCP_HOST, TCP_PORT)) {
            System.out.println("Ya existe una instancia de H2 en " + TCP_HOST + ":" + TCP_PORT + ". Se reutilizará.");
            return;
        }
        try {
            servidor = Server.createTcpServer(
                    "-tcpPort", String.valueOf(TCP_PORT),
                    "-tcpAllowOthers",
                    "-ifNotExists",
                    "-baseDir", "./data"
            ).start();
            System.out.println("Servidor H2 (modo servidor TCP) iniciado en " + TCP_HOST + ":" + TCP_PORT + ".");
        } catch (SQLException ex) {
            System.out.println("No se pudo iniciar H2; se reutilizará instancia externa. Detalle: " + ex.getMessage());
            servidor = null;
        }
    }

    public static void detener() {
        if (servidor != null && servidor.isRunning(false)) {
            servidor.stop();
            System.out.println("Servidor H2 detenido.");
        }
    }

    private static boolean puertoEnUso(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), CHECK_TIMEOUT_MS);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}

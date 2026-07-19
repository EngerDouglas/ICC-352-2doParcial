package edu.pucmm.eventosacademicos.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QrUtil {

    private QrUtil() {
    }

    public static String construirPayload(Long eventoId, Long usuarioId, String token) {
        return "eventoId=" + eventoId + ";usuarioId=" + usuarioId + ";token=" + token;
    }

    public static byte[] generarPng(String contenido, int tamanoPx) throws WriterException, IOException {
        BitMatrix matriz = new MultiFormatWriter().encode(contenido, BarcodeFormat.QR_CODE, tamanoPx, tamanoPx);
        try (ByteArrayOutputStream salida = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(matriz, "PNG", salida);
            return salida.toByteArray();
        }
    }
}

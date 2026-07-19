document.addEventListener('DOMContentLoaded', () => {
    // TODO: eventos/detalle.html -> #btn-inscribirse -> fetch POST
    //       `${API_BASE}/api/eventos/{id}/inscripciones` con credentials: 'include'.
    // TODO: inscripciones/mis-inscripciones.html -> fetch GET
    //       `${API_BASE}/api/participante/inscripciones`, poblar #tabla-inscripciones-body;
    //       botón cancelar -> fetch POST `${API_BASE}/api/inscripciones/{id}/cancelar`.
    // TODO: inscripciones/confirmacion.html -> fetch GET
    //       `${API_BASE}/api/inscripciones/{id}` y setear #codigo-qr.src a
    //       `${API_BASE}/api/inscripciones/{id}/qr`.
});

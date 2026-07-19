document.addEventListener('DOMContentLoaded', () => {
    // TODO: eventos/listado.html -> fetch GET `${API_BASE}/api/eventos`, poblar
    //       #tabla-eventos-body y #vista-grid, toggle #btn-vista-lista / #btn-vista-grid.
    // TODO: eventos/detalle.html -> fetch GET `${API_BASE}/api/eventos/{id}` (id desde
    //       query string ?id=) y poblar #detalle-evento.
    // TODO: eventos/mis-eventos.html -> fetch GET `${API_BASE}/api/organizador/eventos`,
    //       poblar #tabla-mis-eventos-body; acciones publicar/despublicar/cancelar ->
    //       fetch POST `${API_BASE}/api/organizador/eventos/{id}/*` con credentials: 'include'.
    // TODO: eventos/formulario.html -> validaciones de #form-evento antes de enviar
    //       (fetch POST `${API_BASE}/api/organizador/eventos` o `.../{id}` para editar).
});

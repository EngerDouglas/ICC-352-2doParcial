function iniciarBotonInscribirse() {
    const boton = document.querySelector('#btn-inscribirse');
    if (!boton) return;

    const eventoId = qs('id');
    if (!eventoId) return;

    boton.addEventListener('click', async () => {
        const usuario = await sesionActual;
        if (!usuario) {
            irALogin();
            return;
        }
        boton.disabled = true;
        try {
            const inscripcion = await api(`/api/eventos/${encodeURIComponent(eventoId)}/inscripciones`, {
                method: 'POST',
            });
            window.location.href = `/inscripciones/confirmacion.html?id=${inscripcion.id}`;
        } catch (e) {
            alerta('main', 'danger', e.message);
            boton.disabled = false;
        }
    });
}

function iniciarConfirmacion() {
    const imagenQr = document.querySelector('#codigo-qr');
    const contenedor = document.querySelector('#detalle-inscripcion');
    if (!imagenQr || !contenedor) return;

    const id = qs('id');
    if (!id) {
        alerta('main', 'warning', 'Falta el identificador de la inscripción.');
        imagenQr.classList.add('d-none');
        return;
    }

    (async () => {
        const usuario = await requerirSesion();
        if (!usuario) return;
        try {
            const inscripcion = await api(`/api/inscripciones/${encodeURIComponent(id)}`);
            const evento = inscripcion.evento || {};
            contenedor.replaceChildren(
                el('h2', { class: 'h4' }, evento.titulo || ''),
                el('dl', { class: 'row' },
                    el('dt', { class: 'col-sm-3' }, 'Fecha y hora'),
                    el('dd', { class: 'col-sm-9' }, fmtFecha(evento.fechaHora)),
                    el('dt', { class: 'col-sm-3' }, 'Lugar'),
                    el('dd', { class: 'col-sm-9' }, evento.lugar || ''),
                    el('dt', { class: 'col-sm-3' }, 'Inscrito el'),
                    el('dd', { class: 'col-sm-9' }, fmtFecha(inscripcion.fechaInscripcion)),
                ),
                el('p', {}, 'Presenta este código QR al llegar al evento para registrar tu asistencia:'),
            );
            imagenQr.addEventListener('error', () => {
                imagenQr.classList.add('d-none');
                alerta('main', 'warning', 'No se pudo cargar el código QR. Recarga la página para intentarlo de nuevo.');
            });
            imagenQr.src = `${API_BASE}/api/inscripciones/${encodeURIComponent(id)}/qr`;
        } catch (e) {
            imagenQr.classList.add('d-none');
            alerta('main', e.status === 404 ? 'warning' : 'danger',
                e.status === 404 ? 'La inscripción no existe o no le pertenece.' : e.message);
        }
    })();
}

function iniciarMisInscripciones() {
    const cuerpoTabla = document.querySelector('#tabla-inscripciones-body');
    if (!cuerpoTabla) return;

    async function cancelarInscripcion(id) {
        if (!window.confirm('¿Cancelar esta inscripción? Perderás tu cupo en el evento.')) return;
        try {
            await api(`/api/inscripciones/${id}/cancelar`, { method: 'POST' });
            await cargar();
        } catch (e) {
            alerta('main', 'danger', e.message);
        }
    }

    function filaInscripcion(inscripcion) {
        const evento = inscripcion.evento || {};
        const activa = inscripcion.estado === 'ACTIVA';
        const eventoFuturo = evento.fechaHora && new Date(evento.fechaHora) > new Date();

        const acciones = el('td', {});
        if (activa) {
            acciones.append(el('a', {
                class: 'btn btn-sm btn-outline-primary me-1',
                href: `/inscripciones/confirmacion.html?id=${inscripcion.id}`,
            }, 'Ver QR'));
            if (eventoFuturo) {
                acciones.append(el('button', {
                    class: 'btn btn-sm btn-outline-danger',
                    type: 'button',
                    onclick: () => cancelarInscripcion(inscripcion.id),
                }, 'Cancelar'));
            }
        }

        return el('tr', {},
            el('td', {},
                el('a', { href: `/eventos/detalle.html?id=${evento.id}` }, evento.titulo || '')),
            el('td', {}, fmtFecha(evento.fechaHora)),
            el('td', {}, el('span', { class: `badge ${activa ? 'bg-success' : 'bg-secondary'}` },
                inscripcion.estado)),
            acciones,
        );
    }

    async function cargar() {
        try {
            const inscripciones = await api('/api/participante/inscripciones') || [];
            if (inscripciones.length === 0) {
                cuerpoTabla.replaceChildren(el('tr', {},
                    el('td', { colspan: '4', class: 'text-center text-muted' },
                        'No tienes inscripciones todavía.')));
                return;
            }
            cuerpoTabla.replaceChildren(...inscripciones.map(filaInscripcion));
        } catch (e) {
            alerta('main', 'danger', e.message);
        }
    }

    (async () => {
        const usuario = await requerirSesion();
        if (usuario) await cargar();
    })();
}

document.addEventListener('DOMContentLoaded', () => {
    iniciarBotonInscribirse();
    iniciarConfirmacion();
    iniciarMisInscripciones();
});

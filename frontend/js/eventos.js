function iniciarListadoEventos() {
    const cuerpoTabla = document.querySelector('#tabla-eventos-body');
    const vistaLista = document.querySelector('#vista-lista');
    const vistaGrid = document.querySelector('#vista-grid');
    const btnLista = document.querySelector('#btn-vista-lista');
    const btnGrid = document.querySelector('#btn-vista-grid');
    if (!cuerpoTabla || !vistaGrid) return;

    function aplicarVista(vista) {
        const esGrid = vista === 'grid';
        vistaLista.classList.toggle('d-none', esGrid);
        vistaGrid.classList.toggle('d-none', !esGrid);
        btnLista.classList.toggle('active', !esGrid);
        btnGrid.classList.toggle('active', esGrid);
        localStorage.setItem('vistaEventos', vista);
    }

    btnLista.addEventListener('click', () => aplicarVista('lista'));
    btnGrid.addEventListener('click', () => aplicarVista('grid'));
    aplicarVista(localStorage.getItem('vistaEventos') === 'grid' ? 'grid' : 'lista');

    function filaEvento(evento) {
        return el('tr', {},
            el('td', {}, evento.titulo),
            el('td', {}, fmtFecha(evento.fechaHora)),
            el('td', {}, evento.lugar),
            el('td', {}, `${evento.inscritos}/${evento.cupoMaximo}`),
            el('td', {},
                el('a', { class: 'btn btn-sm btn-primary', href: `/eventos/detalle.html?id=${evento.id}` },
                    'Ver detalle')),
        );
    }

    function tarjetaEvento(evento) {
        const cupoLleno = evento.inscritos >= evento.cupoMaximo;
        return el('div', { class: 'card h-100' },
            el('div', { class: 'card-body d-flex flex-column' },
                el('h2', { class: 'card-title h5' }, evento.titulo),
                el('p', { class: 'card-text mb-1' }, fmtFecha(evento.fechaHora)),
                el('p', { class: 'card-text mb-1' }, evento.lugar),
                el('p', { class: 'card-text' },
                    el('span', { class: `badge ${cupoLleno ? 'bg-danger' : 'bg-success'}` },
                        cupoLleno ? 'Cupo agotado' : `Cupo: ${evento.inscritos}/${evento.cupoMaximo}`)),
                el('a', {
                    class: 'btn btn-primary mt-auto',
                    href: `/eventos/detalle.html?id=${evento.id}`,
                }, 'Ver detalle'),
            ),
        );
    }

    (async () => {
        try {
            const eventos = await api('/api/eventos') || [];
            if (eventos.length === 0) {
                cuerpoTabla.replaceChildren(el('tr', {},
                    el('td', { colspan: '5', class: 'text-center text-muted' },
                        'No hay eventos publicados por el momento.')));
                vistaGrid.replaceChildren(el('p', { class: 'text-muted' },
                    'No hay eventos publicados por el momento.'));
                return;
            }
            cuerpoTabla.replaceChildren(...eventos.map(filaEvento));
            vistaGrid.replaceChildren(...eventos.map(tarjetaEvento));
        } catch (e) {
            alerta('main', 'danger', e.message);
        }
    })();
}

function iniciarDetalleEvento() {
    const contenedor = document.querySelector('#detalle-evento');
    const btnInscribirse = document.querySelector('#btn-inscribirse');
    if (!contenedor) return;

    const id = qs('id');
    if (!id) {
        alerta('main', 'warning', 'Falta el identificador del evento.');
        btnInscribirse?.classList.add('d-none');
        return;
    }

    btnInscribirse?.classList.add('d-none');

    (async () => {
        try {
            const evento = await api(`/api/eventos/${encodeURIComponent(id)}`);
            document.title = evento.titulo;
            const cupoLleno = evento.inscritos >= evento.cupoMaximo;

            contenedor.replaceChildren(
                el('h1', {}, evento.titulo),
                el('p', { class: 'lead' }, evento.descripcion || ''),
                el('dl', { class: 'row' },
                    el('dt', { class: 'col-sm-3' }, 'Fecha y hora'),
                    el('dd', { class: 'col-sm-9' }, fmtFecha(evento.fechaHora)),
                    el('dt', { class: 'col-sm-3' }, 'Lugar'),
                    el('dd', { class: 'col-sm-9' }, evento.lugar),
                    el('dt', { class: 'col-sm-3' }, 'Cupo'),
                    el('dd', { class: 'col-sm-9' },
                        el('span', { class: `badge ${cupoLleno ? 'bg-danger' : 'bg-success'}` },
                            cupoLleno ? 'Cupo agotado' : `${evento.inscritos}/${evento.cupoMaximo} inscritos`)),
                ),
            );

            if (btnInscribirse) {
                if (cupoLleno) {
                    btnInscribirse.disabled = true;
                    btnInscribirse.textContent = 'Cupo agotado';
                }
                btnInscribirse.classList.remove('d-none');
            }
        } catch (e) {
            alerta('main', e.status === 404 ? 'warning' : 'danger',
                e.status === 404 ? 'El evento no existe o ya no está disponible.' : e.message);
        }
    })();
}

function iniciarMisEventos() {
    const cuerpoTabla = document.querySelector('#tabla-mis-eventos-body');
    if (!cuerpoTabla) return;

    const badgesEstado = {
        BORRADOR: 'bg-secondary',
        PUBLICADO: 'bg-success',
        CANCELADO: 'bg-danger',
    };

    async function accionEvento(id, accion, confirmacion) {
        if (confirmacion && !window.confirm(confirmacion)) return;
        try {
            await api(`/api/organizador/eventos/${id}/${accion}`, { method: 'POST' });
            await cargar();
        } catch (e) {
            alerta('main', 'danger', e.message);
        }
    }

    function botonAccion(texto, clase, onclick) {
        return el('button', { class: `btn btn-sm ${clase} me-1`, type: 'button', onclick }, texto);
    }

    function filaMiEvento(evento) {
        const acciones = el('td', {});
        if (evento.estado === 'BORRADOR') {
            acciones.append(
                el('a', { class: 'btn btn-sm btn-outline-primary me-1', href: `/eventos/formulario.html?id=${evento.id}` }, 'Editar'),
                botonAccion('Publicar', 'btn-success',
                    () => accionEvento(evento.id, 'publicar', null)),
            );
        } else if (evento.estado === 'PUBLICADO') {
            acciones.append(
                botonAccion('Despublicar', 'btn-outline-secondary',
                    () => accionEvento(evento.id, 'despublicar',
                        '¿Despublicar este evento? Dejará de ser visible para los participantes.')),
                botonAccion('Cancelar', 'btn-outline-danger',
                    () => accionEvento(evento.id, 'cancelar',
                        '¿Cancelar este evento? Esta acción no se puede deshacer.')),
                el('a', { class: 'btn btn-sm btn-outline-primary me-1', href: `/estadisticas/panel.html?id=${evento.id}` }, 'Estadísticas'),
                el('a', { class: 'btn btn-sm btn-outline-primary me-1', href: `/asistencia/escaner.html?id=${evento.id}` }, 'Escáner'),
            );
        }
        return el('tr', {},
            el('td', {}, evento.titulo),
            el('td', {}, el('span', { class: `badge ${badgesEstado[evento.estado] || 'bg-secondary'}` }, evento.estado)),
            el('td', {}, fmtFecha(evento.fechaHora)),
            acciones,
        );
    }

    async function cargar() {
        try {
            const eventos = await api('/api/organizador/eventos') || [];
            if (eventos.length === 0) {
                cuerpoTabla.replaceChildren(el('tr', {},
                    el('td', { colspan: '4', class: 'text-center text-muted' },
                        'Todavía no has creado ningún evento.')));
                return;
            }
            cuerpoTabla.replaceChildren(...eventos.map(filaMiEvento));
        } catch (e) {
            alerta('main', 'danger', e.message);
        }
    }

    (async () => {
        const usuario = await requerirSesion(['ORGANIZADOR', 'ADMINISTRADOR']);
        if (usuario) await cargar();
    })();
}

function iniciarFormularioEvento() {
    const formulario = document.querySelector('#form-evento');
    if (!formulario) return;

    const idEdicion = qs('id');

    (async () => {
        const usuario = await requerirSesion(['ORGANIZADOR', 'ADMINISTRADOR']);
        if (!usuario) return;

        if (idEdicion) {
            try {
                const eventos = await api('/api/organizador/eventos') || [];
                const evento = eventos.find((e) => String(e.id) === idEdicion);
                if (!evento) {
                    alerta('main', 'warning', 'El evento no existe o no le pertenece.');
                    formulario.classList.add('d-none');
                    return;
                }
                formulario.titulo.value = evento.titulo;
                formulario.descripcion.value = evento.descripcion || '';
                formulario.fechaHora.value = (evento.fechaHora || '').slice(0, 16);
                formulario.lugar.value = evento.lugar;
                formulario.cupoMaximo.value = evento.cupoMaximo;
            } catch (e) {
                alerta('main', 'danger', e.message);
                return;
            }
        }

        formulario.addEventListener('submit', async (evento) => {
            evento.preventDefault();

            const titulo = formulario.titulo.value.trim();
            const lugar = formulario.lugar.value.trim();
            const fechaHora = formulario.fechaHora.value;
            const cupoMaximo = parseInt(formulario.cupoMaximo.value, 10);

            if (titulo.length < 3) {
                alerta('main', 'warning', 'El título es demasiado corto.');
                return;
            }
            if (!lugar) {
                alerta('main', 'warning', 'Indique el lugar del evento.');
                return;
            }
            if (!fechaHora || new Date(fechaHora) <= new Date()) {
                alerta('main', 'warning', 'La fecha del evento debe estar en el futuro.');
                return;
            }
            if (!Number.isInteger(cupoMaximo) || cupoMaximo < 1) {
                alerta('main', 'warning', 'El cupo máximo debe ser al menos 1.');
                return;
            }

            const boton = formulario.querySelector('button[type="submit"]');
            boton.disabled = true;
            try {
                const ruta = idEdicion
                    ? `/api/organizador/eventos/${encodeURIComponent(idEdicion)}`
                    : '/api/organizador/eventos';
                await api(ruta, {
                    method: 'POST',
                    body: {
                        titulo,
                        descripcion: formulario.descripcion.value.trim(),
                        fechaHora,
                        lugar,
                        cupoMaximo,
                    },
                });
                window.location.href = '/eventos/mis-eventos.html';
            } catch (e) {
                alerta('main', 'danger', e.message);
                boton.disabled = false;
            }
        });
    })();
}

document.addEventListener('DOMContentLoaded', () => {
    iniciarListadoEventos();
    iniciarDetalleEvento();
    iniciarMisEventos();
    iniciarFormularioEvento();
});

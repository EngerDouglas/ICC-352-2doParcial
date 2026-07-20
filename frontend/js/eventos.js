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

document.addEventListener('DOMContentLoaded', () => {
    iniciarListadoEventos();
    iniciarDetalleEvento();
});

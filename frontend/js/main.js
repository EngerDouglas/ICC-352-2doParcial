function iniciarAdminUsuarios() {
    const cuerpoTabla = document.querySelector('#tabla-usuarios-body');
    if (!cuerpoTabla) return;

    async function accionUsuario(id, accion, confirmacion) {
        if (confirmacion && !window.confirm(confirmacion)) return;
        try {
            await api(`/api/admin/usuarios/${id}/${accion}`, { method: 'POST' });
            await cargar();
        } catch (e) {
            alerta('main', 'danger', e.message);
        }
    }

    function botonAccion(texto, clase, onclick) {
        return el('button', { class: `btn btn-sm ${clase} me-1`, type: 'button', onclick }, texto);
    }

    function filaUsuario(usuario, usuarioActual) {
        const acciones = el('td', {});
        const esYoMismo = usuario.id === usuarioActual.id;

        if (!esYoMismo && !usuario.noEliminable) {
            acciones.append(botonAccion(
                usuario.bloqueado ? 'Desbloquear' : 'Bloquear',
                usuario.bloqueado ? 'btn-outline-success' : 'btn-outline-warning',
                () => accionUsuario(usuario.id, 'bloquear',
                    usuario.bloqueado
                        ? `¿Desbloquear a ${usuario.nombreCompleto}?`
                        : `¿Bloquear a ${usuario.nombreCompleto}? No podrá usar el sistema.`),
            ));
        }
        if (usuario.rol === 'PARTICIPANTE') {
            acciones.append(botonAccion('Hacer organizador', 'btn-outline-primary',
                () => accionUsuario(usuario.id, 'otorgar-organizador',
                    `¿Otorgar rol de organizador a ${usuario.nombreCompleto}?`)));
        } else if (usuario.rol === 'ORGANIZADOR') {
            acciones.append(botonAccion('Revocar organizador', 'btn-outline-secondary',
                () => accionUsuario(usuario.id, 'revocar-organizador',
                    `¿Revocar el rol de organizador a ${usuario.nombreCompleto}?`)));
        }
        if (!esYoMismo && !usuario.noEliminable) {
            acciones.append(botonAccion('Eliminar', 'btn-outline-danger',
                () => accionUsuario(usuario.id, 'eliminar',
                    `¿Eliminar a ${usuario.nombreCompleto}? Esta acción no se puede deshacer.`)));
        }

        return el('tr', {},
            el('td', {}, usuario.nombreCompleto),
            el('td', {}, usuario.email),
            el('td', {}, usuario.rol),
            el('td', {}, el('span', { class: `badge ${usuario.bloqueado ? 'bg-danger' : 'bg-success'}` },
                usuario.bloqueado ? 'Bloqueado' : 'Activo')),
            acciones,
        );
    }

    let usuarioActual = null;

    async function cargar() {
        try {
            const usuarios = await api('/api/admin/usuarios') || [];
            cuerpoTabla.replaceChildren(...usuarios.map((u) => filaUsuario(u, usuarioActual)));
        } catch (e) {
            alerta('main', 'danger', e.message);
        }
    }

    (async () => {
        usuarioActual = await requerirSesion(['ADMINISTRADOR']);
        if (usuarioActual) await cargar();
    })();
}

function iniciarAdminEventos() {
    const cuerpoTabla = document.querySelector('#tabla-eventos-admin-body');
    if (!cuerpoTabla) return;

    const badgesEstado = {
        BORRADOR: 'bg-secondary',
        PUBLICADO: 'bg-success',
        CANCELADO: 'bg-danger',
    };

    async function eliminarEvento(evento) {
        if (!window.confirm(`¿Eliminar el evento "${evento.titulo}"? Esta acción no se puede deshacer.`)) return;
        try {
            await api(`/api/admin/eventos/${evento.id}/eliminar`, { method: 'POST' });
            await cargar();
        } catch (e) {
            alerta('main', 'danger', e.message);
        }
    }

    function filaEvento(evento) {
        return el('tr', {},
            el('td', {},
                el('a', { href: `/eventos/detalle.html?id=${evento.id}` }, evento.titulo)),
            el('td', {}, evento.organizadorNombre || ''),
            el('td', {}, el('span', { class: `badge ${badgesEstado[evento.estado] || 'bg-secondary'}` },
                evento.estado)),
            el('td', {}, fmtFecha(evento.fechaHora)),
            el('td', {}, el('button', {
                class: 'btn btn-sm btn-outline-danger',
                type: 'button',
                onclick: () => eliminarEvento(evento),
            }, 'Eliminar')),
        );
    }

    async function cargar() {
        try {
            const eventos = await api('/api/admin/eventos') || [];
            if (eventos.length === 0) {
                cuerpoTabla.replaceChildren(el('tr', {},
                    el('td', { colspan: '5', class: 'text-center text-muted' },
                        'No hay eventos registrados.')));
                return;
            }
            cuerpoTabla.replaceChildren(...eventos.map(filaEvento));
        } catch (e) {
            alerta('main', 'danger', e.message);
        }
    }

    (async () => {
        const usuario = await requerirSesion(['ADMINISTRADOR']);
        if (usuario) await cargar();
    })();
}

document.addEventListener('DOMContentLoaded', () => {
    iniciarAdminUsuarios();
    iniciarAdminEventos();
    if (window.location.pathname.startsWith('/admin/') &&
        !document.querySelector('#tabla-usuarios-body') &&
        !document.querySelector('#tabla-eventos-admin-body')) {
        requerirSesion(['ADMINISTRADOR']);
    }
});

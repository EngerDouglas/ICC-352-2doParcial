const API_BASE = 'http://localhost:7070';

class ApiError extends Error {
    constructor(status, mensaje) {
        super(mensaje);
        this.status = status;
    }
}

async function api(path, { method = 'GET', body = null } = {}) {
    const opciones = { method, credentials: 'include', headers: {} };
    if (body !== null) {
        opciones.headers['Content-Type'] = 'application/json';
        opciones.body = JSON.stringify(body);
    }

    let respuesta;
    try {
        respuesta = await fetch(`${API_BASE}${path}`, opciones);
    } catch (e) {
        throw new ApiError(0, 'No se pudo conectar con el servidor. Intente de nuevo.');
    }

    let datos = null;
    const tipo = respuesta.headers.get('Content-Type') || '';
    if (tipo.includes('application/json')) {
        datos = await respuesta.json().catch(() => null);
    }

    if (!respuesta.ok) {
        const mensaje = (datos && datos.mensaje) || `Error del servidor (${respuesta.status})`;
        throw new ApiError(respuesta.status, mensaje);
    }
    return datos;
}

function qs(nombre) {
    return new URLSearchParams(window.location.search).get(nombre);
}

function fmtFecha(iso) {
    if (!iso) return '';
    const fecha = new Date(iso);
    if (isNaN(fecha)) return iso;
    return fecha.toLocaleString('es-DO', {
        dateStyle: 'medium',
        timeStyle: 'short',
    });
}

function el(tag, attrs = {}, ...hijos) {
    const nodo = document.createElement(tag);
    for (const [clave, valor] of Object.entries(attrs)) {
        if (clave.startsWith('on') && typeof valor === 'function') {
            nodo.addEventListener(clave.slice(2), valor);
        } else if (valor !== null && valor !== undefined) {
            nodo.setAttribute(clave, valor);
        }
    }
    for (const hijo of hijos) {
        if (hijo === null || hijo === undefined) continue;
        nodo.append(hijo instanceof Node ? hijo : document.createTextNode(hijo));
    }
    return nodo;
}

function alerta(contenedor, tipo, mensaje) {
    const destino = typeof contenedor === 'string'
        ? document.querySelector(contenedor)
        : contenedor;
    if (!destino) return;
    destino.querySelectorAll('.alert').forEach((a) => a.remove());
    const aviso = el('div', { class: `alert alert-${tipo}`, role: 'alert' }, mensaje);
    destino.prepend(aviso);
}

function irALogin() {
    const next = encodeURIComponent(window.location.pathname + window.location.search);
    window.location.href = `/auth/login.html?next=${next}`;
}

const sesionActual = api('/api/auth/me').catch(() => null);

async function requerirSesion(rolesPermitidos = null) {
    const usuario = await sesionActual;
    if (!usuario) {
        irALogin();
        return null;
    }
    if (rolesPermitidos && !rolesPermitidos.includes(usuario.rol)) {
        document.querySelector('main')?.replaceChildren(
            el('div', { class: 'alert alert-danger my-4' },
                'No tiene permisos para acceder a esta página.'),
        );
        return null;
    }
    return usuario;
}

async function renderizarNavbar() {
    const lista = document.querySelector('#navbarNav ul.navbar-nav');
    if (!lista) return;
    const usuario = await sesionActual;

    const enlace = (href, texto) =>
        el('li', { class: 'nav-item' }, el('a', { class: 'nav-link', href }, texto));

    const enlaces = [enlace('/eventos/listado.html', 'Eventos')];

    if (usuario) {
        enlaces.push(enlace('/inscripciones/mis-inscripciones.html', 'Mis inscripciones'));
        if (usuario.rol === 'ORGANIZADOR' || usuario.rol === 'ADMINISTRADOR') {
            enlaces.push(enlace('/eventos/mis-eventos.html', 'Mis eventos'));
            enlaces.push(enlace('/asistencia/escaner.html', 'Escáner'));
        }
        if (usuario.rol === 'ADMINISTRADOR') {
            enlaces.push(enlace('/admin/panel.html', 'Administración'));
        }
        enlaces.push(el('li', { class: 'nav-item' },
            el('span', { class: 'navbar-text mx-2' }, usuario.nombreCompleto)));
        enlaces.push(el('li', { class: 'nav-item' },
            el('button', {
                class: 'btn btn-outline-light btn-sm mt-1',
                type: 'button',
                onclick: async () => {
                    try {
                        await api('/api/auth/logout', { method: 'POST' });
                    } finally {
                        window.location.href = '/';
                    }
                },
            }, 'Salir')));
    } else {
        enlaces.push(enlace('/auth/login.html', 'Iniciar sesión'));
        enlaces.push(enlace('/auth/registro.html', 'Crear cuenta'));
    }

    lista.replaceChildren(...enlaces);
}

document.addEventListener('DOMContentLoaded', renderizarNavbar);

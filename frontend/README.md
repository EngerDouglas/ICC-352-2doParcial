# Frontend — Gestión de Eventos Académicos

HTML, CSS y JavaScript puros (sin frameworks ni paso de build). No necesitas Java,
Gradle ni Docker instalados para trabajar aquí — solo un navegador y, opcionalmente,
un servidor estático simple.

## Cómo verlo funcionando

El backend (API en Java) debe estar corriendo aparte en `http://localhost:7070`
(lo corre quien trabaje en `../backend/` con `./gradlew run`). Con eso levantado:

**Opción 1 — abrir los archivos directo:** doble clic en `index.html` (o cualquier
página dentro de `eventos/`, `auth/`, etc.) y se abre en el navegador. Suficiente
para maquetar HTML/CSS; algunas llamadas a la API pueden fallar por restricciones de
`file://` en el navegador.

**Opción 2 — servidor estático simple (recomendado):** desde esta carpeta
(`frontend/`), correr por ejemplo:

```bash
python3 -m http.server 5500
```

y abrir `http://localhost:5500`. Esto sirve las páginas igual que lo hará nginx en
producción (rutas absolutas como `/css/estilos.css` funcionan igual).

## Cómo apunta al backend

Todas las páginas cargan primero `js/config.js`, que define:

```js
const API_BASE = 'http://localhost:7070';
```

Cada archivo JS usa `${API_BASE}/api/...` para llamar a la API. En producción, nginx
sirve el frontend y hace proxy de `/api/*` al backend bajo el mismo dominio — en ese
caso `API_BASE` se cambia a `''` (string vacío) para que las llamadas sean al mismo
origen.

## Estructura

- `index.html` — página de inicio.
- `auth/` — login y registro.
- `eventos/` — listado, detalle, formulario de creación/edición, "mis eventos".
- `inscripciones/` — "mis inscripciones" y confirmación con QR.
- `asistencia/escaner.html` — escaneo de QR con la cámara (librería `html5-qrcode`).
- `estadisticas/panel.html` — gráficos con Chart.js.
- `admin/` — panel, usuarios, eventos (solo Administrador).
- `css/estilos.css`, `js/config.js` (URL base de la API) y demás `js/*.js`
  (un archivo por grupo de páginas) — estáticos compartidos.

## Estado actual

Implementado y probado contra el backend real: login/registro con sesión por cookie,
listado y detalle de eventos, inscripción y cancelación, confirmación con QR,
formulario de creación/edición de eventos, escáner de asistencia, panel de
estadísticas con Chart.js, y paneles de administración de usuarios y eventos.

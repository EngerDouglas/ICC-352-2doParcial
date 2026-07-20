# Gestión de Eventos Académicos

Sistema web para gestionar eventos académicos (charlas, talleres, seminarios,
congresos) con roles de Administrador, Organizador y Participante, inscripción con
código QR, control de asistencia por escaneo, y estadísticas visuales.

Proyecto del 2do Parcial de Programación Web (ICC-352, PUCMM).

## Estructura del repositorio

Proyecto separado en dos partes independientes, pensado para que dos personas
trabajen en paralelo sin pisarse:

- **`backend/`** — API REST en Javalin (Java + Gradle). Responde JSON, no
  renderiza HTML. Ver `backend/README.md`.
- **`frontend/`** — HTML/CSS/JS estático que consume la API por `fetch()`. No
  requiere Java ni Gradle instalado. Ver `frontend/README.md`.
- **`docker-compose.yml`** + **`docker/`** — despliegue conjunto: nginx sirve
  `frontend/` como estáticos y hace proxy de `/api/*` hacia el backend,
  terminando TLS y redirigiendo el puerto 80 al 443.
- **`docs/diagrama-clases/`** — diagrama de clases del proyecto (requerimiento
  #9 del PDF).

## Cómo ejecutar todo junto con Docker

```bash
docker compose up --build
```

Frontend en `https://localhost` (o el dominio configurado), API en
`https://localhost/api/*` (nginx la enruta al backend).

Verificado localmente de punta a punta: build multi-stage del backend, `docker
compose up --build` levantando `app` + `reverse-proxy`, redirección 80→443,
frontend servido por nginx, y `/api/*` proxeado correctamente al backend.

nginx **requiere** `docker/nginx/certs/fullchain.pem` y `privkey.pem` para arrancar
(carpeta en `.gitignore`, no se versiona). Para la prueba local se generó un
certificado autofirmado con:

```bash
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout docker/nginx/certs/privkey.pem \
  -out docker/nginx/certs/fullchain.pem -subj "/CN=localhost"
```

Eso es **solo para desarrollo** (el navegador lo marca como no confiable). Para
producción hace falta un certificado real (Let's Encrypt/ACME) sobre un dominio que
apunte al servidor — eso requiere tener ya un servidor con IP pública y un dominio,
que no existen todavía (ver pendientes abajo).

## Decisiones de versión (documentadas para el corrector)

- **Javalin 7.2.2** en vez del `7.0.X` literal que pide el PDF: es la misma línea
  mayor (7.x), y es la versión ya probada en el proyecto de referencia del curso con
  este mismo JDK. Cambiar a un `7.0.x` exacto es una sola línea en `backend/build.gradle`
  si se requiere el string literal.
- **Bootstrap 5.3.3 sin jQuery** en vez de Bootstrap 4 + jQuery (usado en el proyecto
  de referencia `asignacion-2`): Bootstrap 5 no depende de jQuery, y todo el AJAX de
  este proyecto usa `fetch()` nativo, como pide el PDF.
- **Java 25 (LTS)**: coincide con el JDK instalado y con el toolchain de los
  proyectos de referencia del curso.
- **Backend 100% API (sin motor de plantillas)**: se separó de frontend para que
  cada persona trabaje en su parte sin depender del entorno de la otra. CORS
  habilitado con credenciales para que el frontend (otro origen en desarrollo)
  pueda mantener la sesión por cookie.

## Estado actual y próximos pasos

Backend y frontend están implementados y funcionando end-to-end en local (probado con
`curl` contra la API real): roles y sesión, alta/edición/publicación/cancelación de
eventos, inscripción con validación de cupo y de duplicados, generación y escaneo de
QR con rechazo de doble asistencia, estadísticas por evento, y administración de
usuarios/eventos.

El diagrama de clases del modelo implementado está en
`docs/diagrama-clases/diagrama-clases.png`.

Pendiente — requiere infraestructura/cuentas que no están disponibles en este entorno:

- [ ] Conseguir un servidor con IP pública y un dominio, provisionar un certificado
      TLS real (Let's Encrypt/ACME) y desplegar ahí con `docker compose`.
- [ ] Publicar la imagen del backend en Docker Hub (`docker login` con la cuenta del
      equipo + `docker push`).
# ICC-352-2doParcial

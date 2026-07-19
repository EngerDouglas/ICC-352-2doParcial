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

**Esto todavía no funciona de punta a punta** (ver "Estado actual" más abajo):
el backend crashea al arrancar porque su lógica de negocio sigue sin implementar, y
además falta `docker/nginx/certs/fullchain.pem` y `privkey.pem` (nginx no arranca sin
esos certificados — ver el TODO de certificados TLS).

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

Este commit contiene el **esqueleto completo** de ambas partes: modelo de datos,
capas DAO/servicio/controlador con firmas y rutas ya definidas, páginas HTML base,
y configuración de build/Docker. **No contiene lógica de negocio todavía** — cada
método no trivial del backend lanza `UnsupportedOperationException("TODO: implementar")`,
y cada archivo JS del frontend solo tiene comentarios `// TODO:`.

Pendiente para la siguiente sesión de trabajo:

- [ ] Backend: implementar los cuerpos de los métodos en `backend/.../dao/`,
      `servicios/` y `controladores/`.
- [ ] Frontend: lógica real de los archivos en `frontend/js/`.
- [ ] Probar el build de Docker (`docker compose up --build`).
- [ ] Provisionar certificados TLS reales para `docker/nginx/nginx.conf` y desplegar
      en un servidor con IP pública.
- [ ] Publicar la imagen del backend en Docker Hub.
- [ ] Colocar el diagrama de clases en `docs/diagrama-clases/`.
# ICC-352-2doParcial

# Backend — API de Gestión de Eventos Académicos

API REST en Javalin. Responde **solo JSON** (y una imagen PNG para el código QR de
cada inscripción) — no renderiza páginas HTML. El frontend vive en `../frontend/` y
es quien consume esta API.

## Arquitectura

- **Capas**: `modelo` (entidades JPA) → `dao` (acceso a datos) → `servicios` (lógica
  de negocio) → `controladores` (handlers Javalin, todos bajo el prefijo `/api`).
  `dto` para payloads JSON, `util` para infraestructura transversal (sesión,
  seguridad, QR, excepciones de negocio).
- **Persistencia**: Hibernate/JPA sobre H2 en **modo servidor TCP real**
  (`org.h2.tools.Server.createTcpServer`, ver `util/H2ServerManager.java`), con
  `hibernate.hbm2ddl.auto=update` en `persistence.xml` como script automático de
  creación/actualización del esquema.
- **CORS**: habilitado (`config.bundledPlugins.enableCors`) con `allowCredentials`
  para que el frontend, corriendo en otro origen durante desarrollo, pueda mantener
  la sesión vía cookie.
- **Errores**: cada excepción de negocio (`util/excepciones/`) se mapea a un código
  HTTP en `Main.java` (`config.routes.exception(...)`) y responde JSON
  `{ "exito": false, "mensaje": "..." }`.

## Cómo ejecutar en local

```bash
./gradlew run
```

La API arranca en `http://localhost:7070/api/...`. En el primer arranque se crea
automáticamente un usuario administrador (no eliminable):
`admin@eventosacademicos.pucmm.edu.do` / `Admin123!` (ver `util/SeedInicial.java`).

El servidor H2 en modo TCP queda escuchando en el puerto `9092`
(`jdbc:h2:tcp://localhost:9092/eventosdb`, usuario `sa`, sin contraseña).

## Rutas principales

Ver `src/main/java/edu/pucmm/eventosacademicos/Main.java` para el listado completo
registrado. Todas bajo `/api`: `/api/auth/*`, `/api/eventos`, `/api/eventos/{id}`,
`/api/organizador/eventos/*`, `/api/inscripciones/*`, `/api/participante/inscripciones`,
`/api/asistencia/validar`, `/api/admin/*`.

## Estado actual

Lógica de negocio implementada y verificada de punta a punta (login, registro,
crear/publicar/cancelar eventos, inscripción con validación de cupo y duplicados,
generación y escaneo de QR con rechazo de doble asistencia, estadísticas,
administración de usuarios y eventos), incluyendo el build de Docker
(`docker build ./backend`) y `docker compose up --build` completo con nginx.
Pendiente: despliegue con dominio, IP pública y TLS real, y publicar la imagen en
Docker Hub — ver el README raíz.

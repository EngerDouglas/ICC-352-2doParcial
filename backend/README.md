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

## Cómo ejecutar en local

```bash
./gradlew run
```

**Estado actual: esto todavía va a fallar al arrancar.** `Main.java` llama a
`SeedInicial.ejecutar()` antes de levantar el servidor, y ese método (como el resto
de la lógica de negocio) es un stub que lanza `UnsupportedOperationException` — ver
"Estado actual" más abajo. Una vez implementado, la API arrancará en
`http://localhost:7070/api/...` y en el primer arranque se creará automáticamente un
usuario administrador (no eliminable, según `Usuario.noEliminable`).

El servidor H2 en modo TCP queda escuchando en el puerto `9092`
(`jdbc:h2:tcp://localhost:9092/eventosdb`, usuario `sa`, sin contraseña).

## Rutas principales

Ver `src/main/java/edu/pucmm/eventosacademicos/Main.java` para el listado completo
registrado. Todas bajo `/api`: `/api/auth/*`, `/api/eventos`, `/api/eventos/{id}`,
`/api/organizador/eventos/*`, `/api/inscripciones/*`, `/api/participante/inscripciones`,
`/api/asistencia/validar`, `/api/admin/*`.

## Estado actual

Esqueleto completo, compila (`./gradlew compileJava`), pero cada método de negocio
lanza `UnsupportedOperationException("TODO: implementar")`. Ver el README raíz del
proyecto para la lista de pendientes.

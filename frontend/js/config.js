// URL base del backend (API REST en Javalin).
// Desarrollo local: el backend corre aparte en el puerto 7070.
// Producción: nginx expone el frontend y el backend bajo el mismo dominio,
// haciendo proxy de /api/* al backend -> cambiar API_BASE a '' (string vacío).
const API_BASE = 'http://localhost:7070';

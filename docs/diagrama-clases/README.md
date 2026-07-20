# Diagrama de clases

`diagrama-clases.png` — diagrama de clases del modelo de dominio implementado
(requerimiento #9 del PDF). Generado a partir de `diagrama-clases.mmd` (fuente en
sintaxis Mermaid, editable) con `npx @mermaid-js/mermaid-cli`.

El modelo de dominio (`backend/src/main/java/edu/pucmm/eventosacademicos/modelo/`)
consta de: `Usuario`, `Evento`, `Inscripcion`, `Asistencia`, y los enums
`RolUsuario`, `EstadoEvento`, `EstadoInscripcion`.

Para regenerar el PNG tras modificar el `.mmd`:

```bash
npx -y @mermaid-js/mermaid-cli -i diagrama-clases.mmd -o diagrama-clases.png -b white -s 3
```

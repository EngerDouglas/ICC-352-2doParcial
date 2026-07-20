document.addEventListener('DOMContentLoaded', () => {
    const totalInscritos = document.querySelector('#total-inscritos');
    const totalAsistentes = document.querySelector('#total-asistentes');
    const porcentajeAsistencia = document.querySelector('#porcentaje-asistencia');
    const canvasInscripciones = document.querySelector('#grafico-inscripciones-por-dia');
    const canvasAsistencia = document.querySelector('#grafico-asistencia-por-hora');
    if (!canvasInscripciones || !canvasAsistencia) return;

    const id = qs('id');
    if (!id) {
        alerta('main', 'warning', 'Falta el identificador del evento.');
        return;
    }

    function graficoBarras(canvas, titulo, puntos, color) {
        new Chart(canvas, {
            type: 'bar',
            data: {
                labels: puntos.map((p) => p.etiqueta),
                datasets: [{
                    label: titulo,
                    data: puntos.map((p) => p.valor),
                    backgroundColor: color,
                }],
            },
            options: {
                scales: { y: { beginAtZero: true, ticks: { precision: 0 } } },
            },
        });
    }

    (async () => {
        const usuario = await requerirSesion(['ORGANIZADOR', 'ADMINISTRADOR']);
        if (!usuario) return;
        try {
            const datos = await api(`/api/eventos/${encodeURIComponent(id)}/estadisticas`);
            totalInscritos.textContent = datos.totalInscritos;
            totalAsistentes.textContent = datos.totalAsistentes;
            porcentajeAsistencia.textContent = `${Number(datos.porcentajeAsistencia).toFixed(1)}%`;
            graficoBarras(canvasInscripciones, 'Inscripciones por día',
                datos.inscripcionesPorDia || [], 'rgba(13, 110, 253, 0.6)');
            graficoBarras(canvasAsistencia, 'Asistencia por hora',
                datos.asistenciaPorHora || [], 'rgba(25, 135, 84, 0.6)');
        } catch (e) {
            alerta('main', 'danger', e.message);
        }
    })();
});

document.addEventListener('DOMContentLoaded', () => {
    const lector = document.querySelector('#lector-qr');
    const resultado = document.querySelector('#resultado-escaneo');
    if (!lector || !resultado) return;

    (async () => {
        const usuario = await requerirSesion(['ORGANIZADOR', 'ADMINISTRADOR']);
        if (!usuario) return;

        let procesando = false;
        const escaner = new Html5Qrcode('lector-qr');

        function mostrarResultado(exito, lineas) {
            resultado.replaceChildren(
                el('div', { class: `alert ${exito ? 'alert-success' : 'alert-danger'}` },
                    ...lineas.flatMap((linea, i) => (i === 0
                        ? [el('strong', {}, linea)]
                        : [el('br'), linea]))),
            );
        }

        async function alDetectar(textoDecodificado) {
            if (procesando) return;
            procesando = true;
            try {
                await escaner.pause(true);
            } catch (e) {
            }
            try {
                const respuesta = await api('/api/asistencia/validar', {
                    method: 'POST',
                    body: { tokenEscaneado: textoDecodificado },
                });
                if (respuesta && respuesta.exito) {
                    mostrarResultado(true, [
                        'Asistencia registrada',
                        `Participante: ${respuesta.nombreParticipante || ''}`,
                        `Evento: ${respuesta.tituloEvento || ''}`,
                    ]);
                } else {
                    mostrarResultado(false, [(respuesta && respuesta.mensaje) || 'Código no válido.']);
                }
            } catch (e) {
                mostrarResultado(false, [e.message]);
            }
            setTimeout(() => {
                procesando = false;
                try {
                    escaner.resume();
                } catch (e) {
                }
            }, 2500);
        }

        try {
            await escaner.start(
                { facingMode: 'environment' },
                { fps: 10, qrbox: { width: 250, height: 250 } },
                alDetectar,
                () => {},
            );
        } catch (e) {
            resultado.replaceChildren(
                el('div', { class: 'alert alert-danger' },
                    'No se pudo acceder a la cámara. Verifica los permisos del navegador e intenta de nuevo.'),
            );
        }
    })();
});

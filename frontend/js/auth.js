document.addEventListener('DOMContentLoaded', () => {
    const formLogin = document.querySelector('#form-login');
    if (formLogin) {
        formLogin.addEventListener('submit', async (evento) => {
            evento.preventDefault();
            const boton = formLogin.querySelector('button[type="submit"]');
            boton.disabled = true;
            try {
                await api('/api/auth/login', {
                    method: 'POST',
                    body: {
                        email: formLogin.email.value.trim(),
                        password: formLogin.password.value,
                    },
                });
                const next = qs('next');
                window.location.href = next && next.startsWith('/') ? next : '/';
            } catch (e) {
                alerta('main', 'danger', e.message);
                boton.disabled = false;
            }
        });
    }

    const formRegistro = document.querySelector('#form-registro');
    if (formRegistro) {
        formRegistro.addEventListener('submit', async (evento) => {
            evento.preventDefault();

            const nombreCompleto = formRegistro.nombreCompleto.value.trim();
            const email = formRegistro.email.value.trim();
            const password = formRegistro.password.value;

            if (nombreCompleto.length < 3) {
                alerta('main', 'warning', 'El nombre completo es demasiado corto.');
                return;
            }
            if (password.length < 6) {
                alerta('main', 'warning', 'La contraseña debe tener al menos 6 caracteres.');
                return;
            }

            const boton = formRegistro.querySelector('button[type="submit"]');
            boton.disabled = true;
            try {
                await api('/api/auth/registro', {
                    method: 'POST',
                    body: { nombreCompleto, email, password },
                });
                window.location.href = '/';
            } catch (e) {
                alerta('main', 'danger', e.message);
                boton.disabled = false;
            }
        });
    }
});

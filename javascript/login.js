document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.querySelector('form');
    if (loginForm && window.location.pathname.includes('login.html')) {
        loginForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const usuario = loginForm.querySelector('input[type="text"]').value;
            const password = loginForm.querySelector('input[type="password"]').value;

            console.log("Intentando iniciar sesión con:", usuario);
            alert(`¡Bienvenido, ${usuario}! Redirigiendo...`);
            // Aquí podrías usar window.location.href = 'home.html';
        });
    }
});
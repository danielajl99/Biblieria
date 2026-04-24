document.addEventListener('DOMContentLoaded', () => {
    const btnExplorar = document.querySelector('.btn');

    if (btnExplorar) {
        btnExplorar.addEventListener('click', (e) => {
            // Evitamos que el enlace haga su trabajo normal por un momento
            e.preventDefault();
            
            console.log("Redirigiendo al catálogo...");

            // Redirección manual mediante JavaScript
            window.location.href = '/html/catalago.html';
        });
    }
});
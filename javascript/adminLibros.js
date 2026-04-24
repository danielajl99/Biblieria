document.addEventListener('DOMContentLoaded', () => {
    
    // 1. Botones de "Registrar" (Nuevo Libro o Nuevo Cliente)
    const btnAdd = document.querySelectorAll('.btn-add');
    btnAdd.forEach(btn => {
        btn.addEventListener('click', () => {
            const seccion = btn.closest('section').id;
            alert(`Abriendo formulario para registrar en: ${seccion}`);
        });
    });

    // 2. Acciones de la Tabla (Editar, Eliminar, Descargar)
    const tableBody = document.querySelector('tbody');
    if (tableBody) {
        tableBody.addEventListener('click', (e) => {
            
            // Botón Editar
            if (e.target.classList.contains('btn-edit')) {
                const fila = e.target.closest('tr');
                const id = fila.cells[0].innerText;
                alert(`Editando registro ID: ${id}`);
            }

            // Botón Eliminar
            if (e.target.classList.contains('btn-delete')) {
                const fila = e.target.closest('tr');
                const nombre = fila.cells[1].innerText;
                if (confirm(`¿Estás seguro de que deseas eliminar a: ${nombre}?`)) {
                    fila.remove();
                }
            }

            // Botón Descargar (Imagen 4)
            if (e.target.classList.contains('btn-download')) {
                alert("Generando reporte PDF del libro...");
            }
        });
    }

    // 3. Botón Cerrar Sesión (Sidebar)
    const logoutBtn = document.querySelector('.logout');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', (e) => {
            e.preventDefault();
            if (confirm("¿Cerrar sesión?")) {
                window.location.href = 'login.html';
            }
        });
    }
});
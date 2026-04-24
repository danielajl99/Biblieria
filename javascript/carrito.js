document.addEventListener('DOMContentLoaded', () => {
    const tabla = document.getElementById('lista-carrito');
    const totalTxt = document.getElementById('resumen-total');
    const subtotalTxt = document.getElementById('resumen-subtotal');

    function renderizarCarrito() {
        // Obtenemos los datos actualizados del LocalStorage
        const carrito = JSON.parse(localStorage.getItem('carrito')) || [];
        
        if (!tabla) return; // Seguridad por si el script carga en otra página

        tabla.innerHTML = '';
        let acumuladoTotal = 0;

        if (carrito.length === 0) {
            tabla.innerHTML = '<tr><td colspan="5" style="text-align:center; padding:20px;">Tu carrito está vacío</td></tr>';
        } else {
            carrito.forEach((prod, index) => {
                const subtotal = prod.precio * prod.cantidad;
                acumuladoTotal += subtotal;

                tabla.innerHTML += `
                    <tr>
                        <td>${prod.titulo}</td>
                        <td>${prod.precio.toFixed(2)}€</td>
                        <td>${prod.cantidad}</td>
                        <td>${subtotal.toFixed(2)}€</td>
                        <td>
                            <button class="btn-delete-cart" onclick="eliminarDelCarrito(${index})">
                                Eliminar
                            </button>
                        </td>
                    </tr>
                `;
            });
        }

        // Actualizamos los textos de la derecha
        if(subtotalTxt) subtotalTxt.innerText = `${acumuladoTotal.toFixed(2)}€`;
        if(totalTxt) totalTxt.innerText = `${acumuladoTotal.toFixed(2)}€`;
    }

    // Función para eliminar un producto (la hacemos global con window)
    window.eliminarDelCarrito = (index) => {
        let carrito = JSON.parse(localStorage.getItem('carrito')) || [];
        carrito.splice(index, 1); // Borramos el elemento
        localStorage.setItem('carrito', JSON.stringify(carrito)); // Guardamos
        renderizarCarrito(); // Refrescamos la tabla
    };

    // Botón Finalizar Compra
    const btnFinalizar = document.getElementById('btn-finalizar');
    if (btnFinalizar) {
        btnFinalizar.addEventListener('click', () => {
            const carrito = JSON.parse(localStorage.getItem('carrito')) || [];
            if (carrito.length > 0) {
                alert("¡Compra procesada con éxito!");
                localStorage.removeItem('carrito'); // Limpiamos el carrito
                renderizarCarrito();
            } else {
                alert("El carrito está vacío.");
            }
        });
    }

    renderizarCarrito();
});
document.addEventListener('DOMContentLoaded', () => {
    // Seleccionamos todos los botones de añadir al carrito
    const botonesCompra = document.querySelectorAll('.btn-comprar');

    botonesCompra.forEach(boton => {
        boton.addEventListener('click', (e) => {
            // Buscamos el contenedor del libro más cercano (ajusta la clase si es necesario)
            const card = e.target.closest('.libro-card') || e.target.closest('.libro-info');
            
            // Capturamos los datos directamente del HTML de esa tarjeta
            const titulo = card.querySelector('h3').innerText;
            
            // Limpiamos el precio (quitamos € y convertimos , en .)
            const precioTexto = card.querySelector('.precio').innerText;
            const precio = parseFloat(precioTexto.replace('€', '').replace(',', '.').trim());

            // Buscamos si hay un input de cantidad dentro de esa tarjeta
            // Si no tienes input en el HTML, por defecto será 1
            const inputCantidad = card.querySelector('input[type="number"]');
            const cantidad = inputCantidad ? parseInt(inputCantidad.value) : 1;

            if (cantidad < 1) return alert("La cantidad debe ser al menos 1");

            // Creamos el objeto del libro
            const libroParaCarrito = {
                titulo: titulo,
                precio: precio,
                cantidad: cantidad
            };

            // --- Lógica de Almacenamiento ---
            let carrito = JSON.parse(localStorage.getItem('carrito')) || [];

            // Verificamos si el libro ya estaba en el carrito para no repetirlo
            const indice = carrito.findIndex(item => item.titulo === libroParaCarrito.titulo);

            if (indice !== -1) {
                // Si ya existe, sumamos la nueva cantidad a la anterior
                carrito[indice].cantidad += cantidad;
            } else {
                // Si es nuevo, lo añadimos al array
                carrito.push(libroParaCarrito);
            }

            // Guardamos en el LocalStorage
            localStorage.setItem('carrito', JSON.stringify(carrito));

            alert(`Añadido: ${cantidad}x "${titulo}" al carrito.`);
        });
    });
});
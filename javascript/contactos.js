document.addEventListener('DOMContentLoaded', () => {
    const contactForm = document.querySelector('.form-body form');
    if (contactForm) {
        contactForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const nombre = contactForm.querySelector('input[placeholder*="Nombre"]').value;
            const asunto = contactForm.querySelector('select').value;
            
            alert(`Gracias ${nombre}, hemos recibido tu consulta sobre: ${asunto}`);
            contactForm.reset();
        });
    }
});
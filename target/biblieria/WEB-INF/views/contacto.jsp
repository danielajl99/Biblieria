<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contacto | Bibliería</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/theme.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/contactos.css">
</head>
<body>
<%@ include file="includes/nav.jsp" %>
<main class="contact-hero">
    <section class="contact-glass-card">
        <div class="contact-header">
            <h1>Contacta con nosotros</h1>
            <p>Cuéntanos qué necesitas y guardaremos tu consulta.</p>
        </div>
        <div class="contact-grid">
            <aside class="info-sidebar">
                <div class="info-item"><div class="icon-circle">Tel</div><div><h4>Teléfono</h4><p>+34 900 123 456</p></div></div>
                <div class="info-item"><div class="icon-circle">@</div><div><h4>Correo</h4><p>biblieria@gmail.com</p></div></div>
                <div class="info-item"><div class="icon-circle">Dir</div><div><h4>Ubicación</h4><p>Calle Literatos, 12, Madrid</p></div></div>
            </aside>
            <div class="form-body">
                <% if (request.getAttribute("ok") != null) { %><div class="alert-ok"><%= request.getAttribute("ok") %></div><% } %>
                <% if (request.getAttribute("error") != null) { %><div class="alert-error"><%= request.getAttribute("error") %></div><% } %>
                <form method="post" action="<%= request.getContextPath() %>/contacto">
                    <div class="input-group"><label>Nombre completo</label><input name="nombre" type="text" placeholder="Ej. Gabriel García" required></div>
                    <div class="input-group"><label>Email</label><input name="email" type="email" placeholder="nombre@correo.com" required></div>
                    <div class="input-group"><label>Asunto</label><select name="asunto"><option>Consulta general</option><option>Pedido de libro</option><option>Eventos y lecturas</option></select></div>
                    <div class="input-group"><label>Mensaje</label><textarea name="mensaje" rows="5" placeholder="Escribe aquí tu mensaje..." required></textarea></div>
                    <button type="submit" class="btn-send">Enviar mensaje</button>
                </form>
            </div>
        </div>
    </section>
</main>
<footer class="main-footer"><p>&copy; 2026 Bibliería - Pasión por la lectura</p></footer>
</body>
</html>

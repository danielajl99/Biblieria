<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inicio | Bibliería</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/theme.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/home.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/pie.css">
</head>
<body>
<%@ include file="includes/nav.jsp" %>
<header class="hero">
    <div class="hero-content">
        <h1>Bibliería</h1>
        <p>Descubre historias que transforman y palabras que inspiran.</p>
        <a href="<%= request.getContextPath() %>/catalogo" class="btn">Explorar catálogo</a>
    </div>
</header>
<footer class="main-footer">
    <p>&copy; 2026 Bibliería - Pasión por la lectura</p>
    <p>Contacto: biblieria@gmail.com | Tel: +34 900 123 456</p>
</footer>
</body>
</html>

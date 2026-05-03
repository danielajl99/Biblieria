<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/theme.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/login.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/registro.css">
    <title>Registro - Bibliería</title>
</head>
<body>
    <div class="login-card">
        <h2>Crear Cuenta</h2>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert-error"><%= request.getAttribute("error") %></div>
        <% } %>

        <form method="post" action="<%= request.getContextPath() %>/registro">
            <div class="input-group">
                <label>Nombre Completo</label>
                <input type="text" name="nombre" placeholder="Tu nombre" value="<%= request.getAttribute("nombre") != null ? request.getAttribute("nombre") : "" %>" required autocomplete="name">
            </div>
            <div class="input-group">
                <label>Correo Electrónico</label>
                <input type="email" name="email" placeholder="correo@ejemplo.com" value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" required autocomplete="email">
            </div>
            <div class="input-group">
                <label>Contraseña</label>
                <input type="password" name="password" placeholder="••••••••" required autocomplete="new-password">
            </div>
            <div class="input-group">
                <label>Confirmar Contraseña</label>
                <input type="password" name="confirm" placeholder="••••••••" required autocomplete="new-password">
            </div>
            <button type="submit" class="btn-login">Registrarse</button>
        </form>

        <div class="footer-links">
            <p>¿Ya tienes cuenta? <a href="<%= request.getContextPath() %>/login">Inicia sesión</a></p>
        </div>
    </div>
</body>
</html>

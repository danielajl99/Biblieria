<%-- 
    Directiva de pagina JSP.
    Define que la respuesta que se enviara al navegador sera HTML con codificacion UTF-8.
    pageEncoding tambien indica a JSP como debe leer este archivo.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <%-- Codificacion del documento HTML para que el navegador interprete bien los caracteres. --%>
    <meta charset="UTF-8">

    <%-- Hace que la pagina se adapte correctamente a moviles, tablets y pantallas grandes. --%>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <%-- 
        Carga la hoja de estilos del login.
        request.getContextPath() obtiene el nombre/base de la aplicacion desplegada.
        Asi el enlace funciona aunque la app no este publicada en la raiz del servidor.
        Ejemplo: /Biblieria-Servlet/css/login.css
    --%>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/theme.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/login.css">

    <%-- Titulo que aparece en la pestana del navegador. --%>
    <title>Login - Bibliería</title>
</head>
<body>
    <%-- Contenedor principal de la tarjeta visual del formulario de login. --%>
    <div class="login-card">
        <h2>Entrar a la Bibliería</h2>

        <%-- 
            Bloque de error.
            Se muestra solo si existe un error enviado desde el servlet como atributo
            request.setAttribute("error", "...") o si viene como parametro en la URL:
            ?error=...
        --%>
        <% if (request.getAttribute("error") != null || request.getParameter("error") != null) { %>
            <div class="alert-error">
                <%-- 
                    Imprime el mensaje de error.
                    Primero da prioridad al atributo "error" de la request, porque suele venir
                    del servlet despues de validar el formulario.
                    Si no existe ese atributo, muestra el parametro "error" recibido por URL.
                --%>
                <%= request.getAttribute("error") != null 
                    ? request.getAttribute("error") : request.getParameter("error") 
                %></div>
        <% } %>

        <%-- 
            Bloque de confirmacion.
            Se muestra cuando la URL trae un parametro "ok", por ejemplo despues de una accion correcta:
            ?ok=Sesion cerrada correctamente
        --%>
        <% if (request.getParameter("ok") != null) { %>
            <div class="alert-ok"><%= request.getParameter("ok") %></div>
        <% } %>

        <%-- 
            Formulario de inicio de sesion.
            method="post" envia los datos en el cuerpo de la peticion HTTP.
            action apunta al servlet /login, que recibira username y password.
        --%>
        <form method="post" action="<%= request.getContextPath() %>/login">
            <div class="input-group">
                <%-- Campo para escribir el nombre de usuario. "required" obliga a rellenarlo. --%>
                <label>Correo electrónico</label>
                <input type="text" name="username" placeholder="Tu correo electrónico" required autocomplete="username">
            </div>
            <div class="input-group">
                <%-- 
                    Campo para la contrasena.
                    type="password" oculta los caracteres escritos.
                    autocomplete ayuda al navegador/gestor de claves a reconocer este campo.
                --%>
                <label>Contraseña</label>
                <input type="password" name="password" placeholder="••••••••" required autocomplete="current-password">
            </div>

            <%-- Boton que envia el formulario al LoginServlet. --%>
            <button type="submit" class="btn-login">Ingresar</button>
        </form>

        <%-- Texto informativo con credenciales de prueba para usar en la demo. --%>
        <div class="footer-links">
            <p>Demo: <strong>admin</strong> / <strong>admin123</strong></p>
            <p>¿No tienes cuenta? <a href="<%= request.getContextPath() %>/registro">Regístrate</a></p>
        </div>
    </div>
</body>
</html>

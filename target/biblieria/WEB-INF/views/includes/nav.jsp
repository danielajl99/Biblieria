<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.biblieria.model.Usuario" %>
<%
    String ctxNav = request.getContextPath();
    Usuario usuarioNav = (Usuario) session.getAttribute("usuario");
%>
<header class="main-header">
    <nav class="navbar">
        <div class="logo">Bibliería</div>
        <ul class="nav-links">
            <li><a href="<%= ctxNav %>/home">Inicio</a></li>
            <li><a href="<%= ctxNav %>/catalogo">Catálogo</a></li>
            <li><a href="<%= ctxNav %>/carrito">Carrito</a></li>
            <li><a href="<%= ctxNav %>/contacto">Contacto</a></li>
            <% if (usuarioNav != null) { %>
                <li><a href="<%= ctxNav %>/pedidos">Pedidos</a></li>
                <% if ("ADMIN".equals(usuarioNav.getRol())) { %>
                    <li><a href="<%= ctxNav %>/admin/libros">Administración</a></li>
                <% } %>
                <li><a href="<%= ctxNav %>/logout">Cerrar sesión</a></li>
            <% } else { %>
                <li><a href="<%= ctxNav %>/login">Inicio sesión</a></li>
                <li><a href="<%= ctxNav %>/registro">Registro</a></li>
            <% } %>
        </ul>
    </nav>
</header>

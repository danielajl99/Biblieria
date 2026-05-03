<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, com.biblieria.model.Libro" %>
<%
    String ctx = request.getContextPath();
    List<Libro> libros = (List<Libro>) request.getAttribute("libros");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catálogo - Bibliería</title>
    <link rel="stylesheet" href="<%= ctx %>/css/theme.css">
    <link rel="stylesheet" href="<%= ctx %>/css/catalogo.css">
</head>
<body>
<%@ include file="includes/nav.jsp" %>
<header class="catalog-header">
    <h1>Nuestro Catálogo</h1>
    <p>Tesoros literarios</p>
</header>
<main class="container">
    <% if (request.getParameter("ok") != null) { %><div class="catalog-alert alert-ok"><%= request.getParameter("ok") %></div><% } %>
    <% if (request.getParameter("error") != null) { %><div class="catalog-alert alert-error"><%= request.getParameter("error") %></div><% } %>
    <section class="grid-catalogo">
        <% if (libros != null) { for (Libro libro : libros) { %>
            <article class="libro-card">
                <div class="libro-img">
                    <img src="<%= ctx %>/libros/imagen?id=<%= libro.getId() %>" alt="Portada de <%= libro.getTitulo() %>">
                </div>
                <div class="libro-info">
                    <span class="anio"><%= libro.getAnio() == null ? "S/A" : libro.getAnio() %></span>
                    <h3><%= libro.getTitulo() %></h3>
                    <p class="autor"><%= libro.getAutor() %></p>
                    <p class="stock">Stock: <%= libro.getStock() %></p>
                    <p class="precio"><%= libro.getPrecio() %> €</p>
                    <% if (libro.getStock() > 0) { %>
                        <form method="post" action="<%= ctx %>/carrito">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="libroId" value="<%= libro.getId() %>">
                            <button class="btn-comprar" type="submit">Añadir al carrito</button>
                        </form>
                    <% } else { %>
                        <button class="btn-comprar disabled" type="button" disabled>Sin stock</button>
                    <% } %>
                </div>
            </article>
        <% }} %>
    </section>
</main>
</body>
</html>

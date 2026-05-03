<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, com.biblieria.model.Libro, com.biblieria.model.Usuario" %>
<%
    String ctx = request.getContextPath();
    List<Libro> libros = (List<Libro>) request.getAttribute("libros");
    List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
    Libro edit = (Libro) request.getAttribute("editLibro");
    boolean editing = edit != null;
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel Admin | Bibliería</title>
    <link rel="stylesheet" href="<%= ctx %>/css/theme.css">
    <link rel="stylesheet" href="<%= ctx %>/css/adminLibros.css">
</head>
<body>
<nav class="sidebar">
    <div class="logo"><h2>Bibliería</h2><span>Panel de Control</span></div>
    <ul>
        <li><a href="#libros" class="active">📚 Gestión de Libros</a></li>
        <li><a href="#usuarios">👥 Gestión de Clientes</a></li>
        <li><a href="<%= ctx %>/catalogo">🌐 Ver Catálogo</a></li>
        <li><a href="<%= ctx %>/logout" class="logout">Cerrar Sesión</a></li>
    </ul>
</nav>
<main class="content">
    <% if (request.getParameter("ok") != null) { %><div class="alert alert-ok"><%= request.getParameter("ok") %></div><% } %>
    <% if (request.getAttribute("error") != null) { %><div class="alert alert-error"><%= request.getAttribute("error") %></div><% } %>
    <section id="libros" class="admin-section">
        <header class="section-header">
            <h1>Inventario de Libros</h1>
            <a class="btn-link" href="<%= ctx %>/catalogo">Ver catálogo público</a>
        </header>
        <div class="form-panel">
            <h2><%= editing ? "Modificar libro" : "Registrar nuevo libro" %></h2>
            <form method="post" action="<%= ctx %>/admin/libros" enctype="multipart/form-data" class="form-grid">
                <input type="hidden" name="action" value="<%= editing ? "update" : "create" %>">
                <% if (editing) { %><input type="hidden" name="id" value="<%= edit.getId() %>"><% } %>
                <label>Título<input name="titulo" value="<%= editing ? edit.getTitulo() : "" %>" required></label>
                <label>Autor<input name="autor" value="<%= editing ? edit.getAutor() : "" %>" required></label>
                <label>Año<input name="anio" type="number" value="<%= editing && edit.getAnio()!=null ? edit.getAnio() : "" %>"></label>
                <label>Precio<input name="precio" type="number" step="0.01" value="<%= editing ? edit.getPrecio() : "0.00" %>" required></label>
                <label>Stock<input name="stock" type="number" value="<%= editing ? edit.getStock() : "0" %>" required></label>
                <label>Portada<input name="imagen" type="file" accept="image/*"></label>
                <label style="grid-column: 1 / -1;">Descripción<textarea name="descripcion" rows="3"><%= editing && edit.getDescripcion()!=null ? edit.getDescripcion() : "" %></textarea></label>
                <button class="btn-save" type="submit"><%= editing ? "Guardar cambios" : "Crear libro" %></button>
                <% if (editing) { %><a class="btn-cancel" href="<%= ctx %>/admin/libros">Cancelar</a><% } %>
            </form>
        </div>
        <div class="table-container">
            <table>
                <thead><tr><th>Portada</th><th>ID</th><th>Título</th><th>Autor</th><th>Año</th><th>Precio</th><th>Stock</th><th>Acciones</th></tr></thead>
                <tbody>
                <% if (libros != null) { for (Libro libro : libros) { %>
                    <tr>
                        <td><img class="preview-img" src="<%= ctx %>/libros/imagen?id=<%= libro.getId() %>" alt="portada"></td>
                        <td>#<%= libro.getId() %></td>
                        <td><%= libro.getTitulo() %></td>
                        <td><%= libro.getAutor() %></td>
                        <td><%= libro.getAnio() == null ? "" : libro.getAnio() %></td>
                        <td><%= libro.getPrecio() %> €</td>
                        <td><%= libro.getStock() %></td>
                        <td class="actions">
                            <a class="btn-link" href="<%= ctx %>/admin/libros?edit=<%= libro.getId() %>">✎</a>
                            <form class="inline-form" method="post" action="<%= ctx %>/admin/libros" onsubmit="return confirm('¿Eliminar este libro?')">
                                <input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="<%= libro.getId() %>">
                                <button class="btn-delete" type="submit">🗑</button>
                            </form>
                        </td>
                    </tr>
                <% }} %>
                </tbody>
            </table>
        </div>
    </section>
    <hr class="separator">
    <section id="usuarios" class="admin-section">
        <header class="section-header"><h1>Clientes Registrados</h1></header>
        <div class="table-container">
            <table>
                <thead><tr><th>ID</th><th>Nombre</th><th>Usuario</th><th>Correo</th><th>Rol</th><th>Fecha Registro</th><th>Acciones</th></tr></thead>
                <tbody>
                <% if (usuarios != null) { for (Usuario u : usuarios) { %>
                    <tr>
                        <td>#USR-<%= u.getId() %></td><td><%= u.getNombre() %></td><td><%= u.getUsername() %></td><td><%= u.getEmail() %></td><td><span class="badge"><%= u.getRol() %></span></td><td><%= u.getFechaRegistro() %></td>
                        <td>
                            <% if (!"admin".equals(u.getUsername())) { %>
                            <form method="post" action="<%= ctx %>/admin/libros" onsubmit="return confirm('¿Eliminar usuario?')">
                                <input type="hidden" name="action" value="deleteUser"><input type="hidden" name="id" value="<%= u.getId() %>">
                                <button class="btn-delete" type="submit">Eliminar</button>
                            </form>
                            <% } else { %><span class="small-muted">Protegido</span><% } %>
                        </td>
                    </tr>
                <% }} %>
                </tbody>
            </table>
        </div>
    </section>
</main>
</body>
</html>

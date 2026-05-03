<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, com.biblieria.model.Pedido, com.biblieria.model.PedidoLinea" %>
<%
    String ctx = request.getContextPath();
    List<Pedido> pedidos = (List<Pedido>) request.getAttribute("pedidos");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= ctx %>/css/theme.css">
    <link rel="stylesheet" href="<%= ctx %>/css/carrito.css">
    <title>Pedidos - Bibliería</title>
</head>
<body>
    <div class="background-overlay"></div>
    <main class="container">
        <header class="cart-header">
            <h1>Mis Pedidos</h1>
            <p>Consulta tus compras confirmadas.</p>
        </header>

        <% if (request.getParameter("ok") != null) { %><div class="cart-alert alert-ok"><%= request.getParameter("ok") %></div><% } %>

        <section class="glass-card pedidos-list">
            <% if (pedidos == null || pedidos.isEmpty()) { %>
                <p class="empty-cart">Todavía no tienes pedidos.</p>
            <% } else { for (Pedido pedido : pedidos) { %>
                <article class="pedido-card">
                    <header class="pedido-head">
                        <div>
                            <h2>Pedido #<%= pedido.getId() %></h2>
                            <p><%= pedido.getFecha() %> · <%= pedido.getEstado() %></p>
                        </div>
                        <strong><%= pedido.getTotal() %> €</strong>
                    </header>
                    <table>
                        <thead>
                            <tr><th>Libro</th><th>Precio</th><th>Cantidad</th><th>Subtotal</th></tr>
                        </thead>
                        <tbody>
                            <% for (PedidoLinea linea : pedido.getLineas()) { %>
                                <tr>
                                    <td><%= linea.getTitulo() %></td>
                                    <td><%= linea.getPrecioUnitario() %> €</td>
                                    <td><%= linea.getCantidad() %></td>
                                    <td><%= linea.getSubtotal() %> €</td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </article>
            <% }} %>
            <a href="<%= ctx %>/catalogo" class="btn-volver">Volver al catálogo</a>
        </section>
    </main>
</body>
</html>

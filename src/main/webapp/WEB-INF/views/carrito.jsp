<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.math.BigDecimal, java.util.*, com.biblieria.model.CarritoItem" %>
<%
    String ctx = request.getContextPath();
    List<CarritoItem> items = (List<CarritoItem>) request.getAttribute("items");
    BigDecimal total = (BigDecimal) request.getAttribute("total");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%= ctx %>/css/theme.css">
    <link rel="stylesheet" href="<%= ctx %>/css/carrito.css">
    <title>Mi Carrito - Bibliería</title>
</head>
<body>
    <div class="background-overlay"></div>

    <main class="container">
        <header class="cart-header">
            <h1>Tu Carrito de Compras</h1>
            <p>Gestiona tus tesoros literarios antes de finalizar.</p>
        </header>

        <% if (request.getParameter("error") != null) { %><div class="cart-alert alert-error"><%= request.getParameter("error") %></div><% } %>

        <div class="carrito-wrapper">
            <section class="carrito-items glass-card">
                <% if (items == null || items.isEmpty()) { %>
                    <p class="empty-cart">Tu carrito está vacío.</p>
                <% } else { %>
                    <table id="tabla-carrito">
                        <thead>
                            <tr>
                                <th>Producto</th>
                                <th>Precio</th>
                                <th>Cantidad</th>
                                <th>Subtotal</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody id="lista-carrito">
                            <% for (CarritoItem item : items) { %>
                                <tr>
                                    <td><%= item.getLibro().getTitulo() %></td>
                                    <td><%= item.getLibro().getPrecio() %> €</td>
                                    <td>
                                        <form class="qty-form" method="post" action="<%= ctx %>/carrito">
                                            <input type="hidden" name="action" value="update">
                                            <input type="hidden" name="libroId" value="<%= item.getLibro().getId() %>">
                                            <input type="number" name="cantidad" min="0" max="<%= item.getLibro().getStock() %>" value="<%= item.getCantidad() %>">
                                            <button type="submit">Actualizar</button>
                                        </form>
                                    </td>
                                    <td><%= item.getSubtotal() %> €</td>
                                    <td>
                                        <form method="post" action="<%= ctx %>/carrito">
                                            <input type="hidden" name="action" value="remove">
                                            <input type="hidden" name="libroId" value="<%= item.getLibro().getId() %>">
                                            <button class="btn-delete-cart" type="submit">Eliminar</button>
                                        </form>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                <% } %>
                <a href="<%= ctx %>/catalogo" class="btn-volver">Continuar Explorando</a>
            </section>

            <aside class="resumen-compra glass-card">
                <h3>Resumen de Pedido</h3>
                <div class="resumen-item">
                    <span>Subtotal:</span>
                    <span id="resumen-subtotal"><%= total %> €</span>
                </div>
                <div class="resumen-item">
                    <span>Gastos de envío:</span>
                    <span class="free">Gratis</span>
                </div>
                <div class="resumen-item">
                    <span>Impuestos (IVA):</span>
                    <span>Incluido</span>
                </div>
                <hr>
                <div class="resumen-item total">
                    <span>Total a pagar:</span>
                    <span id="resumen-total"><%= total %> €</span>
                </div>
                <form method="post" action="<%= ctx %>/carrito">
                    <input type="hidden" name="action" value="checkout">
                    <button id="btn-finalizar" class="btn-checkout" type="submit" <%= items == null || items.isEmpty() ? "disabled" : "" %>>
                        Confirmar Compra
                    </button>
                </form>
            </aside>
        </div>
    </main>
</body>
</html>

package com.biblieria.servlet;

import com.biblieria.dao.LibroDAO;
import com.biblieria.dao.PedidoDAO;
import com.biblieria.model.CarritoItem;
import com.biblieria.model.Libro;
import com.biblieria.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/carrito")
public class CarritoServlet extends HttpServlet {
    private final LibroDAO libroDAO = new LibroDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<CarritoItem> items = buildItems(req.getSession());
            req.setAttribute("items", items);
            req.setAttribute("total", total(items));
            req.getRequestDispatcher("/WEB-INF/views/carrito.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Error cargando carrito", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        try {
            if ("add".equals(action)) {
                add(req);
                resp.sendRedirect(req.getContextPath() + "/catalogo?ok=" + encode("Libro añadido al carrito"));
                return;
            }
            if ("update".equals(action)) {
                update(req);
                resp.sendRedirect(req.getContextPath() + "/carrito");
                return;
            }
            if ("remove".equals(action)) {
                remove(req);
                resp.sendRedirect(req.getContextPath() + "/carrito");
                return;
            }
            if ("checkout".equals(action)) {
                checkout(req, resp);
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/carrito");
        } catch (Exception e) {
            throw new ServletException("Error actualizando carrito", e);
        }
    }

    private void add(HttpServletRequest req) throws Exception {
        int libroId = Integer.parseInt(req.getParameter("libroId"));
        Libro libro = libroDAO.findById(libroId);
        if (libro == null || libro.getStock() <= 0) return;
        Map<Integer, Integer> cart = cart(req.getSession());
        int current = cart.getOrDefault(libroId, 0);
        if (current < libro.getStock()) {
            cart.put(libroId, current + 1);
        }
    }

    private void update(HttpServletRequest req) throws Exception {
        int libroId = Integer.parseInt(req.getParameter("libroId"));
        int cantidad = Integer.parseInt(req.getParameter("cantidad"));
        Map<Integer, Integer> cart = cart(req.getSession());
        if (cantidad <= 0) {
            cart.remove(libroId);
            return;
        }
        Libro libro = libroDAO.findById(libroId);
        if (libro != null) {
            cart.put(libroId, Math.min(cantidad, libro.getStock()));
        }
    }

    private void remove(HttpServletRequest req) {
        int libroId = Integer.parseInt(req.getParameter("libroId"));
        cart(req.getSession()).remove(libroId);
    }

    private void checkout(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            session.setAttribute("postLoginRedirect", "/carrito");
            resp.sendRedirect(req.getContextPath() + "/login?error=" + encode("Debes iniciar sesion para comprar"));
            return;
        }
        List<CarritoItem> items = buildItems(session);
        if (items.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/carrito?error=" + encode("El carrito esta vacio"));
            return;
        }
        int pedidoId = pedidoDAO.crearPedido(usuario.getId(), items);
        cart(session).clear();
        resp.sendRedirect(req.getContextPath() + "/pedidos?ok=" + encode("Pedido #" + pedidoId + " confirmado"));
    }

    private List<CarritoItem> buildItems(HttpSession session) throws Exception {
        List<CarritoItem> items = new ArrayList<>();
        Map<Integer, Integer> cart = cart(session);
        for (Map.Entry<Integer, Integer> entry : new ArrayList<>(cart.entrySet())) {
            Libro libro = libroDAO.findById(entry.getKey());
            if (libro == null) {
                cart.remove(entry.getKey());
                continue;
            }
            int cantidad = Math.min(entry.getValue(), Math.max(libro.getStock(), 0));
            if (cantidad <= 0) continue;
            items.add(new CarritoItem(libro, cantidad));
        }
        return items;
    }

    private BigDecimal total(List<CarritoItem> items) {
        return items.stream().map(CarritoItem::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, Integer> cart(HttpSession session) {
        Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("carrito");
        if (cart == null) {
            cart = new LinkedHashMap<>();
            session.setAttribute("carrito", cart);
        }
        return cart;
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}

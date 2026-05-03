package com.biblieria.servlet;

import com.biblieria.dao.PedidoDAO;
import com.biblieria.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/pedidos")
public class PedidosServlet extends HttpServlet {
    private final PedidoDAO pedidoDAO = new PedidoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Usuario usuario = session == null ? null : (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            String error = URLEncoder.encode("Debes iniciar sesion para ver tus pedidos", StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/login?error=" + error);
            return;
        }

        try {
            if ("ADMIN".equals(usuario.getRol()) && "todos".equals(req.getParameter("scope"))) {
                req.setAttribute("pedidos", pedidoDAO.findAll());
            } else {
                req.setAttribute("pedidos", pedidoDAO.findByUsuario(usuario.getId()));
            }
            req.getRequestDispatcher("/WEB-INF/views/pedidos.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Error cargando pedidos", e);
        }
    }
}

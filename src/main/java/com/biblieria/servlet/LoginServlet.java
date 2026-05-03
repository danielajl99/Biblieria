package com.biblieria.servlet;

import com.biblieria.dao.UsuarioDAO;
import com.biblieria.model.Usuario;
import com.biblieria.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final String CART_ATTR = "carrito";
    private static final String POST_LOGIN_REDIRECT_ATTR = "postLoginRedirect";

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        try {
            Usuario usuario = usuarioDAO.findByUsername(username);
            if (usuario != null && PasswordUtil.verify(password.toCharArray(), usuario.getPasswordHash())) {
                HttpSession old = req.getSession(false);
                Object carrito = old == null ? null : old.getAttribute(CART_ATTR);
                String postLoginRedirect = old == null ? null : (String) old.getAttribute(POST_LOGIN_REDIRECT_ATTR);

                if (old != null) old.invalidate();

                HttpSession session = req.getSession(true);
                session.setAttribute("usuario", usuario);
                if (carrito instanceof Map<?, ?>) {
                    session.setAttribute(CART_ATTR, carrito);
                }
                session.setMaxInactiveInterval(30 * 60);

                resp.sendRedirect(req.getContextPath() + redirectAfterLogin(usuario, postLoginRedirect));
                return;
            }
            req.setAttribute("error", "Usuario o contraseña incorrectos");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Error durante el login", e);
        }
    }

    private String redirectAfterLogin(Usuario usuario, String postLoginRedirect) {
        if (postLoginRedirect != null && postLoginRedirect.startsWith("/")) {
            return postLoginRedirect;
        }
        return "ADMIN".equals(usuario.getRol()) ? "/admin/libros" : "/catalogo";
    }
}

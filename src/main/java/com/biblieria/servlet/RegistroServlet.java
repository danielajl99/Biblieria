package com.biblieria.servlet;

import com.biblieria.dao.UsuarioDAO;
import com.biblieria.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String nombre = clean(req.getParameter("nombre"));
        String email = clean(req.getParameter("email")).toLowerCase();
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");

        try {
            if (nombre.isEmpty() || email.isEmpty() || password == null || password.isBlank()) {
                showError(req, resp, "Todos los campos son obligatorios");
                return;
            }
            if (!password.equals(confirm)) {
                showError(req, resp, "Las contraseñas no coinciden");
                return;
            }
            if (password.length() < 6) {
                showError(req, resp, "La contraseña debe tener al menos 6 caracteres");
                return;
            }
            if (usuarioDAO.existsByEmailOrUsername(email, email)) {
                showError(req, resp, "Ya existe una cuenta con ese correo");
                return;
            }

            String passwordHash = PasswordUtil.hashPassword(password.toCharArray());
            usuarioDAO.createCliente(nombre, email, passwordHash);
            resp.sendRedirect(req.getContextPath() + "/login?ok=Cuenta creada correctamente");
        } catch (Exception e) {
            throw new ServletException("Error durante el registro", e);
        }
    }

    private void showError(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        req.setAttribute("error", message);
        req.setAttribute("nombre", req.getParameter("nombre"));
        req.setAttribute("email", req.getParameter("email"));
        req.getRequestDispatcher("/WEB-INF/views/registro.jsp").forward(req, resp);
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }
}

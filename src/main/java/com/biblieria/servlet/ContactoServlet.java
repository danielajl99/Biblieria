package com.biblieria.servlet;

import com.biblieria.dao.ContactoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/contacto")
public class ContactoServlet extends HttpServlet {
    private final ContactoDAO contactoDAO = new ContactoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/contacto.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            contactoDAO.insert(req.getParameter("nombre"), req.getParameter("email"), req.getParameter("asunto"), req.getParameter("mensaje"));
            req.setAttribute("ok", "Mensaje guardado correctamente en la base de datos.");
            req.getRequestDispatcher("/WEB-INF/views/contacto.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "No se pudo guardar el mensaje: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/contacto.jsp").forward(req, resp);
        }
    }
}

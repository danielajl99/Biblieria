package com.biblieria.servlet;

import com.biblieria.dao.LibroDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {
    private final LibroDAO libroDAO = new LibroDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("libros", libroDAO.findAll());
            req.getRequestDispatcher("/WEB-INF/views/catalogo.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Error cargando catálogo", e);
        }
    }
}

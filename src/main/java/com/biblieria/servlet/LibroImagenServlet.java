package com.biblieria.servlet;

import com.biblieria.dao.LibroDAO;
import com.biblieria.model.Libro;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@WebServlet("/libros/imagen")
public class LibroImagenServlet extends HttpServlet {
    private final LibroDAO libroDAO = new LibroDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Libro libro = libroDAO.findById(id);
            if (libro == null || libro.getImagenRuta() == null || !Files.exists(Path.of(libro.getImagenRuta()))) {
                resp.sendRedirect(req.getContextPath() + "/img/placeholder.svg");
                return;
            }
            resp.setContentType(libro.getImagenMime() == null ? "application/octet-stream" : libro.getImagenMime());
            Files.copy(Path.of(libro.getImagenRuta()), resp.getOutputStream());
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/img/placeholder.svg");
        }
    }
}

package com.biblieria.servlet;

import com.biblieria.dao.LibroDAO;
import com.biblieria.dao.UsuarioDAO;
import com.biblieria.model.Libro;
import com.biblieria.util.CryptoUtil;
import com.biblieria.util.FileUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@WebServlet("/admin/libros")
@MultipartConfig(maxFileSize = 20 * 1024 * 1024, maxRequestSize = 25 * 1024 * 1024)
public class AdminLibrosServlet extends HttpServlet {
    private final LibroDAO libroDAO = new LibroDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String editId = req.getParameter("edit");
            if (editId != null) {
                req.setAttribute("editLibro", libroDAO.findById(Integer.parseInt(editId)));
            }
            loadLists(req);
            req.getRequestDispatcher("/WEB-INF/views/adminLibros.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Error cargando administración", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        try {
            if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Libro actual = libroDAO.findById(id);
                libroDAO.delete(id);
                deleteFileIfExists(actual == null ? null : actual.getImagenRuta());
                resp.sendRedirect(req.getContextPath() + "/admin/libros?ok=Libro eliminado");
                return;
            }
            if ("deleteUser".equals(action)) {
                usuarioDAO.delete(Integer.parseInt(req.getParameter("id")));
                resp.sendRedirect(req.getContextPath() + "/admin/libros?ok=Usuario eliminado");
                return;
            }

            Libro libro = buildLibroFromRequest(req);
            Part imagen = req.getPart("imagen");
            boolean hasNewImage = imagen != null && imagen.getSize() > 0 && imagen.getSubmittedFileName() != null && !imagen.getSubmittedFileName().trim().isEmpty();

            if ("update".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Libro actual = libroDAO.findById(id);
                libro.setId(id);
                if (hasNewImage) {
                    ImagenGuardada guardada = saveImage(imagen);
                    libro.setImagenRuta(guardada.path.toString());
                    libro.setImagenMime(guardada.mime);
                    deleteFileIfExists(actual == null ? null : actual.getImagenRuta());
                } else if (actual != null) {
                    libro.setImagenRuta(actual.getImagenRuta());
                    libro.setImagenMime(actual.getImagenMime());
                }
                libroDAO.update(libro);
                resp.sendRedirect(req.getContextPath() + "/admin/libros?ok=Libro actualizado");
                return;
            }

            if (hasNewImage) {
                ImagenGuardada guardada = saveImage(imagen);
                libro.setImagenRuta(guardada.path.toString());
                libro.setImagenMime(guardada.mime);
            }
            libroDAO.insert(libro);
            resp.sendRedirect(req.getContextPath() + "/admin/libros?ok=Libro creado");
        } catch (Exception e) {
            req.setAttribute("error", "No se pudo realizar la operación: " + e.getMessage());
            try { loadLists(req); } catch (Exception ignored) {}
            req.getRequestDispatcher("/WEB-INF/views/adminLibros.jsp").forward(req, resp);
        }
    }

    private void loadLists(HttpServletRequest req) throws Exception {
        req.setAttribute("libros", libroDAO.findAll());
        req.setAttribute("usuarios", usuarioDAO.findAll());
    }

    private Libro buildLibroFromRequest(HttpServletRequest req) {
        Libro libro = new Libro();
        libro.setTitulo(req.getParameter("titulo"));
        libro.setAutor(req.getParameter("autor"));
        String anio = req.getParameter("anio");
        libro.setAnio(anio == null || anio.trim().isEmpty() ? null : Integer.parseInt(anio));
        libro.setPrecio(new BigDecimal(req.getParameter("precio").replace(',', '.')));
        libro.setStock(Integer.parseInt(req.getParameter("stock")));
        libro.setDescripcion(req.getParameter("descripcion"));
        return libro;
    }

    private ImagenGuardada saveImage(Part part) throws IOException {
        String original = FileUtil.sanitizeFileName(part.getSubmittedFileName());
        String ext = FileUtil.extension(original);
        String savedName = UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
        Path dir = FileUtil.uploadBaseDir().resolve("portadas");
        Files.createDirectories(dir);
        Path destino = dir.resolve(savedName);
        byte[] bytes = CryptoUtil.readAll(part.getInputStream());
        Files.write(destino, bytes);
        String mime = part.getContentType() == null ? "application/octet-stream" : part.getContentType();
        return new ImagenGuardada(destino, mime);
    }

    private void deleteFileIfExists(String path) {
        if (path == null || path.trim().isEmpty()) return;
        try { Files.deleteIfExists(Path.of(path)); } catch (Exception ignored) {}
    }

    private static class ImagenGuardada {
        private final Path path;
        private final String mime;
        private ImagenGuardada(Path path, String mime) { this.path = path; this.mime = mime; }
    }
}

package com.biblieria.dao;

import com.biblieria.config.DB;
import com.biblieria.model.Libro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {
    public List<Libro> findAll() throws SQLException {
        String sql = "SELECT id, titulo, autor, anio, precio, stock, descripcion, imagen_ruta, imagen_mime FROM libros ORDER BY id DESC";
        List<Libro> libros = new ArrayList<>();
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                libros.add(map(rs));
            }
        }
        return libros;
    }

    public Libro findById(int id) throws SQLException {
        String sql = "SELECT id, titulo, autor, anio, precio, stock, descripcion, imagen_ruta, imagen_mime FROM libros WHERE id=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        }
        return null;
    }

    public int insert(Libro libro) throws SQLException {
        String sql = "INSERT INTO libros(titulo, autor, anio, precio, stock, descripcion, imagen_ruta, imagen_mime) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fillCommon(ps, libro);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return 0;
    }

    public boolean update(Libro libro) throws SQLException {
        String sql = "UPDATE libros SET titulo=?, autor=?, anio=?, precio=?, stock=?, descripcion=?, imagen_ruta=?, imagen_mime=? WHERE id=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            fillCommon(ps, libro);
            ps.setInt(9, libro.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM libros WHERE id=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private void fillCommon(PreparedStatement ps, Libro libro) throws SQLException {
        ps.setString(1, libro.getTitulo());
        ps.setString(2, libro.getAutor());
        if (libro.getAnio() == null) ps.setNull(3, java.sql.Types.INTEGER); else ps.setInt(3, libro.getAnio());
        ps.setBigDecimal(4, libro.getPrecio());
        ps.setInt(5, libro.getStock());
        ps.setString(6, libro.getDescripcion());
        ps.setString(7, libro.getImagenRuta());
        ps.setString(8, libro.getImagenMime());
    }

    private Libro map(ResultSet rs) throws SQLException {
        Libro l = new Libro();
        l.setId(rs.getInt("id"));
        l.setTitulo(rs.getString("titulo"));
        l.setAutor(rs.getString("autor"));
        int anio = rs.getInt("anio");
        l.setAnio(rs.wasNull() ? null : anio);
        l.setPrecio(rs.getBigDecimal("precio"));
        l.setStock(rs.getInt("stock"));
        l.setDescripcion(rs.getString("descripcion"));
        l.setImagenRuta(rs.getString("imagen_ruta"));
        l.setImagenMime(rs.getString("imagen_mime"));
        return l;
    }
}

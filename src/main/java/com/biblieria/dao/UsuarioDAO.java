package com.biblieria.dao;

import com.biblieria.config.DB;
import com.biblieria.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    public Usuario findByUsername(String username) throws SQLException {
        String sql = "SELECT id, nombre, email, username, password_hash, rol, fecha_registro FROM usuarios WHERE username=? OR email=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public boolean existsByEmailOrUsername(String email, String username) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE email=? OR username=? LIMIT 1";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void createCliente(String nombre, String email, String passwordHash) throws SQLException {
        String sql = "INSERT INTO usuarios(nombre, email, username, password_hash, rol) VALUES (?, ?, ?, ?, 'CLIENTE')";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, email);
            ps.setString(3, email);
            ps.setString(4, passwordHash);
            ps.executeUpdate();
        }
    }

    public List<Usuario> findAll() throws SQLException {
        String sql = "SELECT id, nombre, email, username, password_hash, rol, fecha_registro FROM usuarios ORDER BY fecha_registro DESC";
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) usuarios.add(map(rs));
        }
        return usuarios;
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id=? AND username <> 'admin'";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Usuario map(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNombre(rs.getString("nombre"));
        u.setEmail(rs.getString("email"));
        u.setUsername(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setRol(rs.getString("rol"));
        java.sql.Timestamp ts = rs.getTimestamp("fecha_registro");
        if (ts != null) u.setFechaRegistro(ts.toLocalDateTime());
        return u;
    }
}

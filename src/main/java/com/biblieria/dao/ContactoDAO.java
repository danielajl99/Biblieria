package com.biblieria.dao;

import com.biblieria.config.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ContactoDAO {
    public void insert(String nombre, String email, String asunto, String mensaje) throws SQLException {
        String sql = "INSERT INTO contactos(nombre, email, asunto, mensaje) VALUES(?,?,?,?)";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, email);
            ps.setString(3, asunto);
            ps.setString(4, mensaje);
            ps.executeUpdate();
        }
    }
}

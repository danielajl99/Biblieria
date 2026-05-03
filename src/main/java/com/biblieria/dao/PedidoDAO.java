package com.biblieria.dao;

import com.biblieria.config.DB;
import com.biblieria.model.CarritoItem;
import com.biblieria.model.Pedido;
import com.biblieria.model.PedidoLinea;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PedidoDAO {
    public int crearPedido(int usuarioId, List<CarritoItem> items) throws SQLException {
        String insertPedido = "INSERT INTO pedidos(usuario_id, total, estado) VALUES (?, ?, 'CONFIRMADO')";
        String insertLinea = "INSERT INTO pedido_lineas(pedido_id, libro_id, titulo, precio_unitario, cantidad, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
        String checkStock = "SELECT stock FROM libros WHERE id=? FOR UPDATE";
        String updateStock = "UPDATE libros SET stock = stock - ? WHERE id=? AND stock >= ?";

        try (Connection con = DB.getConnection()) {
            boolean autoCommit = con.getAutoCommit();
            con.setAutoCommit(false);
            try {
                for (CarritoItem item : items) {
                    try (PreparedStatement ps = con.prepareStatement(checkStock)) {
                        ps.setInt(1, item.getLibro().getId());
                        try (ResultSet rs = ps.executeQuery()) {
                            if (!rs.next() || rs.getInt("stock") < item.getCantidad()) {
                                throw new SQLException("Stock insuficiente para " + item.getLibro().getTitulo());
                            }
                        }
                    }
                }

                BigDecimal total = items.stream()
                        .map(CarritoItem::getSubtotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                int pedidoId;
                try (PreparedStatement ps = con.prepareStatement(insertPedido, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, usuarioId);
                    ps.setBigDecimal(2, total);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException("No se pudo crear el pedido");
                        pedidoId = keys.getInt(1);
                    }
                }

                try (PreparedStatement linePs = con.prepareStatement(insertLinea);
                     PreparedStatement stockPs = con.prepareStatement(updateStock)) {
                    for (CarritoItem item : items) {
                        BigDecimal subtotal = item.getSubtotal();
                        linePs.setInt(1, pedidoId);
                        linePs.setInt(2, item.getLibro().getId());
                        linePs.setString(3, item.getLibro().getTitulo());
                        linePs.setBigDecimal(4, item.getLibro().getPrecio());
                        linePs.setInt(5, item.getCantidad());
                        linePs.setBigDecimal(6, subtotal);
                        linePs.addBatch();

                        stockPs.setInt(1, item.getCantidad());
                        stockPs.setInt(2, item.getLibro().getId());
                        stockPs.setInt(3, item.getCantidad());
                        stockPs.addBatch();
                    }
                    linePs.executeBatch();
                    int[] updated = stockPs.executeBatch();
                    for (int rows : updated) {
                        if (rows == 0) throw new SQLException("No se pudo actualizar el stock");
                    }
                }

                con.commit();
                con.setAutoCommit(autoCommit);
                return pedidoId;
            } catch (SQLException e) {
                con.rollback();
                con.setAutoCommit(autoCommit);
                throw e;
            }
        }
    }

    public List<Pedido> findByUsuario(int usuarioId) throws SQLException {
        String sql = "SELECT id, usuario_id, total, estado, fecha FROM pedidos WHERE usuario_id=? ORDER BY fecha DESC";
        List<Pedido> pedidos = new ArrayList<>();
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) pedidos.add(mapPedido(rs));
            }
        }
        cargarLineas(pedidos);
        return pedidos;
    }

    public List<Pedido> findAll() throws SQLException {
        String sql = "SELECT id, usuario_id, total, estado, fecha FROM pedidos ORDER BY fecha DESC";
        List<Pedido> pedidos = new ArrayList<>();
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) pedidos.add(mapPedido(rs));
        }
        cargarLineas(pedidos);
        return pedidos;
    }

    private void cargarLineas(List<Pedido> pedidos) throws SQLException {
        if (pedidos.isEmpty()) return;
        StringBuilder ids = new StringBuilder();
        for (int i = 0; i < pedidos.size(); i++) {
            if (i > 0) ids.append(',');
            ids.append('?');
        }
        String sql = "SELECT id, pedido_id, libro_id, titulo, precio_unitario, cantidad, subtotal FROM pedido_lineas WHERE pedido_id IN (" + ids + ") ORDER BY id";
        Map<Integer, Pedido> porId = new LinkedHashMap<>();
        for (Pedido pedido : pedidos) porId.put(pedido.getId(), pedido);
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < pedidos.size(); i++) ps.setInt(i + 1, pedidos.get(i).getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PedidoLinea linea = mapLinea(rs);
                    Pedido pedido = porId.get(linea.getPedidoId());
                    if (pedido != null) pedido.getLineas().add(linea);
                }
            }
        }
    }

    private Pedido mapPedido(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setId(rs.getInt("id"));
        p.setUsuarioId(rs.getInt("usuario_id"));
        p.setTotal(rs.getBigDecimal("total"));
        p.setEstado(rs.getString("estado"));
        Timestamp fecha = rs.getTimestamp("fecha");
        if (fecha != null) p.setFecha(fecha.toLocalDateTime());
        return p;
    }

    private PedidoLinea mapLinea(ResultSet rs) throws SQLException {
        PedidoLinea l = new PedidoLinea();
        l.setId(rs.getInt("id"));
        l.setPedidoId(rs.getInt("pedido_id"));
        l.setLibroId(rs.getInt("libro_id"));
        l.setTitulo(rs.getString("titulo"));
        l.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        l.setCantidad(rs.getInt("cantidad"));
        l.setSubtotal(rs.getBigDecimal("subtotal"));
        return l;
    }
}

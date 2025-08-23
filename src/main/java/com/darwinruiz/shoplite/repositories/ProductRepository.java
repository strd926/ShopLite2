package com.darwinruiz.shoplite.repositories;

import com.darwinruiz.shoplite.database.DbConnection;
import com.darwinruiz.shoplite.models.Product;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository implements IProductRepository {
    @Override
    public List<Product> findAll(int offset, int limit) {
        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT id, name, price, stock FROM products ORDER BY id LIMIT ? OFFSET ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, limit);
                ps.setInt(2, offset);
                ResultSet rs = ps.executeQuery();
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    products.add(map(rs));
                }
                return products;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al cargar Productos: " + e.getMessage(), e);
        } finally {
            DbConnection.closeConnection(conn);
        }
    }

    @Override
    public int countAll() {
        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            try (Statement st = conn.createStatement()) {
                ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM products");
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en la cuenta de Productos: " + e.getMessage(), e);
        } finally {
            DbConnection.closeConnection(conn);
        }
    }

    @Override
    public Optional<Product> findById(int id) {
        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT id, name, price, stock FROM products WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("No se encuentra el producto con el Id: " + e.getMessage(), e);
        } finally {
            DbConnection.closeConnection(conn);
        }
    }

    @Override
    public void create(Product p) {
        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO products (name, price, stock) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, p.getName());
                ps.setBigDecimal(2, p.getPrice());
                ps.setInt(3, p.getStock());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear Producto: " + e.getMessage(), e);
        } finally {
            DbConnection.closeConnection(conn);
        }
    }

    @Override
    public void update(Product p) {
        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "UPDATE products SET name = ?, price = ?, stock = ? WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, p.getName());
                ps.setBigDecimal(2, p.getPrice());
                ps.setInt(3, p.getStock());
                ps.setInt(4, p.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el producto: " + e.getMessage(), e);
        } finally {
            DbConnection.closeConnection(conn);
        }
    }

    @Override
    public void deleteById(int id) {
        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "DELETE FROM products WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el producto: " + e.getMessage(), e);
        } finally {
            DbConnection.closeConnection(conn);
        }
    }

    private Product map(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getBigDecimal("price"),
                rs.getInt("stock")
        );
    }
}
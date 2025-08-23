package com.darwinruiz.shoplite.repositories;

import com.darwinruiz.shoplite.database.DbConnection;
import com.darwinruiz.shoplite.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements IUserRepository {
    @Override
    public List<User> findAll(int offset, int limit) {
        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT id, username, password, role, active FROM users ORDER BY id LIMIT ? OFFSET ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, limit);
                ps.setInt(2, offset);
                ResultSet rs = ps.executeQuery();
                List<User> users = new ArrayList<>();
                while (rs.next()) {
                    users.add(map(rs));
                }
                return users;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Falló la conexión", e);
                }
            }
        }
    }

    @Override
    public int countAll() {
        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            try (Statement st = conn.createStatement()) {
                ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM users");
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Falló la conexión", e);
                }
            }
        }
    }

    @Override
    public Optional<User> findById(int id) {
        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT id, username, password, role, active FROM users WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Falló la conexión", e);
                }
            }
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT id, username, password, role, active FROM users WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Falló la conexión", e);
                }
            }
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "SELECT 1 FROM users WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Falló la conexión", e);
                }
            }
        }
    }

    @Override
    public void create(User user) {
        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "INSERT INTO users (username, password, role, active) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getRole());
                ps.setBoolean(4, user.isActive());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar usuario: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Falló la conexión", e);
                }
            }
        }
    }

    @Override
    public void update(User user) {
        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "UPDATE users SET username=?, password=?, role=?, active=? WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getRole());
                ps.setBoolean(4, user.isActive());
                ps.setInt(5, user.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Falló la conexión", e);
                }
            }
        }
    }

    @Override
    public void deleteById(int id) {
        Connection conn = null;
        try {
            conn = DbConnection.getConnection();
            String sql = "DELETE FROM users WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Falló la conexión", e);
                }
            }
        }
    }

    private User map(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getBoolean("active")
        );
    }
}
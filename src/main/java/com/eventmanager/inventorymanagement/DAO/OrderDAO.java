package com.eventmanager.inventorymanagement.DAO;

import com.eventmanager.inventorymanagement.model.Order;
import com.eventmanager.inventorymanagement.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public static void init() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS orders (" +
                "order_id INT PRIMARY KEY AUTO_INCREMENT, customer_id INT, product_id INT, quantity INT, " +
                "order_date TIMESTAMP, order_status VARCHAR(255), shipping_address VARCHAR(255), " +
                "shipping_method VARCHAR(255), payment_method VARCHAR(255), total_price DOUBLE)";
        try (Statement stmt = Database.getConnection().createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    public static void insertOrder(Order order) {
        String sql = "INSERT INTO orders (customer_id, product_id, quantity, order_date, order_status, " +
                "shipping_address, shipping_method, payment_method, total_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = Database.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, order.getCustomerId());
            pstmt.setInt(2, order.getProductId());
            pstmt.setInt(3, order.getQuantity());
            pstmt.setTimestamp(4, new Timestamp(order.getOrderDate().getTime()));
            pstmt.setString(5, order.getOrderStatus());
            pstmt.setString(6, order.getShippingAddress());
            pstmt.setString(7, order.getShippingMethod());
            pstmt.setString(8, order.getPaymentMethod());
            pstmt.setDouble(9, order.getTotalPrice());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Product product = ProductDAO.getProductById(rs.getInt("product_id"));
                Order order = new Order(
                        rs.getInt("order_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getTimestamp("order_date"),
                        rs.getString("order_status"),
                        rs.getString("shipping_address"),
                        rs.getString("shipping_method"),
                        rs.getString("payment_method"),
                        rs.getDouble("total_price"),
                        product
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public static Order getOrderById(int orderId) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        try (PreparedStatement pstmt = Database.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Product product = ProductDAO.getProductById(rs.getInt("product_id"));
                    return new Order(
                            rs.getInt("order_id"),
                            rs.getInt("customer_id"),
                            rs.getInt("product_id"),
                            rs.getInt("quantity"),
                            rs.getTimestamp("order_date"),
                            rs.getString("order_status"),
                            rs.getString("shipping_address"),
                            rs.getString("shipping_method"),
                            rs.getString("payment_method"),
                            rs.getDouble("total_price"),
                            product
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if order not found or an error occurred
    }

}

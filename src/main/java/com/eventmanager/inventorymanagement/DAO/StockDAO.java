package com.eventmanager.inventorymanagement.DAO;

import com.eventmanager.inventorymanagement.model.Stock;
import com.eventmanager.inventorymanagement.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockDAO {

    public static void init() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS stocks (" +
                "stock_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "stock_level INT, stock_name VARCHAR(255), product_id INT)";
        try (Statement stmt = Database.getConnection().createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    public static void insertStock(Stock stock) {
        String sql = "INSERT INTO stocks (stock_level, stock_name, product_id) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = Database.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, stock.getStockLevel());
            pstmt.setString(2, stock.getStockName());
            pstmt.setInt(3, stock.getProduct().getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Stock> getStocks() {
        List<Stock> stocks = new ArrayList<>();
        String sql = "SELECT * FROM stocks";
        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product product = ProductDAO.getProductById(rs.getInt("product_id"));
                stocks.add(new Stock(
                        rs.getInt("stock_id"),
                        rs.getInt("stock_level"),
                        rs.getString("stock_name"),
                        product
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocks;
    }

    public static void updateStock(Stock stock) {
        String sql = "UPDATE stocks SET stock_level = ? WHERE product_id = ?";
        try (PreparedStatement pstmt = Database.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, stock.getStockLevel());
            pstmt.setInt(2, stock.getProduct().getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clearStocksTable() {
        String sql = "DELETE FROM stocks";
        try (Statement stmt = Database.getConnection().createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

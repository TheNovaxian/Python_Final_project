package com.eventmanager.inventorymanagement.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.eventmanager.inventorymanagement.model.Product;
import com.eventmanager.inventorymanagement.model.Order;
import com.eventmanager.inventorymanagement.model.Stock;

public class Database {

    private static final String URL = "jdbc:h2:~/test2;DB_CLOSE_DELAY=-1"; // In-memory database for testing
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static Connection connection;

    // Initialize database connection
    public static void init() {
        try {
            // Load H2 database driver
            Class.forName("org.h2.Driver");
            // Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Create tables if not already exist
            createTables();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Create tables
    public static void createTables() throws SQLException {
        Statement stmt = connection.createStatement();

        // Create products table
        String createProductsTable = "CREATE TABLE IF NOT EXISTS products ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "name VARCHAR(255), "
                + "price DOUBLE, "
                + "quantity INT)";
        stmt.executeUpdate(createProductsTable);

        // Create orders table
        String createOrdersTable = "CREATE TABLE IF NOT EXISTS orders ("
                + "order_id INT PRIMARY KEY AUTO_INCREMENT, "
                + "customer_id INT, "
                + "product_id INT, "
                + "quantity INT, "
                + "order_date TIMESTAMP, "
                + "order_status VARCHAR(255), "
                + "shipping_address VARCHAR(255), "
                + "shipping_method VARCHAR(255), "
                + "payment_method VARCHAR(255), "
                + "total_price DOUBLE)";
        stmt.executeUpdate(createOrdersTable);

        // Create stocks table
        String createStocksTable = "CREATE TABLE IF NOT EXISTS stocks ("
                + "stock_id INT PRIMARY KEY AUTO_INCREMENT, "
                + "stock_level INT, "
                + "stock_name VARCHAR(255), "
                + "product_id INT)";
        stmt.executeUpdate(createStocksTable);

        // Close statement
        stmt.close();
    }

    // Insert a product into the products table
    public static void insertProduct(Product product) {
        String sql = "INSERT INTO products (name, price, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setInt(3, product.getQuantity());

            // Execute the insert statement
            pstmt.executeUpdate();

            // Retrieve the generated keys (auto-generated ID)
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Set the generated ID into the product object
                    product.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    // Get all products from the database
    public static List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                );
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }


    // Insert an order into the orders table
    public static void insertOrder(Order order) {
        String sql = "INSERT INTO orders (customer_id, product_id, quantity, order_date, order_status, " +
                "shipping_address, shipping_method, payment_method, total_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
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

    public static Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("quantity")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Get all orders from the database
    public static List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product product = getProductById(rs.getInt("product_id"));
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
                        product // Here, you can associate the actual Product object
                );
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Insert stock information
    public static void insertStock(Stock stock) {
        String sql = "INSERT INTO stocks (stock_level, stock_name, product_id) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, stock.getStockLevel());
            pstmt.setString(2, stock.getStockName());
            pstmt.setInt(3, stock.getProduct().getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all stocks from the database
    public static List<Stock> getStocks() {
        List<Stock> stocks = new ArrayList<>();
        String sql = "SELECT * FROM stocks";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Stock stock = new Stock(
                        rs.getInt("stock_id"),
                        rs.getInt("stock_level"),
                        rs.getString("stock_name"),
                        new Product(rs.getInt("product_id"), "Placeholder", 0.0, 0) // Use Product ID to fetch product details
                );
                stocks.add(stock);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocks;
    }

    public static void updateStock(Stock stock) {
        String sql = "UPDATE stocks SET stock_level = ? WHERE product_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, stock.getStockLevel());
            stmt.setInt(2, stock.getProduct().getId());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Stock updated successfully for product ID " + stock.getProduct().getId());
            } else {
                System.out.println("No stock record found for product ID " + stock.getProduct().getId());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Delete stock associated with the product
            String deleteStockSql = "DELETE FROM stocks WHERE product_id = ?";
            try (PreparedStatement deleteStockPstmt = connection.prepareStatement(deleteStockSql)) {
                deleteStockPstmt.setInt(1, productId);
                deleteStockPstmt.executeUpdate();
            }

            // Now delete the product itself
            pstmt.setInt(1, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void clearStocksTable() {
        String sql = "DELETE FROM stocks";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql); // Clear the stocks table
            System.out.println("Stocks table cleared successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

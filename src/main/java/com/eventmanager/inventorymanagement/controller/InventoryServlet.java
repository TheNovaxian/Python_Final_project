package com.eventmanager.inventorymanagement.controller;

import com.eventmanager.inventorymanagement.DAO.Database;
import com.eventmanager.inventorymanagement.model.*;
import com.eventmanager.inventorymanagement.DAO.ProductDAO;
import com.eventmanager.inventorymanagement.DAO.StockDAO;
import com.eventmanager.inventorymanagement.DAO.OrderDAO;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;



@WebServlet("/inventory")
public class InventoryServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize the database connection and create tables if needed
        Database.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        String action = req.getParameter("action");
        if (action == null) action = "home";

        out.println("<html><head><title>Inventory Manager</title>");
        out.println("<link href='./css/styles.css' rel='stylesheet'>");
        out.println("<link href='https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css' rel='stylesheet'></head>");
        out.println("<body class='container'>");

        out.println("<h1 class='my-4'>Inventory Management</h1>");
        out.println("<nav class='navbar navbar-expand-lg navbar-dark bg-primary custom-navbar'>");  // Updated classes
        out.println("<a class='navbar-brand' href='inventory'>Inventory Manager</a>");  // Updated text here
        out.println("<button class='navbar-toggler' type='button' data-toggle='collapse' data-target='#navbarNav' aria-controls='navbarNav' aria-expanded='false' aria-label='Toggle navigation'>");
        out.println("<span class='navbar-toggler-icon'></span>");
        out.println("</button>");

        out.println("<div class='collapse navbar-collapse' id='navbarNav'>");
        out.println("<ul class='navbar-nav ml-auto'>");  // ml-auto for right-aligning the navbar items
        out.println("<li class='nav-item'><a class='nav-link' href='?action=viewProducts'>View Products</a></li>");
        out.println("<li class='nav-item'><a class='nav-link' href='?action=viewStock'>View Stock</a></li>");
        out.println("<li class='nav-item'><a class='nav-link' href='?action=placeOrderForm'>Place Order</a></li>");
        out.println("<li class='nav-item'><a class='nav-link' href='?action=viewOrders'>View Orders</a></li>");
        out.println("<li class='nav-item'><a class='nav-link' href='?action=addProductForm'>Add Product</a></li>");
        out.println("<li class='nav-item'><a class='nav-link' href='?action=viewReport'>Generate Report</a></li>");
        out.println("</ul>");
        out.println("</div>");
        out.println("</nav>");
        out.println("<hr>");


        switch (action) {
            case "viewProducts":
                displayProducts(out);
                break;

            case "viewStock":
                displayStock(out);
                break;

            case "placeOrderForm":
                displayPlaceOrderForm(out);
                break;

            case "addProductForm":
                displayAddProductForm(out); // Display form to add new product
                break;

            case "deleteProduct":
                deleteProduct(req, out); // Handle product deletion
                break;

            case "placeOrder":
                placeOrder(req, out);
                break;

            case "viewOrders":
                displayOrders(out);
                break;

            case "viewReport":
                displayReportLink(out);
                break;

            default:
                out.println("<p>Welcome to the inventory system! Choose an action above.</p>");
                break;
        }

        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "home";

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<html><head><title>Inventory Manager</title>");
        out.println("<link href='https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css' rel='stylesheet'></head>");
        out.println("<link href='css/styles.css' rel='stylesheet'>");
        out.println("<body class='container'>");
        out.println("<h1 class='my-4'>Inventory Management</h1>");
        out.println("<nav class='navbar navbar-expand-lg navbar-light bg-light'>");
        out.println("<a class='navbar-brand' href='/inventory'>Home</a>");
        out.println("<div class='collapse navbar-collapse'>");
        out.println("<ul class='navbar-nav mr-auto'>");
        out.println("<li class='nav-item'><a class='nav-link' href='?action=viewProducts'>View Products</a></li>");
        out.println("<li class='nav-item'><a class='nav-link' href='?action=viewStock'>View Stock</a></li>");
        out.println("<li class='nav-item'><a class='nav-link' href='?action=placeOrderForm'>Place Order</a></li>");
        out.println("<li class='nav-item'><a class='nav-link' href='?action=viewReport'>Generate Report</a></li>");

        out.println("</ul></div></nav><hr>");

        switch (action) {
            case "addProduct":
                addProduct(req, out); // Process adding product
                break;

            case "placeOrder":
                placeOrder(req, out);
                break;

            default:
                out.println("<p>Welcome to the inventory system! Choose an action above.</p>");
                break;
        }

        out.println("</body></html>");
    }

    private void displayOrders(PrintWriter out) {
        List<Order> orders = OrderDAO.getOrders();

        out.println("<h2>Order List</h2>");
        if (orders.isEmpty()) {
            out.println("<p>No orders found.</p>");
        } else {
            out.println("<table class='table table-striped'>");
            out.println("<thead><tr><th>ID</th><th>Product</th><th>Quantity</th><th>Total</th><th>Status</th><th>Shipping</th><th>Payment</th><th>Details</th></tr></thead>");
            out.println("<tbody>");
            for (Order o : orders) {
                out.printf("<tr><td>%d</td><td>%s</td><td>%d</td><td>$%.2f</td><td>%s</td><td>%s</td><td>%s</td><td><a href='order-details?id=%d'>View</a></td></tr>",
                        o.getOrderId(), o.getProduct().getName(), o.getQuantity(),
                        o.getTotalPrice(), o.getOrderStatus(),
                        o.getShippingMethod(), o.getPaymentMethod(), o.getOrderId());
            }
            out.println("</tbody></table>");
        }
    }


    private void deleteProduct(HttpServletRequest req, PrintWriter out) {
        try {
            // Get the product ID from the request
            int productId = Integer.parseInt(req.getParameter("id"));

            // Call the Database method to delete the product
            ProductDAO.deleteProduct(productId);

            // Show success message and link back to the product list
            out.println("<h3>Product Deleted Successfully!</h3>");
            out.println("<a href='?action=viewProducts'>Back to Product List</a>");
        } catch (NumberFormatException e) {
            out.println("<p>Error: Invalid product ID.</p>");
        }
    }


    private void displayProducts(PrintWriter out) {
        List<Product> products = ProductDAO.getProducts();
        out.println("<h2>Product List</h2>");
        if (products.isEmpty()) {
            out.println("<p>No products available.</p>");
        } else {
            out.println("<table class='table table-bordered table-hover'>");
            out.println("<thead class='thead-dark'><tr><th>ID</th><th>Name</th><th>Price</th></tr></thead>");
            out.println("<tbody>");
            for (Product p : products) {
                out.printf(
                        "<tr><td>%d</td><td>%s</td><td>$%.2f</td><td><a class='btn btn-block btn-danger' href='?action=deleteProduct&id=%d'>Delete</a></td></tr>",
                        p.getId(), p.getName(), p.getPrice(), p.getQuantity(), p.getId()
                );
            }
            out.println("</tbody></table>");
        }
    }


    private void displayStock(PrintWriter out) {
        List<Stock> stocks = StockDAO.getStocks();
        out.println("<h2>Stock Levels</h2>");
        if (stocks.isEmpty()) {
            out.println("<p>No stock records found.</p>");
        } else {
            out.println("<table class='table table-bordered table-striped'>");
            out.println("<thead class='thead-dark'><tr><th>Stock ID</th><th>Product ID</th><th>Product Name</th><th>Available Units</th></tr></thead>");
            out.println("<tbody>");
            for (Stock s : stocks) {
                out.printf(
                        "<tr><td>%d</td><td>%d</td><td>%s</td><td>%d</td></tr>",
                        s.getStockID(), s.getProduct().getId(), s.getStockName(), s.getStockLevel()
                );
            }
            out.println("</tbody></table>");
        }
    }



    private void displayAddProductForm(PrintWriter out) {
        out.println("<h2>Add Product</h2>");
        out.println("<form action='?action=addProduct' method='POST'>");
        out.println("<input type='hidden' name='action' value='addProduct'>");
        out.println("<div class='form-group'>");
        out.println("<label for='productName'>Product Name:</label>");
        out.println("<input type='text' class='form-control' id='productName' name='productName' required>");
        out.println("</div>");
        out.println("<div class='form-group'>");
        out.println("<label for='productPrice'>Product Price:</label>");
        out.println("<input type='number' class='form-control' id='productPrice' name='productPrice' required>");
        out.println("</div>");
        out.println("<div class='form-group'>");
        out.println("<label for='productQuantity'>Product Quantity:</label>");
        out.println("<input type='number' class='form-control' id='productQuantity' name='productQuantity' required>");
        out.println("</div>");
        out.println("<button type='submit' class='btn btn-primary'>Add Product</button>");
        out.println("</form>");
    }

    private void addProduct(HttpServletRequest req, PrintWriter out) {
        String productName = req.getParameter("productName");
        double productPrice = Double.parseDouble(req.getParameter("productPrice"));
        int productQuantity = Integer.parseInt(req.getParameter("productQuantity"));

        // Create the Product object first
        Product product = new Product(0, productName, productPrice, productQuantity); // ID will be auto-generated
        ProductDAO.insertProduct(product);

        // Create the Stock object now, linked with the newly created Product
        Stock stock = new Stock(0, productQuantity, productName, product); // Passing the product to the stock
        StockDAO.insertStock(stock);  // Insert stock record into the database

        // Display success message
        out.println("<h3>Product and Stock added successfully!</h3>");
        out.printf("<p>Product Name: %s</p>", product.getName());
        out.printf("<p>Price: $%.2f</p>", product.getPrice());
        out.printf("<p>Quantity: %d</p>", product.getQuantity());
    }


    private void displayPlaceOrderForm(PrintWriter out) {
        List<Product> products = ProductDAO.getProducts();

        out.println("<h2>Place Order</h2>");
        out.println("<form action='?action=placeOrder' method='POST'>");
        out.println("<div class='form-group'>");
        out.println("<label for='product'>Select Product:</label>");
        out.println("<select class='form-control' id='product' name='productId'>");

        for (Product p : products) {
            out.printf("<option value='%d'>%s - $%.2f</option>", p.getId(), p.getName(), p.getPrice());
        }

        out.println("</select>");
        out.println("</div>");

        // Add customer name
        out.println("<div class='form-group'>");
        out.println("<label for='customerName'>Customer Name:</label>");
        out.println("<input type='text' class='form-control' id='customerName' name='customerName' required>");
        out.println("</div>");

        // Add shipping address
        out.println("<div class='form-group'>");
        out.println("<label for='shippingAddress'>Shipping Address:</label>");
        out.println("<textarea class='form-control' id='shippingAddress' name='shippingAddress' required></textarea>");
        out.println("</div>");

        // Add shipping method
        out.println("<div class='form-group'>");
        out.println("<label for='shippingMethod'>Shipping Method:</label>");
        out.println("<select class='form-control' id='shippingMethod' name='shippingMethod' required>");
        out.println("<option value='Standard'>Standard</option>");
        out.println("<option value='Express'>Express</option>");
        out.println("</select>");
        out.println("</div>");

        // Add payment method
        out.println("<div class='form-group'>");
        out.println("<label for='paymentMethod'>Payment Method:</label>");
        out.println("<select class='form-control' id='paymentMethod' name='paymentMethod' required>");
        out.println("<option value='Credit Card'>Credit Card</option>");
        out.println("<option value='PayPal'>PayPal</option>");
        out.println("</select>");
        out.println("</div>");

        // Quantity
        out.println("<div class='form-group'>");
        out.println("<label for='quantity'>Quantity:</label>");
        out.println("<input type='number' class='form-control' id='quantity' name='quantity' min='1' required>");
        out.println("</div>");

        out.println("<button type='submit' class='btn btn-primary'>Place Order</button>");
        out.println("</form>");
    }

    private void placeOrder(HttpServletRequest req, PrintWriter out) {
        int productId = Integer.parseInt(req.getParameter("productId"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        String customerName = req.getParameter("customerName");
        String shippingAddress = req.getParameter("shippingAddress");
        String shippingMethod = req.getParameter("shippingMethod");
        String paymentMethod = req.getParameter("paymentMethod");

        Product product = null;
        for (Product p : ProductDAO.getProducts()) {
            if (p.getId() == productId) {
                product = p;
                break;
            }
        }

        if (product == null) {
            out.println("<p>Product not found.</p>");
            return;
        }

        Stock stock = null;
        for (Stock s : StockDAO.getStocks()) {
            if (s.getProduct().getId() == productId) {
                stock = s;
                break;
            }
        }

        if (stock != null && stock.getStockLevel() >= quantity) {
            stock.reduceStock(quantity); // reduce stock
            StockDAO.updateStock(stock); // 🔁 persist change in database if needed

            Order order = new Order(1, 123, product.getId(), quantity, new java.util.Date(),
                    "Processing", shippingAddress, shippingMethod, paymentMethod, product.getPrice() * quantity, product);
            OrderDAO.insertOrder(order);

            out.println("<h3>Order Placed Successfully!</h3>");
            out.printf("<p>Order Details:</p>");
            out.printf("<p>Customer: %s</p>", customerName);
            out.printf("<p>Product: %s</p>", product.getName());
            out.printf("<p>Quantity: %d</p>", quantity);
            out.printf("<p>Total Price: $%.2f</p>", order.getTotalPrice());
            out.printf("<p>Shipping Address: %s</p>", shippingAddress);
            out.printf("<p>Shipping Method: %s</p>", shippingMethod);
            out.printf("<p>Payment Method: %s</p>", paymentMethod);
            out.printf("<p>Remaining stock: %d</p>", stock.getStockLevel());
        } else {
            out.println("<p>Not enough stock to fulfill order.</p>");
        }
    }

    private void displayReportLink(PrintWriter out) {
        // Here, you can either generate a static report or display a link for downloading the report.
        out.println("<h2>Generate Product Report</h2>");
        out.println("<p><a href='generateReport'>Click here for reports.</a></p>");
    }


}

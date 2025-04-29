package com.eventmanager.inventorymanagement.controller;

import com.eventmanager.inventorymanagement.DAO.ProductDAO;
import com.eventmanager.inventorymanagement.DAO.StockDAO;
import com.eventmanager.inventorymanagement.DAO.OrderDAO;
import com.eventmanager.inventorymanagement.model.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/generateReport")
public class ReportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        // HTML + Bootstrap
        out.println("<html><head><title>Inventory Reports</title>");
        out.println("<link href='https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css' rel='stylesheet'>");
        out.println("<link href='./css/styles.css' rel='stylesheet'>");
        out.println("</head><body class='container'>");

        out.println("<h1 class='my-4'>Inventory Reports</h1>");

        // Navigation
        out.println("<ul class='nav nav-tabs mb-4'>");
        out.println("<li class='nav-item'><a class='nav-link active' href='#products' data-toggle='tab'>Product Report</a></li>");
        out.println("<li class='nav-item'><a class='nav-link' href='#stocks' data-toggle='tab'>Stock Report</a></li>");
        out.println("<li class='nav-item'><a class='nav-link' href='#orders' data-toggle='tab'>Order Report</a></li>");
        out.println("</ul>");

        out.println("<div class='tab-content'>");

        // Product Report
        out.println("<div class='tab-pane fade show active' id='products'>");
        List<Product> products = ProductDAO.getProducts();
        out.println("<h2>Product Report</h2>");
        out.println("<button onclick=\"window.history.back();\" class=\"btn btn-secondary mt-3\">Go Back</button>");
        if (products.isEmpty()) {
            out.println("<p>No products found.</p>");
        } else {
            out.println("<table class='table table-bordered table-striped'>");
            out.println("<thead><tr><th>ID</th><th>Name</th><th>Price</th><th>Quantity</th></tr></thead><tbody>");
            for (Product p : products) {
                out.printf("<tr><td>%d</td><td>%s</td><td>$%.2f</td><td>%d</td></tr>",
                        p.getId(), p.getName(), p.getPrice(), p.getQuantity());
            }
            out.println("</tbody></table>");
        }
        out.println("<a href=\"downloadProductReport\" class=\"btn btn-primary mb-3\">Download Product Report</a>");
        out.println("</div>");

        // Stock Report
        out.println("<div class='tab-pane fade' id='stocks'>");
        List<Stock> stocks = StockDAO.getStocks();
        out.println("<h2>Stock Report</h2>");
        out.println("<button onclick=\"window.history.back();\" class=\"btn btn-secondary mt-3\">Go Back</button>");
        if (stocks.isEmpty()) {
            out.println("<p>No stock records found.</p>");
        } else {
            out.println("<table class='table table-bordered table-striped'>");
            out.println("<thead><tr><th>Stock ID</th><th>Product</th><th>Stock Level</th></tr></thead><tbody>");
            for (Stock s : stocks) {
                out.printf("<tr><td>%d</td><td>%s</td><td>%d</td></tr>",
                        s.getStockID(), s.getStockName(), s.getStockLevel());
            }
            out.println("</tbody></table>");
        }
        out.println("<a href=\"downloadStockReport\" class=\"btn btn-primary mb-3\">Download Stock Report</a>");
       ;

        out.println("</div>");

        // Order Report
        out.println("<div class='tab-pane fade' id='orders'>");
        List<Order> orders = OrderDAO.getOrders();
        out.println("<h2>Order Report</h2>");
        out.println("<button onclick=\"window.history.back();\" class=\"btn btn-secondary mt-3\">Go Back</button>");
        if (orders.isEmpty()) {
            out.println("<p>No orders found.</p>");
        } else {
            out.println("<table class='table table-bordered table-striped'>");
            out.println("<thead><tr><th>ID</th><th>Product</th><th>Quantity</th><th>Total</th><th>Status</th><th>Shipping</th><th>Payment</th></tr></thead><tbody>");
            for (Order o : orders) {
                out.printf("<tr><td>%d</td><td>%s</td><td>%d</td><td>$%.2f</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                        o.getOrderId(), o.getProduct().getName(), o.getQuantity(), o.getTotalPrice(),
                        o.getOrderStatus(), o.getShippingMethod(), o.getPaymentMethod());
            }
            out.println("</tbody></table>");
        }
        out.println("<a href=\"downloadOrderReport\" class=\"btn btn-primary mb-3\">Download Order Report</a>");
        out.println("</div>");

        out.println("</div>"); // Close tab content

        // Including JavaScript for Bootstrap's tab functionality
        out.println("<script src='https://code.jquery.com/jquery-3.5.1.slim.min.js'></script>");
        out.println("<script src='https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js'></script>");
        out.println("<script src='https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js'></script>");

        out.println("</body></html>");
    }
}

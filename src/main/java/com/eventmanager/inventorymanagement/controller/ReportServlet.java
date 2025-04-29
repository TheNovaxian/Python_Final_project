package com.eventmanager.inventorymanagement.controller;

import com.eventmanager.inventorymanagement.DAO.ProductDAO;
import com.eventmanager.inventorymanagement.model.Product;
import com.eventmanager.inventorymanagement.model.Report;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/generateReport")
public class ReportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Assuming you have a method to get products and generate a report
        List<Product> products = ProductDAO.getProducts(); // This would fetch products from the database
        Report.generateProductReport(products); // You can modify this to store the report to show later

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html><head><title>Product Report</title></head><body>");
        out.println("<h2>Product Report</h2>");

        // Displaying the product report here
        for (Product product : products) {
            out.printf("<p>ID: %d, Name: %s, Price: %.2f, Quantity: %d</p>",
                    product.getId(), product.getName(), product.getPrice(), product.getQuantity());
        }

        out.println("</body></html>");
    }
}

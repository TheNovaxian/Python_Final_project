package com.eventmanager.inventorymanagement.controller;

import com.eventmanager.inventorymanagement.DAO.ProductDAO;
import com.eventmanager.inventorymanagement.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import javax.swing.text.Document;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/downloadProductReport")
public class DownloadProductReportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Product> products = ProductDAO.getProducts();

        resp.setContentType("text/plain");
        resp.setHeader("Content-Disposition", "attachment; filename=product_report.txt");

        PrintWriter out = resp.getWriter();
        out.println("Product Report");
        out.println("==============");
        for (Product p : products) {
            out.printf("ID: %d, Name: %s, Price: $%.2f, Quantity: %d%n",
                    p.getId(), p.getName(), p.getPrice(), p.getQuantity());
        }
        out.close();
    }
}

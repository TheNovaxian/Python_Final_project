package com.eventmanager.inventorymanagement.controller;

import com.eventmanager.inventorymanagement.DAO.OrderDAO;
import com.eventmanager.inventorymanagement.model.Order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/downloadOrderReport")
public class DownloadOrderReportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Get list of orders
        List<Order> orders = OrderDAO.getOrders();

        // Set response content type to text/plain for a text file
        resp.setContentType("text/plain");
        resp.setHeader("Content-Disposition", "attachment; filename=order_report.txt");

        // Output the order information to the text file
        PrintWriter out = resp.getWriter();
        out.println("Order Report");
        out.println("=============");
        for (Order o : orders) {
            out.printf("Order ID: %d, Product: %s, Quantity: %d, Total: $%.2f, Status: %s, Shipping: %s, Payment: %s%n",
                    o.getOrderId(), o.getProduct().getName(), o.getQuantity(), o.getTotalPrice(),
                    o.getOrderStatus(), o.getShippingMethod(), o.getPaymentMethod());
        }
        out.close();
    }
}

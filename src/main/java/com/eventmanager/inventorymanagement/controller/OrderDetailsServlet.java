package com.eventmanager.inventorymanagement.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.eventmanager.inventorymanagement.DAO.Database;
import com.eventmanager.inventorymanagement.model.*;
import com.eventmanager.inventorymanagement.DAO.ProductDAO;
import com.eventmanager.inventorymanagement.DAO.StockDAO;
import com.eventmanager.inventorymanagement.DAO.OrderDAO;

import java.io.IOException;

@WebServlet("/order-details")
public class OrderDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int orderId = Integer.parseInt(req.getParameter("id"));
        Order order = OrderDAO.getOrderById(orderId);

        if (order != null) {
            req.setAttribute("order", order);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/order-details.jsp");
            dispatcher.forward(req, resp);
        } else {
            resp.getWriter().println("<p>Order not found.</p>");
        }
    }
}

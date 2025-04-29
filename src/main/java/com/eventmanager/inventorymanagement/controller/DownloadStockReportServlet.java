package com.eventmanager.inventorymanagement.controller;

import com.eventmanager.inventorymanagement.DAO.StockDAO;
import com.eventmanager.inventorymanagement.model.Stock;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/downloadStockReport")
public class DownloadStockReportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Get list of stocks
        List<Stock> stocks = StockDAO.getStocks();

        // Set response content type to text/plain for a text file
        resp.setContentType("text/plain");
        resp.setHeader("Content-Disposition", "attachment; filename=stock_report.txt");

        // Output the stock information to the text file
        PrintWriter out = resp.getWriter();
        out.println("Stock Report");
        out.println("=============");
        for (Stock s : stocks) {
            out.printf("Stock ID: %d, Product: %s, Stock Level: %d%n",
                    s.getStockID(), s.getStockName(), s.getStockLevel());
        }
        out.close();
    }
}

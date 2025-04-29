package com.eventmanager.inventorymanagement.model;

import java.util.List;
import java.util.Objects;

public class Report {

    // Method to generate a product report
    public static void generateProductReport(List<Product> products) {
        if (products == null || products.isEmpty()) {
            System.out.println("No products available to generate the report.");
            return;
        }

        System.out.println("Product Report:");
        System.out.println("----------------------------------------------------");
        System.out.println(String.format("%-10s %-20s %-10s %-10s", "ID", "Name", "Price", "Quantity"));
        System.out.println("----------------------------------------------------");

        for (Product product : products) {
            System.out.println(String.format("%-10s %-20s %-10.2f %-10d", product.getId(), product.getName(), product.getPrice(), product.getQuantity()));
        }
        System.out.println("----------------------------------------------------");
    }

    // Method to generate an order report
    public static void generateOrderReport(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            System.out.println("No orders available to generate the report.");
            return;
        }

        System.out.println("Order Report:");
        System.out.println("----------------------------------------------------");
        System.out.println(String.format("%-10s %-15s %-20s %-10s %-15s", "Order ID", "Customer ID", "Product", "Quantity", "Total Price"));
        System.out.println("----------------------------------------------------");

        for (Order order : orders) {
            System.out.println(String.format("%-10d %-15d %-20s %-10d %-15.2f", order.getOrderId(), order.getCustomerId(),
                    order.getProduct().getName(), order.getQuantity(), order.getTotalPrice()));
        }
        System.out.println("----------------------------------------------------");
    }

    // Method to generate a stock report
    public static void generateStockReport(List<Stock> stocks) {
        if (stocks == null || stocks.isEmpty()) {
            System.out.println("No stocks available to generate the report.");
            return;
        }

        System.out.println("Stock Report:");
        System.out.println("----------------------------------------------------");
        System.out.println(String.format("%-10s %-20s %-10s", "Stock ID", "Stock Name", "Level"));
        System.out.println("----------------------------------------------------");

        for (Stock stock : stocks) {
            System.out.println(String.format("%-10d %-20s %-10d", stock.getStockID(), stock.getStockName(), stock.getStockLevel()));
        }
        System.out.println("----------------------------------------------------");
    }

    // Method to generate a supplier report
    public static void generateSupplierReport(List<Supplier> suppliers) {
        if (suppliers == null || suppliers.isEmpty()) {
            System.out.println("No suppliers available to generate the report.");
            return;
        }

        System.out.println("Supplier Report:");
        System.out.println("----------------------------------------------------");
        System.out.println(String.format("%-10s %-20s %-30s %-15s %-30s", "ID", "Name", "Address", "Phone", "Email"));
        System.out.println("----------------------------------------------------");

        for (Supplier supplier : suppliers) {
            System.out.println(String.format("%-10d %-20s %-30s %-15s %-30s", supplier.getId(), supplier.getName(), supplier.getAddress(),
                    supplier.getPhone(), supplier.getEmail()));
        }
        System.out.println("----------------------------------------------------");
    }
}

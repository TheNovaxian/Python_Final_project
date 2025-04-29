package com.eventmanager.inventorymanagement.model;

import java.util.List;

public class ReportsGenerator {

    //method to generate a product report
    public static void generateProductReport(List<Product> products) {
        System.out.println("Product Reports:");
        for (Product product : products) {
            System.out.println("ID: " + product.getId() + ", Name: " + product.getName() + ", Price: " + product.getPrice() + ", Quantity: " + product.getQuantity());
        }
        System.out.println("----------------------------");
    }

    //method to generate a order report
    public static void generateOrderReport(List<Order> orders) {
        System.out.println("Order Reports:");
        for (Order order : orders) {
            System.out.println("Order ID: " + order.getOrderId() + ", Customer ID: " + order.getCustomerId() + ",  Product: " + order.getProduct().getName() + ", Quantity: " + order.getQuantity() + ", Total Price: " + order.getTotalPrice());
        }
        System.out.println("----------------------------");
    }

    //method to generate a stock report
    public static void generateStockReport(List<Stock> stocks) {
        System.out.println("Stock Reports:");
        for (Stock stock : stocks) {
            System.out.println("Stock ID: " + stock.getStockID() + ", Stock Name: " + stock.getStockName() + ", Level: " + stock.getStockLevel());
        }
        System.out.println("----------------------------");
    }

    //method to generate supplier report
    public static void generateSupplierReport(List<Supplier> suppliers) {
        System.out.println("Supplier Reports:");
        for (Supplier supplier : suppliers) {
            System.out.println("ID: " + supplier.getId() + ", Name: " + supplier.getName() + ", Address: " + supplier.getAddress() + ", Phone: " + supplier.getPhone() + ", Email: " + supplier.getEmail());
        }
        System.out.println("----------------------------");
    }
}

package com.eventmanager.inventorymanagement.model;

import com.eventmanager.inventorymanagement.database.Database;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {

        //create our inventory manager
        InventoryManager inventoryManager = new InventoryManager();

        //add products
        Product cheese = inventoryManager.addProduct("cheese",20.00,200);

        //print stock
        Stock cheeseStock = inventoryManager.getStocks().get(0);

        // Print initial stock details
        System.out.println("Initial stock for cheese: " + cheeseStock);

        //reduce stock
        cheeseStock.reduceStock(50);
        System.out.println("stock for cheese after: " + cheeseStock);

        // Initialize database connection (if needed)
        Database.init();  // If you have an init method to set up the connection

        // Call the clearStocks() method from the Database class
        Database.clearStocksTable();  // This will clear the stocks table
        Database.createTables();

        // Optionally, you can print a message to indicate that the operation was successful
        System.out.println("Stocks table has been cleared.");

        //generate reports
        inventoryManager.generateReports();


    }



}

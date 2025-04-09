package com.eventmanager.inventorymanagement.model;

public class Main {

    public static void main(String[] args) {

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


    }



}

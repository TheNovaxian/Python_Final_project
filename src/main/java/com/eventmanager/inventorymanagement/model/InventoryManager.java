package com.eventmanager.inventorymanagement.model;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private List<Order> orders;
    private List<Stock> stocks;
    private List<Supplier> suppliers;
    private List<Product> products;

    private int id;

    public InventoryManager() {
        orders = new ArrayList<Order>();
        stocks = new ArrayList<Stock>();
        suppliers = new ArrayList<Supplier>();
        products = new ArrayList<Product>();
        id = 0;
    }

    //method to add product
    public Product addProduct(String name, double price, int quantity) {
        Product product = new Product(id++,name,price,quantity);

        Stock stock = new Stock(id++,quantity,name,product);

        products.add(product);
        stocks.add(stock);

        return product;

    }

    // Method to get the list of all stocks
    public List<Stock> getStocks() {
        return stocks;
    }


}

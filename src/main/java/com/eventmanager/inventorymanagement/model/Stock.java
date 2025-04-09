package com.eventmanager.inventorymanagement.model;

public class Stock {
    private int stockID;
    private int stockLevel;
    private String stockName;
    private Product product;


    public Stock(int stockID, int stockLevel, String stockName, Product product) {
        this.stockID = stockID;
        this.stockLevel = stockLevel;
        this.stockName = stockName;
        this.product = product;
    }

    //getters and setters
    public int getStockID() {
        return stockID;
    }
    public void setStockID(int stockID) {
        this.stockID = stockID;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    //method to reduce stock level
    public boolean reduceStock(int quantity) {
         if(quantity <= stockLevel) {
             stockLevel -= quantity;
             System.out.println("Stock reduced by " + quantity + " to " + stockLevel);
             return true;
         }else {
             System.out.println("Not enough stock for " + quantity);
             return false;
         }
    }

    public void restock(int quantity) {
        stockLevel += quantity;
        System.out.println("Stock restocked by " + quantity + ". New stock level: " + stockLevel);
    }

   // override to string
    @Override
    public String toString() {
        return "Stock{" + "stockID=" + stockID + ", stockLevel=" + stockLevel + ", stockName=" + stockName + '}';
    }


}

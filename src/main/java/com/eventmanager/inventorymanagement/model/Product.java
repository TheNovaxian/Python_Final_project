package com.eventmanager.inventorymanagement.model;

import java.util.List;

public class Product {


    private int id;
    private String name;
    private double price;
    int quantity;



    public Product(int id, String name, double price,int quantity ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }



//    public List<Supplier> getListofSuplliers() {
//        return listofSuplliers;
//    }
//
//    public void setListofSuplliers(List<Supplier> listofSuplliers) {
//        this.listofSuplliers = listofSuplliers;
//    }
//
//    //add supplier
//    public void addSupplier(Supplier supplier) {
//        this.listofSuplliers.add(supplier);
//    }
//
//    //remove supplier
//    public void removeSupplier(Supplier supplier) {
//        this.listofSuplliers.remove(supplier);
//    }



}

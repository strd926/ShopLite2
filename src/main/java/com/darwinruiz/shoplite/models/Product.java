package com.darwinruiz.shoplite.models;

import java.math.BigDecimal;

public class Product {
    private int id;
    private String name;
    private BigDecimal price;
    private int stock;

    public Product(int id, String name, BigDecimal price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public Product(String name, BigDecimal price, int stock) {
        this(0, name, price, stock);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
}
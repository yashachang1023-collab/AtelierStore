package com.atelier.atelierstore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "stationery")
public class Stationery extends BaseItem{
    public Stationery(String id, String name) {
        super(id, name);
    }

    private double price;
    private String category;
    private int stock;

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }



    public Stationery() {

    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Stationery(double price, String category, int stock) {
        this.price = price;
        this.category = category;
        this.stock = stock;
    }

    public Stationery(String id, String name, double price, String category) {
        super(id, name);
        this.price = price;
        this.category = category;
    }

    @Override
    public void displayInfo() {
        System.out.println("【文具周边】ID: " + getId() + " | 名称: " + getName() + " | 价格: " + getPrice() + "€ | 分类: " + getCategory());
    }
}

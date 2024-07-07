package org.example.model;

public class Equipment {
    private int id;
    private String name;
    private int quantity;

    public Equipment(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}

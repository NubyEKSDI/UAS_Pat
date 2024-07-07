package org.example.model;

public class BorrowRecord {
    private int id;
    private String user_id;
    private String equipment_id;
    private int quantity;
    private String borrowed_at;
    private String returned_at;

    // Getters and setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEquipment_id() {
        return equipment_id;
    }

    public void setEquipment_id(String equipmen_id) {
        this.equipment_id = equipmen_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBorrowed_at() {
        return borrowed_at;
    }

    public void setBorrowed_at(String borrowed_at) {
        this.borrowed_at = borrowed_at;
    }

    public String getReturned_at() {
        return returned_at;
    }

    public void setReturned_at(String returned_at) {
        this.returned_at = returned_at;
    }
}

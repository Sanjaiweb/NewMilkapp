package com.example.milkyway;

public class Customer {
    private String id; // Unique ID of the customer
    private String name; // Customer name
    private String address; // Customer address
    private String phone; // Customer phone number
    private String subscription; // Customer subscription details
    private String area; // Customer area
    private String shopId; // Shop ID to which the customer belongs
    private boolean delivered; // Flag to track if delivery has occurred

    // Default constructor required for Firestore
    public Customer() {
    }

    // Constructor with all parameters
    public Customer(String id, String name, String address, String phone, String subscription, String area, String shopId) {
        this.id = id; // Initialize the ID
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.subscription = subscription;
        this.area = area;
        this.shopId = shopId; // Initialize the shopId
        this.delivered = false; // Default delivered status
    }

    // Constructor for name and address only
    public Customer(String name, String address) {
        this.name = name;
        this.address = address;
        this.delivered = false; // Default delivered status
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getSubscription() {
        return subscription;
    }

    public String getArea() {
        return area;
    }

    public String getShopId() {
        return shopId; // Getter for shopId
    }

    public boolean isDelivered() {
        return delivered; // Getter for delivered
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered; // Setter for delivered
    }

    // Optional: Override toString for easier debugging/logging
    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", subscription='" + subscription + '\'' +
                ", area='" + area + '\'' +
                ", shopId='" + shopId + '\'' +
                ", delivered=" + delivered +
                '}';
    }
}

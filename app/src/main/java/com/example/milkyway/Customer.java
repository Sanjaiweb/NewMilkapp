package com.example.milkyway;

public class Customer {
    private String id; // Field to store the unique ID of the customer
    private String name;
    private String address;
    private String phone;
    private String subscription;
    private String area;
    private String shopId; // Add shopId field
    private boolean delivered; // Field to track if delivery has occurred


    // Constructor
    public Customer(String id, String name, String address, String phone, String subscription, String area, String shopId) {
        this.id = id; // Initialize the ID
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.subscription = subscription;
        this.area = area;
        this.shopId = shopId; // Initialize the shopId
        this.delivered = delivered;
    }

    // Getters
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

}

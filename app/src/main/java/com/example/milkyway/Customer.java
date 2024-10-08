package com.example.milkyway;
public class Customer {
    private String id;
    private String name;
    private String address;
    private String phone;
    private String subscription;
    private String area;
    private String shopId;
    private boolean deliveryStatus; // true if delivered, false otherwise

    // Empty constructor required for Firestore
    public Customer() {}

    public Customer(String name, String address, String phone, String subscription, String area, String shopId) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.subscription = subscription;
        this.area = area;
        this.shopId = shopId;
        this.deliveryStatus = false; // Default value for new customers
    }

    // Getters and Setters
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
        return shopId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Delivery status methods
    public boolean isDelivered() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(boolean deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}

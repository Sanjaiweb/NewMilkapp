package com.example.milkyway;

public class DeliveryPerson {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String area;

    // Required no-argument constructor for Firestore
    public DeliveryPerson() {}

    public DeliveryPerson(String name, String phone, String email, String area) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.area = area;
    }

    // Getter and Setter for id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getters only (remove setters if not needed)
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getArea() {
        return area;
    }
}

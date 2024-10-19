package com.example.milkyway;
public class DeliveryPerson {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String area;
    private String status;

    // Default constructor required for calls to DataSnapshot.getValue(DeliveryPerson.class)
    public DeliveryPerson() {}

    public DeliveryPerson(String id, String name, String email, String phone, String area, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.area = area;
        this.status = status;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

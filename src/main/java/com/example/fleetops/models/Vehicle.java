package com.example.fleetops.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;
    private String licensePlate;
    private boolean available = true;

    // Relația inversă: un vehicul are mai multe comenzi
    // mappedBy = "vehicle" trebuie să coincidă cu numele câmpului din clasa Order
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    @JsonIgnore // Evită buclele infinite când convertești în JSON (nu trimite și lista de comenzi înapoi)
    private List<Order> orders = new ArrayList<>();

    // Getters
    public Long getId() { return id; }
    public String getModel() { return model; }
    public String getLicensePlate() { return licensePlate; }
    public boolean isAvailable() { return available; }
    public List<Order> getOrders() { return orders; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setModel(String model) { this.model = model; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
}
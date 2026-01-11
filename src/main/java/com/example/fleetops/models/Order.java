package com.example.fleetops.models;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pickup;
    private String destination;

    @Column(columnDefinition = "TEXT")
    private String routeData;

    // Adăugăm relația cu vehiculul
    @ManyToOne
    @JoinColumn(name = "vehicle_id") // Aceasta este coloana Foreign Key în Postgres
    private Vehicle vehicle;

    // Getters
    public Long getId() { return id; }
    public String getPickup() { return pickup; }
    public String getDestination() { return destination; }
    public String getRouteData() { return routeData; }
    public Vehicle getVehicle() { return vehicle; } // Getter nou

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setPickup(String pickup) { this.pickup = pickup; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setRouteData(String routeData) { this.routeData = routeData; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; } // Setter nou
}
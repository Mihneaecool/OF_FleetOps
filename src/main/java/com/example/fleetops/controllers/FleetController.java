package com.example.fleetops.controllers;

import com.example.fleetops.models.Order;
import com.example.fleetops.models.Vehicle;
import com.example.fleetops.services.FleetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Gestionare Flotă", description = "Operațiuni vehicule și comenzi")
public class FleetController {

    @Autowired private FleetService fleetService;

    // --- VEHICULE ---

    @GetMapping("/vehicles")
    @Operation(summary = "Listare toate vehiculele")
    public List<Vehicle> getVehicles() {
        return fleetService.getAllVehicles();
    }

    @PostMapping("/vehicles")
    @Operation(summary = "Adaugă un vehicul nou în flotă")
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(fleetService.saveVehicle(vehicle));
    }

    // --- COMENZI ---

    @GetMapping("/orders")
    @Operation(summary = "Listare toate comenzile salvate")
    public List<Order> getOrders() {
        return fleetService.getAllOrders();
    }

    @PostMapping("/orders") // AM ADĂUGAT /orders AICI
    @Operation(summary = "Creare comandă cu coordonate dinamice")
    public Order createOrder(@RequestParam String pickup,
                             @RequestParam String destination,
                             @RequestParam Long vehicleId,
                             @RequestParam double sLat,
                             @RequestParam double sLon,
                             @RequestParam double eLat,
                             @RequestParam double eLon) {
        // Trimite parametrii către serviciul care acum este configurat să îi primească
        return fleetService.createOrder(pickup, destination, vehicleId, sLat, sLon, eLat, eLon);
    }
}
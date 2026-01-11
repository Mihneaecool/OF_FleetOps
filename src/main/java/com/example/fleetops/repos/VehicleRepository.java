package com.example.fleetops.repos;

import com.example.fleetops.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // Putem adăuga o metodă personalizată pentru a vedea doar mașinile libere
    List<Vehicle> findByAvailableTrue();
}
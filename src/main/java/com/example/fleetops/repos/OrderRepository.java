package com.example.fleetops.repos;

import com.example.fleetops.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Putem găsi toate comenzile unui anumit utilizator (dacă am adăugat relația în entitate)
    // List<Order> findByUserId(Long userId);
}
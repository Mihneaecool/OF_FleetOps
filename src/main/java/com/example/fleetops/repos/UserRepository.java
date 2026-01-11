package com.example.fleetops.repos;

import com.example.fleetops.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring va genera automat: SELECT * FROM users WHERE username = ?
    Optional<User> findByUsername(String username);

    // Verifică dacă un utilizator există deja la înregistrare
    Boolean existsByUsername(String username);
}
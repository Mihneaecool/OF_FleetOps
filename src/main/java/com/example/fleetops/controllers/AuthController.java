package com.example.fleetops.controllers;

import com.example.fleetops.models.User;
import com.example.fleetops.services.FleetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autentificare", description = "Endpoint-uri pentru înregistrare")
public class AuthController {

    @Autowired private FleetService fleetService;

    @PostMapping("/register")
    @Operation(summary = "Înregistrează un utilizator nou")
    public String register(@RequestBody User user) {
        fleetService.registerUser(user);
        return "Utilizator creat cu succes!";
    }
}
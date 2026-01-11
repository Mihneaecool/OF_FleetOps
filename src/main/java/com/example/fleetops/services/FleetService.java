package com.example.fleetops.services;

import com.example.fleetops.models.Order;
import com.example.fleetops.models.User;
import com.example.fleetops.models.Vehicle;
import com.example.fleetops.repos.OrderRepository;
import com.example.fleetops.repos.UserRepository;
import com.example.fleetops.repos.VehicleRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.concurrent.ScheduledFuture;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
public class FleetService {

    @Autowired private UserRepository userRepository;
    @Autowired private VehicleRepository vehicleRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private RestTemplate restTemplate;
    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private ObjectMapper objectMapper;

    // Folosim un pool de thread-uri pentru simulări paralele
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        return userRepository.save(user);
    }

    public List<Vehicle> getAllVehicles() { return vehicleRepository.findAll(); }
    public Vehicle saveVehicle(Vehicle vehicle) { return vehicleRepository.save(vehicle); }
    public List<Order> getAllOrders() { return orderRepository.findAll(); }

    public Order createOrder(String pickup, String destination, Long vehicleId,
                             double sLat, double sLon, double eLat, double eLon) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehiculul nu există!"));

        if (!vehicle.isAvailable()) {
            throw new RuntimeException("Vehiculul este ocupat!");
        }

        // 1. Apel către C++ cu coordonatele dinamice
        String cppUrl = "http://cpp-service:18080/calculate-route";
        Map<String, Double> requestData = Map.of(
                "sLat", sLat, "sLon", sLon,
                "eLat", eLat, "eLon", eLon
        );

        String routeJson;
        try {
            routeJson = restTemplate.postForObject(cppUrl, requestData, String.class);
        } catch (Exception e) {
            // Fallback în caz că serviciul C++ este offline
            routeJson = String.format("[{\"lat\":%f,\"lon\":%f},{\"lat\":%f,\"lon\":%f}]", sLat, sLon, eLat, eLon);
        }

        // 2. Actualizare stare vehicul
        vehicle.setAvailable(false);
        vehicleRepository.save(vehicle);

        Order order = new Order();
        order.setPickup(pickup);
        order.setDestination(destination);
        order.setRouteData(routeJson);
        order.setVehicle(vehicle);

        Order savedOrder = orderRepository.save(order);

        // Pornim simularea
        startVehicleSimulation(savedOrder);

        return savedOrder;
    }

    public void startVehicleSimulation(Order order) {
        // Folosim un array de un singur element pentru a păstra referința la task-ul programat
        final ScheduledFuture<?>[] futureHandle = new ScheduledFuture<?>[1];

        futureHandle[0] = scheduler.scheduleAtFixedRate(new Runnable() {
            private int step = 0;

            @Override
            public void run() {
                try {
                    // Citim coordonatele din String-ul JSON salvat în DB
                    List<Map<String, Object>> coordinates = objectMapper.readValue(
                            order.getRouteData(),
                            new TypeReference<List<Map<String, Object>>>() {}
                    );

                    if (step < coordinates.size()) {
                        Map<String, Object> point = coordinates.get(step);

                        // Trimitem și pickup/destination pentru ca harta să știe ce titlu să pună
                        Map<String, Object> positionUpdate = Map.of(
                                "orderId", order.getId(),
                                "lat", point.get("lat"),
                                "lon", point.get("lon"),
                                "pickup", order.getPickup(),
                                "destination", order.getDestination(),
                                "status", "IN_TRANSIT"
                        );

                        messagingTemplate.convertAndSend("/topic/positions", positionUpdate);
                        step++;
                    } else {
                        // Cursa a ajuns la final
                        messagingTemplate.convertAndSend("/topic/positions", Map.of(
                                "orderId", order.getId(),
                                "status", "DELIVERED"
                        ));

                        // Eliberăm vehiculul
                        Vehicle v = vehicleRepository.findById(order.getVehicle().getId()).orElse(null);
                        if (v != null) {
                            v.setAvailable(true);
                            vehicleRepository.save(v);
                        }

                        futureHandle[0].cancel(false);
                    }
                } catch (Exception e) {
                    futureHandle[0].cancel(false);
                }
            }
        }, 0, 5, TimeUnit.SECONDS); // 5 secunde este un echilibru bun pentru demo
    }
}
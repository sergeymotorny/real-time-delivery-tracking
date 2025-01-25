package com.motorny.controller;

import com.motorny.service.DeliveryLocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/location")
@CrossOrigin(origins = "http://localhost:3000")
public class DeliveryLocationController {

    private final DeliveryLocationService deliveryLocationService;

    public DeliveryLocationController(DeliveryLocationService deliveryLocationService) {
        this.deliveryLocationService = deliveryLocationService;
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> getCurrentLocation() {
        // Simulating a location between New York and New Jersey
        double nycLat = 40.7128;  // Широта (latitude) Нью-Йорка
        double nycLon = -74.0060; // Долгота (longitude) Нью-Йорка
        double njLat = 40.7357;   // Широта (latitude) Нью-Джерси
        double njLon = -74.1724;  // Долгота (longitude) Нью-Джерси

        double randomLat = nycLat + Math.random() * (njLat - nycLat);
        double randomLon = nycLon + Math.random() * (njLon - nycLon);

        String location = randomLat + " , " + randomLon;

        return new ResponseEntity<>(Map.of("location", location), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Map<String, String>> updateLocation() throws InterruptedException {
        double nycLat = 40.7128;
        double nycLon = -74.0060;
        double njLat = 40.7357;
        double njLon = -74.1724;

        int range = 100;
        while (range > 0) {
            // Generate random location between New York City and New Jersey
            double randomLat = nycLat + Math.random() * (njLat - nycLat);
            double randomLon = nycLon + Math.random() * (njLon - nycLon);

            String location = randomLat + " , " + randomLon;
            deliveryLocationService.updateLocation(location);

            Thread.sleep(1000);
            range--;
        }

        return new ResponseEntity<>(Map.of("message", "Location Updated"), HttpStatus.OK);
    }
}

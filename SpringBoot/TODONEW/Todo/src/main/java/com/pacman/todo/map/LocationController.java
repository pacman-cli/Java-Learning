package com.pacman.todo.map;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//GET /api/locations → fetch all markers
//
//POST /api/locations → add a new marker
@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "http://localhost:3000")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public List<Location> getUserLocations(HttpServletRequest request) {
        String username = request.getAttribute("username").toString();// Comes from jwt filter
        return locationService.getLocations(username);
    }

    @PostMapping
    public Location createLocation(@RequestBody Location location, HttpServletRequest request) {
        String username = request.getAttribute("username").toString();
        return locationService.saveLocation(username, location);
    }
}

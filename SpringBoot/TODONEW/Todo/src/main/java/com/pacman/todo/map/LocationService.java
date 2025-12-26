package com.pacman.todo.map;

import com.pacman.todo.user.User;
import com.pacman.todo.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public LocationService(LocationRepository locationRepository, UserRepository userRepository) {
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    public List<Location> getLocations(String username){
        User user=userRepository.findByUsername(username).orElse(null);
        return locationRepository.findByUser(user);
    }

    public Location saveLocation(String username,Location location){
        User user = userRepository.findByUsername(username).orElseThrow();
        location.setUser(user);
        return  locationRepository.save(location);
    }
}

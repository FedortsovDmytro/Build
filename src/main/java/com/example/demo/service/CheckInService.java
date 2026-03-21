package com.example.demo.service;

import com.example.demo.entity.Day;
import com.example.demo.entity.Location;
import com.example.demo.entity.User;
import com.example.demo.repository.DayRepository;
import com.example.demo.repository.LocationRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class CheckInService {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final DayRepository dayRepository;

    public CheckInService(UserRepository userRepository,
                          LocationRepository locationRepository,
                          DayRepository dayRepository) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.dayRepository = dayRepository;
    }

    public Day checkIn(Long userId, Long locationId, double latitude, double longitude) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        Day day = new Day.Builder()
                .checkInTime(LocalDateTime.now())
                .latitude(latitude)
                .longitude(longitude)
                .user(user)
                .location(location)
                .build();

        return dayRepository.save(day);
    }
}
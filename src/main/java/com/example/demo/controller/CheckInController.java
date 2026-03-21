package com.example.demo.controller;

import com.example.demo.entity.Day;
import com.example.demo.entity.Location;
import com.example.demo.entity.User;
import com.example.demo.repository.DayRepository;
import com.example.demo.repository.LocationRepository;
import com.example.demo.repository.UserRepository;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
public class CheckInController {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private DayRepository dayRepository;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/checkin")
    public String checkInPage() {
        return "checkin";
    }

    @PostMapping("/checkin/confirm")
    @ResponseBody
    public String confirmLocation(@RequestBody Map<String, Double> coords, Authentication auth) {

        double lat = coords.get("latitude");
        double lon = coords.get("longitude");

        Location location = locationRepository.findTopByOrderByIdDesc();
        if (location == null) {
            return "Локація не налаштована адміністратором";
        }

        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        LocalDate today = LocalDate.now();

        boolean alreadyCheckedIn = dayRepository.existsByUserAndLocalDate(user, today);

        if (alreadyCheckedIn) {
            return "Ви вже відмічені сьогодні ✅";
        }

        double distance = distance(lat, lon, location.getLatitude(), location.getLongitude());

        if (distance * 1000 <= location.getRadiusInMeters()) {
            Day day = new Day();
            day.setUser(user);
            day.setDate(today);
            day.setCheckInTime(LocalDateTime.now());
            day.setConfirmed(true);
            dayRepository.save(day);

            return "Ви в межах локації! ✔️";
        } else {
            return "Ви поза зоною! ❌";
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
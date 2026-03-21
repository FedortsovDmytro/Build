package com.example.demo.controller;

import com.example.demo.entity.Day;
import com.example.demo.entity.EmployeeWorkSummary;
import com.example.demo.entity.User;
import com.example.demo.repository.DayRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    private final UserRepository userRepository;
    private final DayRepository dayRepository;

    public ManagerController(UserRepository userRepository,
                             DayRepository dayRepository) {
        this.userRepository = userRepository;
        this.dayRepository = dayRepository;
    }

    @GetMapping
    public String managerPage(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "manager";
    }
    @PostMapping("/day/toggle")
    @ResponseBody
    @Transactional
    public void toggleDay(
            @RequestParam Long userId,
            @RequestParam String date
    ) {
        User user = userRepository.findById(userId).orElseThrow();
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime localDateTime = LocalDateTime.now();

        Day day = dayRepository.findByUserAndLocalDate(user, localDate)
                .orElse(null);

        if (day == null) {
            day = new Day();
            day.setUser(user);
            day.setLocalDate(localDate);
            day.setConfirmed(true);
            day.setCheckInTime(localDateTime);
        } else {
            day.setConfirmed(!day.isConfirmed());
            day.setCheckInTime(localDateTime);
        }

        dayRepository.save(day);
    }
    @GetMapping("/user/{id}")
    public String userDetails(@PathVariable Long id, Model model) {

        User user = userRepository.findById(id).orElseThrow();

        LocalDate end = LocalDate.now();
        LocalDate start = end.minusMonths(2);

        List<Day> userDays = dayRepository.findByUser(user);

        Map<LocalDate, Day> dayMap = userDays.stream()
                .collect(Collectors.toMap(Day::getLocalDate, d -> d));

        List<Day> fullDays = new ArrayList<>();

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {

            Day day = dayMap.getOrDefault(date, null);

            if (day != null) {
                fullDays.add(day);
            } else {
                Day empty = new Day();
                empty.setLocalDate(date);
                empty.setConfirmed(false);
                empty.setId(null);
                fullDays.add(empty);
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("days", fullDays);

        return "user-details";
    }
}
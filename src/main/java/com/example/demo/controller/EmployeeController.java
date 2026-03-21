package com.example.demo.controller;

import com.example.demo.entity.Day;
import com.example.demo.entity.User;
import com.example.demo.repository.DayRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private final DayRepository dayRepository;

    private final UserRepository userRepository;
    public EmployeeController(DayRepository dayRepository,UserRepository userRepository) {
        this.dayRepository = dayRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/list/{userId}")
    public List<Day> getEmployeeDays(@PathVariable Long userId) {

        return dayRepository.findByUserId(userId);
    }
    @GetMapping("/home")
    public String home(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            Model model,
            Authentication auth
    ) {
        LocalDate now = LocalDate.now();
        int currentYear = (year != null) ? year : now.getYear();
        int currentMonth = (month != null) ? month : now.getMonthValue();

        User user = userRepository.findByEmail(auth.getName()).orElseThrow();

        LocalDate start = YearMonth.of(currentYear, currentMonth).atDay(1);
        LocalDate end = YearMonth.of(currentYear, currentMonth).atEndOfMonth();
        List<Day> days = dayRepository.findByUserAndLocalDateBetween(
                user,
                start,
                end
        );

        int prevMonth = (currentMonth == 1) ? 12 : currentMonth - 1;
        int prevYear = (currentMonth == 1) ? currentYear - 1 : currentYear;

        int nextMonth = (currentMonth == 12) ? 1 : currentMonth + 1;
        int nextYear = (currentMonth == 12) ? currentYear + 1 : currentYear;

        model.addAttribute("days", days);
        model.addAttribute("email", user.getEmail());
        model.addAttribute("year", currentYear);
        model.addAttribute("month", currentMonth);
        model.addAttribute("prevMonth", prevMonth);
        model.addAttribute("prevYear", prevYear);
        model.addAttribute("nextMonth", nextMonth);
        model.addAttribute("nextYear", nextYear);

        return "home";
    }
}
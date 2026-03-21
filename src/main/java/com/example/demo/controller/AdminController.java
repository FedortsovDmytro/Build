package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.DayRepository;
import com.example.demo.repository.LocationRepository;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final DayRepository dayRepository;
    private final LocationRepository locationRepository;
    public AdminController(UserRepository userRepository,
                           DayRepository dayRepository,LocationRepository locationRepository) {
        this.userRepository = userRepository;
        this.dayRepository = dayRepository;
        this.locationRepository = locationRepository;
    }

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin";
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
    @GetMapping("/location")
    public String locationPage(Model model) {
        model.addAttribute("locations", locationRepository.findAll());
        return "location";
    }
    @PostMapping("/location")
    public String addLocation(
            @RequestParam String name,
            @RequestParam String address,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radiusInMeters
    ) {
        Location location = new Location();

        location.setName(name);
        location.setAddress(address);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setRadiusInMeters(radiusInMeters);
        LocalDateTime now = LocalDateTime.now();
        locationRepository.save(location);

        return "redirect:/admin/location?success";
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
    @PostMapping("/user/toggle-role/{id}")
    @ResponseBody
    public void toggleRole(@PathVariable Long id) {

        User user = userRepository.findById(id).orElseThrow();

        if (user.getRole() == Role.MANAGER) {
            user.setRole(Role.EMPLOYEE);
        } else {
            user.setRole(Role.MANAGER);
        }

        userRepository.save(user);
    }
    @GetMapping("/export-excel")
    public void exportExcel(HttpServletResponse response) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Attendance Summary");

        // Make columns wider
        for (int i = 0; i < 50; i++) {  // adjust 50 to your max number of dates
            sheet.setColumnWidth(i, 5000); // ~5000 units = ~35-40 pixels
        }

        // Get users and dates
        List<User> users = userRepository.findAll();
        List<Day> days = dayRepository.findAll();
        List<LocalDate> dates = days.stream()
                .map(Day::getLocalDate)
                .distinct()
                .sorted()
                .toList();

        // Header row
        Row headerRow = sheet.createRow(0);
        headerRow.setHeightInPoints(25); // make header taller
        headerRow.createCell(0).setCellValue("Person");
        for (int i = 0; i < dates.size(); i++) {
            headerRow.createCell(i + 1).setCellValue(dates.get(i).toString());
        }

        // Data rows
        int rowIndex = 1;
        for (User user : users) {
            Row row = sheet.createRow(rowIndex++);
            row.setHeightInPoints(20); // make row taller
            row.createCell(0).setCellValue(user.getFirstName() + " " + user.getLastName());

            for (int i = 0; i < dates.size(); i++) {
                LocalDate date = dates.get(i);
                Day day = dayRepository.findByUserAndLocalDate(user, date).orElse(null);
                String value = (day != null && day.isConfirmed()) ? "Present" : "";
                row.createCell(i + 1).setCellValue(value);
            }
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=attendance_summary.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}

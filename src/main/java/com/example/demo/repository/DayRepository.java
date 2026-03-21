package com.example.demo.repository;

import com.example.demo.entity.Day;
import com.example.demo.entity.EmployeeWorkSummary;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DayRepository extends JpaRepository<Day,Long> {
    List<Day> findByUserId(Long userId);
    List<Day> findByUserAndCheckInTimeBetween(User user, LocalDateTime start, LocalDateTime end);
    List<Day> findByUser(User user);
    @Transactional(readOnly = true)
    @Query("""
       SELECT new com.example.demo.entity.EmployeeWorkSummary(u.firstName, u.lastName, COUNT(d))
       FROM Day d
       JOIN d.user u
       WHERE MONTH(d.checkInTime) = MONTH(CURRENT_DATE())
       GROUP BY u.id, u.firstName, u.lastName
       """)
    List<EmployeeWorkSummary> getMonthlySummary();

    boolean existsByUserAndLocalDate(User user, LocalDate localDate);

    Optional<Day> findByUserAndLocalDate(User user, LocalDate localDate);

    List<Day> findByUserAndLocalDateBetween(User user, LocalDate start, LocalDate end);
}

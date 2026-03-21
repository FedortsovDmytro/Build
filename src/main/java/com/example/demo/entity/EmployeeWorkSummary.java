package com.example.demo.entity;

public class EmployeeWorkSummary {
    private String firstName;
    private String lastName;
    private Long totalDays;

    public EmployeeWorkSummary(String firstName, String lastName, Long totalDays) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.totalDays = totalDays;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Long totalDays) {
        this.totalDays = totalDays;
    }
}
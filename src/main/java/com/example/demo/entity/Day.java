package com.example.demo.entity;

import com.example.demo.repository.UserRepository;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Day {

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate localDate;
    private LocalDateTime checkInTime;
    private Double latitude;
    private Double longitude;
    private boolean confirmed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    public Day() {}

    private Day(Builder builder) {
        this.checkInTime = builder.checkInTime;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.location = builder.location;
    }

    public void setConfirmed(boolean b) {
        this.confirmed = b;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDate(LocalDate today) {
       this.localDate = today;
    }

    public void setCheckInTime(LocalDateTime now) {
        this.checkInTime = now;
    }

    public static class Builder {

        private LocalDateTime checkInTime;
        private Double latitude;
        private Double longitude;
        private Location location;
        private User user;
        private Day day;

        public Builder checkInTime(LocalDateTime checkInTime) {
            this.checkInTime = checkInTime;
            return this;
        }

        public Builder latitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder location(Location location) {
            this.location = location;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }
        public Day build() {
            return new Day(this);
        }
    }
    public boolean isConfirmed() {
        return confirmed;
    }
    public User getUser() {return user;}
    public Long getId() { return id; }
    public LocalDateTime getCheckInTime() { return checkInTime; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public Location getLocation() { return location; }
    public LocalDate getLocalDate() { return localDate; }
    public void setLocalDate(LocalDate localDate) { this.localDate = localDate; }
}
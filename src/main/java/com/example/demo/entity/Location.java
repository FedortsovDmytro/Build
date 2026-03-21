package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    private double latitude;
    private double longitude;

    private double radiusInMeters;

    @OneToMany(mappedBy = "location")
    private List<Day> days;

    public Location() {}

    private Location(Builder builder) {
        this.name = builder.name;
        this.address = builder.address;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.radiusInMeters = builder.radiusInMeters;
        this.days = builder.days;
    }

    // ================= GETTERS =================

    public Long getId() { return id; }

    public String getName() { return name; }

    public String getAddress() { return address; }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

    public double getRadiusInMeters() { return radiusInMeters; }

    public List<Day> getDays() { return days; }


    public void setName(String name) { this.name = name; }

    public void setAddress(String address) { this.address = address; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

    public void setRadiusInMeters(double radiusInMeters) { this.radiusInMeters = radiusInMeters; }

    public void setDays(List<Day> days) { this.days = days; }


    // ================= BUILDER =================

    public static class Builder {

        private String name;
        private String address;
        private double latitude;
        private double longitude;
        private double radiusInMeters;
        private List<Day> days;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder latitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder radiusInMeters(double radiusInMeters) {
            this.radiusInMeters = radiusInMeters;
            return this;
        }

        public Builder days(List<Day> days) {
            this.days = days;
            return this;
        }

        public Location build() {
            return new Location(this);
        }
    }
}
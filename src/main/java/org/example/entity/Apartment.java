package org.example.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "apartment")
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;

    @Column(name = "apartment_number", nullable = false)
    private String apartmentNumber;

    @Column(name = "floor_number", nullable = false)
    private Integer floorNumber;

    @Column(nullable = false)
    private BigDecimal area;

    @ManyToMany(mappedBy = "ownedApartments", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Owner> owners = new HashSet<>();

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL)
    private Set<Resident> residents = new HashSet<>();

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL)
    private Set<Fee> fees = new HashSet<>();

    // Default constructor
    public Apartment() {
    }

    // Getters and Validating Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        validateApartmentNumber(apartmentNumber);
        this.apartmentNumber = apartmentNumber;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        validateFloorNumber(floorNumber);
        this.floorNumber = floorNumber;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        validateArea(area);
        this.area = area;
    }

    public Set<Owner> getOwners() {
        return owners;
    }

    public void setOwners(Set<Owner> owners) {
        this.owners = owners;
    }

    public Set<Resident> getResidents() {
        return residents;
    }

    public void setResidents(Set<Resident> residents) {
        this.residents = residents;
    }

    public Set<Fee> getFees() {
        return fees;
    }

    public void setFees(Set<Fee> fees) {
        this.fees = fees;
    }

    // Validation Methods
    private void validateApartmentNumber(String number) {
        if (number == null || number.trim().isEmpty()) {
            throw new IllegalArgumentException("Apartment number cannot be empty");
        }
        if (!number.matches("^[A-Za-z0-9-]{1,10}$")) {
            throw new IllegalArgumentException("Invalid apartment number format");
        }
    }

    private void validateFloorNumber(Integer floor) {
        if (floor == null || floor < 0) {
            throw new IllegalArgumentException("Floor number cannot be negative");
        }
        if (building != null && floor > building.getTotalFloors()) {
            throw new IllegalArgumentException("Floor number cannot exceed building's total floors");
        }
    }

    private void validateArea(BigDecimal area) {
        if (area == null || area.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Area must be greater than 0");
        }
        if (area.compareTo(new BigDecimal("500")) > 0) { // reasonable maximum
            throw new IllegalArgumentException("Area seems unreasonably large");
        }
    }
}

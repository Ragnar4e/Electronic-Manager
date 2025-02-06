package org.example.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "building")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Integer totalFloors;

    @Column(nullable = false)
    private Integer totalApartments;

    @Column(name = "built_up_area")
    private BigDecimal builtUpArea;

    @Column(name = "common_area")
    private BigDecimal commonArea;

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
    private Set<Apartment> apartments = new HashSet<>();

    // Default constructor
    public Building() {
    }

    // Getters and Validating Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        validateAddress(address);
        this.address = address;
    }

    public Integer getTotalFloors() {
        return totalFloors;
    }

    public void setTotalFloors(Integer totalFloors) {
        validateFloors(totalFloors);
        this.totalFloors = totalFloors;
    }

    public Integer getTotalApartments() {
        return totalApartments;
    }

    public void setTotalApartments(Integer totalApartments) {
        validateApartments(totalApartments);
        this.totalApartments = totalApartments;
    }

    public BigDecimal getBuiltUpArea() {
        return builtUpArea;
    }

    public void setBuiltUpArea(BigDecimal builtUpArea) {
        validateArea(builtUpArea, "Built-up area");
        this.builtUpArea = builtUpArea;
    }

    public BigDecimal getCommonArea() {
        return commonArea;
    }

    public void setCommonArea(BigDecimal commonArea) {
        validateArea(commonArea, "Common area");
        this.commonArea = commonArea;
    }

    public Set<Apartment> getApartments() {
        return apartments;
    }

    public void setApartments(Set<Apartment> apartments) {
        this.apartments = apartments;
    }

    // Validation Methods
    private void validateAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }
        if (address.length() < 5 || address.length() > 200) {
            throw new IllegalArgumentException("Address must be between 5 and 200 characters");
        }
    }

    private void validateFloors(Integer floors) {
        if (floors == null || floors < 1) {
            throw new IllegalArgumentException("Building must have at least 1 floor");
        }
        if (floors > 50) {  // reasonable maximum
            throw new IllegalArgumentException("Invalid number of floors");
        }
    }

    private void validateApartments(Integer apartments) {
        if (apartments == null || apartments < 1) {
            throw new IllegalArgumentException("Building must have at least 1 apartment");
        }
    }

    private void validateArea(BigDecimal area, String fieldName) {
        if (area == null || area.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than 0");
        }
        if (area.compareTo(new BigDecimal("10000")) > 0) { // arbitrary upper limit
            throw new IllegalArgumentException(fieldName + " is unreasonably large");
        }
    }
}

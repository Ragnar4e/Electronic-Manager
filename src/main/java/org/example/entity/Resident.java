package org.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "resident")
public class Resident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    private Integer age;

    @Column(name = "has_pet")
    private Boolean hasPet = false;

    @Column(name = "uses_elevator")
    private Boolean usesElevator = true;

    // Default constructor
    public Resident() {
    }

    // Getters and Validating Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        validateName(firstName, "First name");
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        validateName(lastName, "Last name");
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        validateAge(age);
        this.age = age;
    }

    public Boolean getHasPet() {
        return hasPet;
    }

    public void setHasPet(Boolean hasPet) {
        this.hasPet = hasPet;
    }

    public Boolean getUsesElevator() {
        return usesElevator;
    }

    public void setUsesElevator(Boolean usesElevator) {
        this.usesElevator = usesElevator;
    }

    // Validation Methods
    private void validateName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        if (!name.matches("^[A-Za-z\\s-]{2,50}$")) {
            throw new IllegalArgumentException(fieldName + " can only contain letters, spaces, and hyphens");
        }
    }

    private void validateAge(Integer age) {
        if (age == null || age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
        if (age > 120) { // reasonable maximum
            throw new IllegalArgumentException("Age seems unreasonably high");
        }
    }
}

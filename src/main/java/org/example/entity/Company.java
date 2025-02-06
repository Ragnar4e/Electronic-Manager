package org.example.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String contactNumber;

    private String email;

    private String address;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Set<Employee> employees = new HashSet<>();

    // Default constructor
    public Company() {
    }

    // Getters and Validating Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        validatePhoneNumber(contactNumber);
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        validateEmail(email);
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    // Validation Methods
    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be empty");
        }
        if (name.length() < 2 || name.length() > 100) {
            throw new IllegalArgumentException("Company name must be between 2 and 100 characters");
        }
    }

    private void validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (email != null && !email.isEmpty() && !email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    private void validatePhoneNumber(String phone) {
        String phoneRegex = "^[0-9]{10}$";
        if (phone != null && !phone.isEmpty() && !phone.matches(phoneRegex)) {
            throw new IllegalArgumentException("Phone number must be 10 digits");
        }
    }
}

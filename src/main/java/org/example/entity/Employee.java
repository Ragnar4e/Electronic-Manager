package org.example.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    private String email;

    private String phone;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private Set<Building> managedBuildings = new HashSet<>();

    // Default constructor
    public Employee() {
    }

    // Getters and Validating Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        validateEmail(email);
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        validatePhone(phone);
        this.phone = phone;
    }

    public Set<Building> getManagedBuildings() {
        return managedBuildings;
    }

    public void setManagedBuildings(Set<Building> managedBuildings) {
        this.managedBuildings = managedBuildings;
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

    private void validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (email != null && !email.isEmpty() && !email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    private void validatePhone(String phone) {
        String phoneRegex = "^[0-9]{10}$";
        if (phone != null && !phone.isEmpty() && !phone.matches(phoneRegex)) {
            throw new IllegalArgumentException("Phone number must be 10 digits");
        }
    }
}

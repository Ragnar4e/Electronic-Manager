package org.example.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "owner")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    private String email;

    private String phone;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "apartment_owner",
            joinColumns = @JoinColumn(name = "owner_id"),
            inverseJoinColumns = @JoinColumn(name = "apartment_id")
    )
    private Set<Apartment> ownedApartments = new HashSet<>();

    // Default constructor
    public Owner() {
    }

    // Getters and Validating Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<Apartment> getOwnedApartments() {
        return ownedApartments;
    }

    public void setOwnedApartments(Set<Apartment> ownedApartments) {
        this.ownedApartments = ownedApartments;
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
        if (email != null && !email.isEmpty()) {
            String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
            if (!email.matches(emailRegex)) {
                throw new IllegalArgumentException("Invalid email format");
            }
        }
    }

    private void validatePhone(String phone) {
        if (phone != null && !phone.isEmpty()) {
            String phoneRegex = "^[0-9]{10}$";
            if (!phone.matches(phoneRegex)) {
                throw new IllegalArgumentException("Phone number must be 10 digits");
            }
        }
    }
}

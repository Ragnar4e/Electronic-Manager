package org.example.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fee")
public class Fee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FeeStatus status = FeeStatus.PENDING;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    // Enum for fee status
    public enum FeeStatus {
        PENDING,
        PAID
    }

    // Default constructor
    public Fee() {
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        validateAmount(amount);
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        validateDueDate(dueDate);
        this.dueDate = dueDate;
    }

    public FeeStatus getStatus() {
        return status;
    }

    public void setStatus(FeeStatus status) {
        this.status = status;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        validatePaymentDate(paymentDate);
        this.paymentDate = paymentDate;
    }

    // Validation Methods
    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        if (amount.compareTo(new BigDecimal("100000")) > 0) { // reasonable maximum
            throw new IllegalArgumentException("Amount seems unreasonably high");
        }
    }

    private void validateDueDate(LocalDate dueDate) {
        if (dueDate == null) {
            throw new IllegalArgumentException("Due date cannot be null");
        }
        if (dueDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date cannot be in the past");
        }
    }

    private void validatePaymentDate(LocalDate paymentDate) {
        if (paymentDate != null) {
            if (paymentDate.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Payment date cannot be in the future");
            }
            if (this.dueDate != null && paymentDate.isBefore(this.dueDate.minusMonths(1))) {
                throw new IllegalArgumentException("Payment date seems unreasonably early");
            }
        }
    }
}

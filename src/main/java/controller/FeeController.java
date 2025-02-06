package controller;

import org.example.entity.Fee;
import service.FeeService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FeeController {
    private final FeeService feeService;

    public FeeController(FeeService feeService) {
        this.feeService = feeService;
    }

    // Create operations
    public Fee createFee(Long apartmentId, BigDecimal amount, LocalDate dueDate) {
        Fee fee = new Fee();
        fee.setAmount(amount);
        fee.setDueDate(dueDate);
        fee.setStatus(Fee.FeeStatus.PENDING);
        return feeService.save(fee);
    }

    // Read operations
    public Fee getFeeById(Long id) {
        return feeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fee not found with ID: " + id));
    }

    public List<Fee> getAllFees() {
        return feeService.findAll();
    }

    public List<Fee> getFeesByApartment(Long apartmentId) {
        return feeService.findByApartmentId(apartmentId);
    }

    public List<Fee> findByStatus(Fee.FeeStatus status) {
        return feeService.findByStatus(status);
    }

    public List<Fee> getFeesByStatus(Fee.FeeStatus status) {
        return feeService.findByStatus(status);
    }

    public List<Fee> getOverdueFees() {
        return feeService.findOverdueFees();
    }

    public List<Fee> findByBuildingId(Long buildingId) {
        return feeService.findByBuildingId(buildingId);
    }

    // Update operations
    public Fee updateFee(Long id, BigDecimal amount, LocalDate dueDate, Fee.FeeStatus status) {
        Fee fee = getFeeById(id);
        fee.setAmount(amount);
        fee.setDueDate(dueDate);
        fee.setStatus(status);
        return feeService.save(fee);
    }

    // Delete operations
    public void deleteFee(Long id) {
        feeService.deleteById(id);
    }

    // Business operations
    public void markFeeAsPaid(Long feeId) {
        feeService.markAsPaid(feeId, LocalDate.now());
    }

    public void generateMonthlyFees(Long buildingId) {
        feeService.generateMonthlyFees(buildingId);
    }

    public BigDecimal calculateTotalUnpaidFees(Long apartmentId) {
        return feeService.calculateTotalUnpaidFees(apartmentId);
    }

    public void applyLateFee(Long feeId, BigDecimal lateFeeAmount) {
        feeService.applyLateFee(feeId, lateFeeAmount);
    }

    public BigDecimal getTotalFeesByBuilding(Long buildingId) {
        return feeService.getTotalFeesByBuildingId(buildingId);
    }
}

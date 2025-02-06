package service;

import org.example.entity.Fee;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FeeService extends GenericService<Fee, Long> {
    List<Fee> findByApartmentId(Long apartmentId);
    List<Fee> findByStatus(Fee.FeeStatus status);
    List<Fee> findByDueDateBefore(LocalDate date);
    List<Fee> findByBuildingId(Long buildingId);
    BigDecimal getTotalFeesByBuildingId(Long buildingId);
    List<Fee> findOverdueFees();

    // Business logic methods
    void markAsPaid(Long feeId, LocalDate paymentDate);
    void generateMonthlyFees(Long buildingId);
    BigDecimal calculateTotalUnpaidFees(Long apartmentId);
    void applyLateFee(Long feeId, BigDecimal lateFeeAmount);
}
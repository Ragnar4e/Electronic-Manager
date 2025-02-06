package repository;

import org.example.entity.Fee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FeeRepository extends GenericRepository<Fee, Long> {
    List<Fee> findByApartmentId(Long apartmentId);
    List<Fee> findByStatus(Fee.FeeStatus status);
    List<Fee> findByDueDateBefore(LocalDate date);
    // Get fees for a specific building
    List<Fee> findByBuildingId(Long buildingId);
    // Get total fees collected for a building
    BigDecimal getTotalFeesByBuildingId(Long buildingId);
    // Get unpaid fees past due date
    List<Fee> findOverdueFees();
}
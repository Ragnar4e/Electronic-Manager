package service;

import jakarta.persistence.EntityManager;
import org.example.entity.Fee;
import org.example.entity.Apartment;
import org.example.entity.Building;
import repository.FeeRepository;
import repository.ApartmentRepository;
import repository.BuildingRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class FeeServiceImpl implements FeeService {
    private final EntityManager entityManager;
    private final FeeRepository feeRepository;
    private final ApartmentRepository apartmentRepository;
    private final BuildingRepository buildingRepository;

    public FeeServiceImpl(EntityManager entityManager,
                          FeeRepository feeRepository,
                          ApartmentRepository apartmentRepository,
                          BuildingRepository buildingRepository) {
        this.entityManager = entityManager;
        this.feeRepository = feeRepository;
        this.apartmentRepository = apartmentRepository;
        this.buildingRepository = buildingRepository;
    }

    @Override
    public Fee save(Fee fee) {
        return feeRepository.save(fee);
    }

    @Override
    public Optional<Fee> findById(Long id) {
        return feeRepository.findById(id);
    }

    @Override
    public List<Fee> findAll() {
        return feeRepository.findAll();
    }

    @Override
    public void delete(Fee fee) {
        feeRepository.delete(fee);
    }

    @Override
    public void deleteById(Long id) {
        feeRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return feeRepository.existsById(id);
    }

    @Override
    public List<Fee> findByApartmentId(Long apartmentId) {
        return feeRepository.findByApartmentId(apartmentId);
    }

    @Override
    public List<Fee> findByStatus(Fee.FeeStatus status) {
        return feeRepository.findByStatus(status);
    }

    @Override
    public List<Fee> findByDueDateBefore(LocalDate date) {
        return feeRepository.findByDueDateBefore(date);
    }

    @Override
    public List<Fee> findByBuildingId(Long buildingId) {
        return feeRepository.findByBuildingId(buildingId);
    }

    @Override
    public BigDecimal getTotalFeesByBuildingId(Long buildingId) {
        return feeRepository.getTotalFeesByBuildingId(buildingId);
    }

    @Override
    public List<Fee> findOverdueFees() {
        return feeRepository.findOverdueFees();
    }

    @Override
    public void markAsPaid(Long feeId, LocalDate paymentDate) {
        Fee fee = feeRepository.findById(feeId)
                .orElseThrow(() -> new IllegalArgumentException("Fee not found"));

        fee.setStatus(Fee.FeeStatus.PAID);
        fee.setPaymentDate(paymentDate);
        feeRepository.save(fee);
    }

    @Override
    public void generateMonthlyFees(Long buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found"));

        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = now.withDayOfMonth(1);

        // Проверка дали вече не са генерирани такси за този месец
        for (Apartment apartment : building.getApartments()) {
            boolean hasMonthlyFee = apartment.getFees().stream()
                    .anyMatch(fee -> fee.getDueDate().getMonth() == now.getMonth() &&
                            fee.getDueDate().getYear() == now.getYear());

            if (hasMonthlyFee) {
                throw new IllegalStateException(
                        "Monthly fees have already been generated for this month");
            }
        }

        LocalDate dueDate = LocalDate.now().plusMonths(1);

        for (Apartment apartment : building.getApartments()) {
            Fee fee = new Fee();
            fee.setApartment(apartment);
            fee.setAmount(calculateFeeAmount(apartment));
            fee.setDueDate(dueDate);
            fee.setStatus(Fee.FeeStatus.PENDING);
            feeRepository.save(fee);
        }
    }

    @Override
    public BigDecimal calculateTotalUnpaidFees(Long apartmentId) {
        List<Fee> unpaidFees = feeRepository.findByStatus(Fee.FeeStatus.PENDING);
        return unpaidFees.stream()
                .filter(fee -> fee.getApartment().getId().equals(apartmentId))
                .map(Fee::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void applyLateFee(Long feeId, BigDecimal lateFeeAmount) {
        Fee fee = feeRepository.findById(feeId)
                .orElseThrow(() -> new IllegalArgumentException("Fee not found"));

        fee.setAmount(fee.getAmount().add(lateFeeAmount));
        feeRepository.save(fee);
    }

    private BigDecimal calculateFeeAmount(Apartment apartment) {
        BigDecimal baseFee = apartment.getArea().multiply(new BigDecimal("2.5"));

        BigDecimal additionalFees = BigDecimal.ZERO;
        for (var resident : apartment.getResidents()) {
            if (resident.getAge() > 7 && resident.getUsesElevator()) {
                additionalFees = additionalFees.add(new BigDecimal("10"));
            }
            if (resident.getHasPet()) {
                additionalFees = additionalFees.add(new BigDecimal("15"));
            }
        }

        return baseFee.add(additionalFees);
    }
}

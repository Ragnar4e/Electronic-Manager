package service;

import jakarta.persistence.EntityManager;
import org.example.entity.*;
import repository.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ApartmentServiceImpl implements ApartmentService {
    private final EntityManager entityManager;
    private final ApartmentRepository apartmentRepository;
    private final OwnerRepository ownerRepository;
    private final ResidentRepository residentRepository;
    private final FeeRepository feeRepository;

    public ApartmentServiceImpl(EntityManager entityManager,
                                ApartmentRepository apartmentRepository,
                                OwnerRepository ownerRepository,
                                ResidentRepository residentRepository,
                                FeeRepository feeRepository) {
        this.entityManager = entityManager;
        this.apartmentRepository = apartmentRepository;
        this.ownerRepository = ownerRepository;
        this.residentRepository = residentRepository;
        this.feeRepository = feeRepository;
    }

    @Override
    public Apartment save(Apartment apartment) {
        // Validation: Check if apartment data is valid before saving
        validateApartment(apartment);
        return apartmentRepository.save(apartment);
    }

    @Override
    public Optional<Apartment> findById(Long id) {
        return apartmentRepository.findById(id);
    }

    @Override
    public List<Apartment> findAll() {
        return apartmentRepository.findAll();
    }

    @Override
    public void delete(Apartment apartment) {
        apartmentRepository.delete(apartment);
    }

    @Override
    public void deleteById(Long id) {
        apartmentRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return apartmentRepository.existsById(id);
    }

    @Override
    public List<Apartment> findByBuildingId(Long buildingId) {
        return apartmentRepository.findByBuildingId(buildingId);
    }

    @Override
    public List<Apartment> findByFloorNumber(Integer floorNumber) {
        return apartmentRepository.findByFloorNumber(floorNumber);
    }

    @Override
    public List<Apartment> findByApartmentNumber(String apartmentNumber) {
        return apartmentRepository.findByApartmentNumber(apartmentNumber);
    }

    @Override
    public List<Apartment> findApartmentsWithUnpaidFees() {
        return apartmentRepository.findApartmentsWithUnpaidFees();
    }

    @Override
    public List<Apartment> getAllApartmentsSortedByArea() {
        return apartmentRepository.findAllSortedByArea();
    }

    @Override
    public BigDecimal calculateMonthlyFee(Long apartmentId) {
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new IllegalArgumentException("Apartment not found"));

        BigDecimal baseFee = apartment.getArea().multiply(new BigDecimal("2.5"));

        BigDecimal elevatorFee = apartment.getResidents().stream()
                .filter(r -> r.getAge() > 7 && r.getUsesElevator())
                .map(r -> new BigDecimal("10"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal petFee = apartment.getResidents().stream()
                .filter(Resident::getHasPet)
                .map(r -> new BigDecimal("15"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return baseFee.add(elevatorFee).add(petFee);
    }

    @Override
    public void addOwnerToApartment(Long apartmentId, Long ownerId) {
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new IllegalArgumentException("Apartment not found"));
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));

        // Validation: Check if the owner already owns this apartment
        if (owner.getOwnedApartments().contains(apartment)) {
            throw new IllegalStateException("Owner already owns this apartment");
        }

        owner.getOwnedApartments().add(apartment);
        ownerRepository.save(owner);
    }

    @Override
    public void addResidentToApartment(Long apartmentId, Long residentId) {
        // Check if the apartment exists
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new IllegalArgumentException("Apartment not found"));

        // Business validation: Check for maximum number of residents
        int currentResidents = apartment.getResidents().size();
        if (currentResidents >= 6) { // Example: maximum 6 residents
            throw new IllegalStateException("Apartment has reached maximum capacity of residents");
        }

        // Check if the resident exists
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new IllegalArgumentException("Resident not found"));


        if (resident.getApartment() != null) {
            throw new IllegalStateException("Resident is already assigned to another apartment");
        }

        resident.setApartment(apartment);
        residentRepository.save(resident);
    }

    @Override
    public boolean hasOverdueFees(Long apartmentId) {
        List<Fee> overdueFees = feeRepository.findOverdueFees();
        return overdueFees.stream()
                .anyMatch(fee -> fee.getApartment().getId().equals(apartmentId));
    }

    @Override
    public int getResidentCount(Long apartmentId) {
        return residentRepository.findByApartmentId(apartmentId).size();
    }

    // Validation Methods
    private void validateApartment(Apartment apartment) {
        if (apartment == null) {
            throw new IllegalArgumentException("Apartment cannot be null");
        }
        if (apartment.getArea() == null || apartment.getArea().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Apartment area must be greater than 0");
        }
        if (apartment.getApartmentNumber() == null || apartment.getApartmentNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Apartment number cannot be empty");
        }
    }
}

package service;

import jakarta.persistence.EntityManager;
import org.example.entity.Resident;
import org.example.entity.Apartment;
import repository.ResidentRepository;
import repository.ApartmentRepository;
import java.util.List;
import java.util.Optional;

public class ResidentServiceImpl implements ResidentService {
    private final EntityManager entityManager;
    private final ResidentRepository residentRepository;
    private final ApartmentRepository apartmentRepository;

    public ResidentServiceImpl(EntityManager entityManager,
                               ResidentRepository residentRepository,
                               ApartmentRepository apartmentRepository) {
        this.entityManager = entityManager;
        this.residentRepository = residentRepository;
        this.apartmentRepository = apartmentRepository;
    }

    @Override
    public Resident save(Resident resident) {
        return residentRepository.save(resident);
    }

    @Override
    public Optional<Resident> findById(Long id) {
        return residentRepository.findById(id);
    }

    @Override
    public List<Resident> findAll() {
        return residentRepository.findAll();
    }

    @Override
    public void delete(Resident resident) {
        residentRepository.delete(resident);
    }

    @Override
    public void deleteById(Long id) {
        residentRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return residentRepository.existsById(id);
    }

    @Override
    public List<Resident> findByApartmentId(Long apartmentId) {
        return residentRepository.findByApartmentId(apartmentId);
    }

    @Override
    public List<Resident> findByBuildingId(Long buildingId) {
        return residentRepository.findByBuildingId(buildingId);
    }

    @Override
    public List<Resident> findByAgeGreaterThan(Integer age) {
        return residentRepository.findByAgeGreaterThan(age);
    }

    @Override
    public List<Resident> findResidentsWithPets() {
        return residentRepository.findResidentsWithPets();
    }

    @Override
    public List<Resident> findResidentsUsingElevator() {
        return residentRepository.findResidentsUsingElevator();
    }

    @Override
    public void moveResident(Long residentId, Long newApartmentId) {
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new IllegalArgumentException("Resident not found"));
        Apartment newApartment = apartmentRepository.findById(newApartmentId)
                .orElseThrow(() -> new IllegalArgumentException("New apartment not found"));

        // Проверка дали апартаментът не е препълнен
        if (newApartment.getResidents().size() >= 6) {
            throw new IllegalStateException("New apartment is at maximum capacity");
        }

        // Проверка дали не се опитваме да преместим в същия апартамент
        if (resident.getApartment() != null &&
                resident.getApartment().getId().equals(newApartmentId)) {
            throw new IllegalStateException("Resident already lives in this apartment");
        }

        resident.setApartment(newApartment);
        residentRepository.save(resident);
    }

    @Override
    public boolean isEligibleForElevatorFee(Long residentId) {
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new IllegalArgumentException("Resident not found"));
        return resident.getAge() > 7 && resident.getUsesElevator();
    }

    @Override
    public boolean hasPetFee(Long residentId) {
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new IllegalArgumentException("Resident not found"));
        return resident.getHasPet();
    }

    @Override
    public void updateResidentStatus(Long residentId, boolean hasPet, boolean usesElevator) {
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new IllegalArgumentException("Resident not found"));

        resident.setHasPet(hasPet);
        resident.setUsesElevator(usesElevator);
        residentRepository.save(resident);
    }
}
